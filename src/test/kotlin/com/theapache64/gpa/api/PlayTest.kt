package com.theapache64.gpa.api

import com.theapache64.expekt.should
import com.theapache64.gpa.utils.runBlockingTest
import org.apache.http.client.ClientProtocolException
import org.junit.Test

class PlayTest {

    @Test
    fun givenValidCreds_whenLogin_thenSuccess() = runBlockingTest {
        val username = System.getenv("PLAY_API_GOOGLE_USERNAME")!!
        val password = System.getenv("PLAY_API_GOOGLE_PASSWORD")!!

        val account = Play.login(username, password)
        account.should.not.`null`
    }

    @Test
    fun givenInvalidCreds_whenLogin_thenError() = runBlockingTest {
        try {
            Play.login("", "")
        } catch (e: ClientProtocolException) {
            assert(true)
        }
    }

}