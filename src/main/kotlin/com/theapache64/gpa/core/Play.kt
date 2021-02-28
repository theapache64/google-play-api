package com.theapache64.gpa.core

import com.akdeniz.googleplaycrawler.GooglePlayAPI
import de.onyxbits.raccoon.net.DroidConnectionSocketFactory
import org.apache.http.client.HttpClient
import org.apache.http.client.config.RequestConfig
import org.apache.http.config.RegistryBuilder
import org.apache.http.conn.socket.ConnectionSocketFactory
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager

object Play {
    fun auth(username: String, password: String) {

        // Building GooglePlayAPI
        val api = GooglePlayAPI(
            username,
            password
        )
        // api.useragent = "Android-Finsky/13.1.32-all (versionCode=81313200,sdk=24,device=dream2lte,hardware=dream2lte,product=dream2ltexx,build=NRD90M:user)"
        // ret.token = "7QcNrnwIT-Z_MMdFcMBh4WtplXA5aRg0WUhCngLE7jTT8Swtf4gAj805ZGDUyv5imo6dIg."
        // api.androidID = "369d2143ebec0776"
        api.client = createLoginClient()
        api.localization = "en-IN"

        api.login()
        println("Token : ${api.token}")
        // ret.search("papercop").let { println(it) }

    }

    private fun createLoginClient(): HttpClient? {
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

}