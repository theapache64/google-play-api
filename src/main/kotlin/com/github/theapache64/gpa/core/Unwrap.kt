package com.github.theapache64.gpa.core

import com.akdeniz.googleplaycrawler.GooglePlay
import com.akdeniz.googleplaycrawler.GooglePlay.Payload
import com.akdeniz.googleplaycrawler.GooglePlay.SearchResponse
import com.github.theapache64.gpa.core.Unwrap
import com.akdeniz.googleplaycrawler.GooglePlay.ListResponse
import com.akdeniz.googleplaycrawler.GooglePlay.DeliveryResponse
import com.akdeniz.googleplaycrawler.GooglePlay.BulkDetailsResponse
import com.akdeniz.googleplaycrawler.GooglePlay.DetailsResponse
import com.akdeniz.googleplaycrawler.GooglePlay.TocResponse
import com.akdeniz.googleplaycrawler.GooglePlay.UploadDeviceConfigResponse

/**
 * Extract a response from a [ResponseWrapper]. Return an empty instance
 * if the requested message is not available.
 *
 * @author patrick
 */
object Unwrap {
    fun payload(rw: GooglePlay.ResponseWrapper?): Payload {
        return if (rw != null && rw.hasPayload()) {
            rw.payload
        } else Payload.getDefaultInstance()
    }

    fun searchResponse(rw: GooglePlay.ResponseWrapper?): SearchResponse {
        val pl = payload(rw)
        return if (payload(rw).hasSearchResponse()) {
            pl.searchResponse
        } else SearchResponse.getDefaultInstance()
    }

    fun listResponse(rw: GooglePlay.ResponseWrapper?): ListResponse {
        val pl = payload(rw)
        return if (pl.hasListResponse()) {
            pl.listResponse
        } else ListResponse.getDefaultInstance()
    }

    fun deliveryResponse(rw: GooglePlay.ResponseWrapper?): DeliveryResponse {
        val pl = payload(rw)
        return if (pl.hasDeliveryResponse()) {
            pl.deliveryResponse
        } else DeliveryResponse.getDefaultInstance()
    }

    fun bulkDetailsResponse(rw: GooglePlay.ResponseWrapper?): BulkDetailsResponse {
        val pl = payload(rw)
        return if (pl.hasBulkDetailsResponse()) {
            pl.bulkDetailsResponse
        } else BulkDetailsResponse.getDefaultInstance()
    }

    fun detailsResponse(rw: GooglePlay.ResponseWrapper?): DetailsResponse {
        val pl = payload(rw)
        return if (pl.hasDetailsResponse()) {
            pl.detailsResponse
        } else DetailsResponse.getDefaultInstance()
    }

    fun tocResponse(rw: GooglePlay.ResponseWrapper?): TocResponse {
        val pl = payload(rw)
        return if (pl.hasTocResponse()) {
            pl.tocResponse
        } else TocResponse.getDefaultInstance()
    }

    fun uploadDeviceConfigResponse(
        rw: GooglePlay.ResponseWrapper?
    ): UploadDeviceConfigResponse {
        val pl = payload(rw)
        return if (pl.hasUploadDeviceConfigResponse()) {
            pl.uploadDeviceConfigResponse
        } else UploadDeviceConfigResponse.getDefaultInstance()
    }
}