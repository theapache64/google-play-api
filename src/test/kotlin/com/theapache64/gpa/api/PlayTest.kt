package com.theapache64.gpa.api

import com.akdeniz.googleplaycrawler.GooglePlayAPI
import com.akdeniz.googleplaycrawler.GooglePlayException
import com.theapache64.expekt.should
import com.theapache64.gpa.model.Account
import com.theapache64.gpa.utils.runBlockingTest
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

        val account = Play.login(username, password)
        account.should.not.`null`
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

    @Test
    fun givenValidSmallPackageName_whenDownload_thenSuccess() {
        downloadApk("a.i")
    }

    @Test
    fun givenValidMediumPackageName_whenDownload_thenSuccess() {
        downloadApk("com.theapache64.papercop")
    }


    /**
     * To download APK
     */
    private fun downloadApk(packageName: String) {
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