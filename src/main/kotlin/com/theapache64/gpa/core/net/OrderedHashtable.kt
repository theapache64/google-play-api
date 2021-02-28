package com.theapache64.gpa.core.net

import java.util.*

/**
 * Dirty hack to maintain key order.
 *
 * @author patrick
 */
internal class OrderedHashtable : Hashtable<Int, Any>() {

    private val ordered = Vector<Int>()

    @Synchronized
    override fun put(key: Int, value: Any): Any {
        super.put(key, value)
        ordered.add(key)
        return value
    }

    @Synchronized
    override fun keys(): Enumeration<Int> {
        return ordered.elements()
    }

    companion object {
        /**
         *
         */
        private const val serialVersionUID = 1L
    }
}