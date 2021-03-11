package com.github.theapache64.gpa.api

import com.akdeniz.googleplaycrawler.GooglePlayAPI
import com.akdeniz.googleplaycrawler.GooglePlayException
import com.github.theapache64.gpa.utils.runBlockingTest
import com.github.theapache64.gpa.utils.testAccount
import com.theapache64.expekt.should
import kotlinx.coroutines.delay
import org.apache.http.client.ClientProtocolException
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File
import java.io.FileOutputStream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class PlayTest {


    private lateinit var api: GooglePlayAPI

    @BeforeAll
    @Test
    fun givenValidCreds_whenLogin_thenSuccess() = runBlockingTest {
        val username = System.getenv("PLAY_API_GOOGLE_USERNAME")!!
        val password = System.getenv("PLAY_API_GOOGLE_PASSWORD")!!

        //val account = Play.login(username, password)
        val account = testAccount
        account.should.not.`null`
        delay(3000) // wait to sync the id in google's blood
        api = Play.getApi(account)
    }

    @Test
    fun givenInvalidCreds_whenLogin_thenError() = runBlockingTest {
        try {
            Play.login("", "")
            assert(false)
        } catch (e: ClientProtocolException) {
            assert(true)
        }
    }

    @Test
    fun givenValidPackageName_whenGetPackageDetails_thenSuccess() {
        val packageName = "com.theapache64.papercop"
        val details = api.details(packageName)
        details.docV2.docid.should.equal(packageName)
    }

    @Test
    fun givenInvalidPackageName_whenGetPackageDetails_thenError() {
        val packageName = ""
        try {
            api.details(packageName)
            assert(false)
        } catch (e: GooglePlayException) {
            assert(true)
        }
    }

    /**
     * Search test
     */
    @Test
    fun givenValidKeyword_whenSearch_thenSuccess() {
        val keyword = "WhatsApp"
        var serp = Play.search(keyword, api)

        repeat(10) {
            serp = Play.search(keyword, api, serp)
        }

        println("**********************")
        val file = File("build/test_output/${System.currentTimeMillis()}.txt")
        file.parentFile.mkdirs()
        file.createNewFile()
        serp.content.forEach {
            file.appendText(
                "${it.docid}\n"
            )
        }
    }

    @Test
    fun givenValidSmallPackageName_whenDownload_thenSuccess() {
        downloadApkAndTest("a.i")
    }

    @Test
    fun givenValidMediumPackageName_whenDownload_thenSuccess() {
        downloadApkAndTest("com.theapache64.papercop")
    }


    /**
     * To download APK
     */
    private fun downloadApkAndTest(packageName: String) {
        val apkFile = File("$packageName.apk")
        val details = api.details(packageName)
        val downloadData = api.purchaseAndDeliver(
            packageName,
            details.docV2.details.appDetails.versionCode,
            1,
        )
        downloadData.openApp().use { input ->
            FileOutputStream(apkFile).use { output ->
                input.copyTo(output)
            }
        }

        apkFile.exists().should.`true`
        apkFile.length().should.equal(downloadData.totalSize)
        apkFile.delete() // test finished, so deleting downloaded file
    }
}