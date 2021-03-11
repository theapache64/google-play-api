package com.github.theapache64.gpa.core

import com.akdeniz.googleplaycrawler.GooglePlay.DocV2
import com.akdeniz.googleplaycrawler.GooglePlay
import com.github.theapache64.gpa.core.Unwrap
import com.akdeniz.googleplaycrawler.GooglePlay.PreFetch
import com.google.protobuf.InvalidProtocolBufferException
import com.github.theapache64.gpa.core.SearchEngineResultPage
import java.util.ArrayList

/**
 * A (relatively) smart adapter for transforming the various search response
 * formats into a flat, continuous list.
 *
 * @author patrick
 */
class SearchEngineResultPage(type: Int) {
    private val items: ArrayList<DocV2> = ArrayList()

    /**
     * Check if results are available.
     *
     * @return null if there are no more search results to load.
     */
    var nextPageUrl: String?
        private set

    /**
     * Get the title of this page (if any).
     *
     * @return null or the title of the first appended doc.
     */
    var title: String? = null
        private set
    private val type: Int

    /**
     * Try to make sense of a [ResponseWrapper], containing a search result.
     *
     * @param rw
     * a wrapper containing either a [SearchResponse],
     * [ListResponse] or a [PreFetch]
     */
    fun append(rw: GooglePlay.ResponseWrapper) {
        // The SearchResponse format changed considerably over time. The message
        // type seems to have gotten deprecated for Android 5 and later in favor of
        // ListResponse. Apparently, SearchResponse got too too unwieldy.
        append(Unwrap.searchResponse(rw).docList)
        append(Unwrap.listResponse(rw).docList)
        for (pf in rw.preFetchList) {
            try {
                append(GooglePlay.ResponseWrapper.parseFrom(pf.response))
            } catch (e: InvalidProtocolBufferException) {
                // We tried, we failed.
            }
        }
    }

    private fun append(list: List<DocV2>) {
        for (doc in list) {
            append(doc)
        }
    }

    /**
     * Grow the SERP
     *
     * @param doc
     * a document of type [DocumentType.PRODUCTLIST] or a document
     * containing a [DocumentType.PRODUCTLIST].
     */
    fun append(doc: DocV2) {
        when (doc.docType) {
            46 -> {
                for (child in doc.childList) {
                    if (accept(child)) {
                        append(child)
                    }
                }
            }
            45 -> {
                for (d in doc.childList) {
                    if (d.docType == 1) {
                        items.add(d)
                    }
                }
                nextPageUrl = null
                if (doc.hasContainerMetadata()) {
                    nextPageUrl = doc.containerMetadata.nextPageUrl
                }
                if (title == null && doc.hasTitle()) {
                    title = doc.title
                }
            }
            else -> {
                for (child in doc.childList) {
                    append(child)
                }
            }
        }
    }

    private fun accept(doc: DocV2): Boolean {
        val dbid = doc.backendDocid
        return when (type) {
            ALL -> {
                true
            }
            SEARCH -> {
                dbid != null && dbid.matches(".*search.*".toRegex())
            }
            SIMILIAR -> {
                dbid != null && dbid.matches("similar_apps".toRegex())
            }
            RELATED -> {
                dbid != null && dbid
                    .matches("pre_install_users_also_installed".toRegex())
            }
            else -> {
                false
            }
        }
    }

    /**
     * Get the entry list.
     *
     * @return a flat list.
     */
    val content: List<DocV2>
        get() = items

    override fun toString(): String {
        val ret = StringBuilder()
        if (title != null) {
            ret.append('[')
            ret.append(title)
            ret.append("]\n")
        }
        for (item in items) {
            ret.append(item.docid)
            ret.append(", ")
            ret.append("\"")
            ret.append(item.title)
            ret.append("\"\n")
        }
        if (nextPageUrl != null) {
            ret.append("-> ")
            ret.append(nextPageUrl)
            ret.append('\n')
        }
        return ret.toString()
    }

    companion object {
        /**
         * Type: everything
         */
        const val ALL = 0

        /**
         * Type: only append what was searched for
         */
        const val SEARCH = 1

        /**
         * Type: only append similar items. This requires an exact match
         */
        const val SIMILIAR = 2

        /**
         * Type: only append items of the "other users also..." type. This requires an
         * exact match.
         */
        const val RELATED = 3
    }

    /**
     *
     * @param type
     * Either ALL, SEARCH, SIMILAR or RELATED. Only Applies when trying
     * to add [DocumentType.MULTILIST].
     */
    init {
        nextPageUrl = null
        this.type = type
    }
}