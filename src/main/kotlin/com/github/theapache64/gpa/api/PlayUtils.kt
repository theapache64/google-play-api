package com.github.theapache64.gpa.api

import com.github.theapache64.gpa.core.net.DroidConnectionSocketFactory
import org.apache.http.client.HttpClient
import org.apache.http.client.config.RequestConfig
import org.apache.http.config.RegistryBuilder
import org.apache.http.conn.socket.ConnectionSocketFactory
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import java.util.*

internal object PlayUtils {
    fun createLoginClient(): HttpClient? {
        val rb = RegistryBuilder.create<ConnectionSocketFactory>()
        rb.register("https", DroidConnectionSocketFactory())
        // rb.register("http", new DroidConnectionSocketFactory());
        val connManager = PoolingHttpClientConnectionManager(
            rb.build()
        )
        connManager.maxTotal = 100
        connManager.defaultMaxPerRoute = 30
        // TODO: Increase the max connection limits. If we are doing bulkdownloads,
        // we will download from multiple hosts.
        val timeout = 9
        val config = RequestConfig.custom()
            .setConnectTimeout(timeout * 1000)
            .setConnectionRequestTimeout(timeout * 1000)
            .setSocketTimeout(timeout * 1000).build()
        val hcb = HttpClientBuilder.create().setDefaultRequestConfig(
            config
        )
        return hcb.setConnectionManager(connManager).build()
    }

    fun getLocalization(): String {
        return Locale.getDefault().let { locale ->
            "${locale.language}-${locale.country}"
        }
    }
}