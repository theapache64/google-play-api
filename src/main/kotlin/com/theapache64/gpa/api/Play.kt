package com.theapache64.gpa.api

import com.akdeniz.googleplaycrawler.GooglePlayAPI
import com.theapache64.gpa.model.Account
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object Play {
    private const val USER_AGENT =
        "Android-Finsky/13.1.32-all (versionCode=81313200,sdk=24,device=dream2lte,hardware=dream2lte,product=dream2ltexx,build=NRD90M:user)"

    suspend fun login(
        username: String,
        password: String,
        localization: String = PlayUtils.getLocalization(),
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Account = withContext(dispatcher) {

        // Building GooglePlayAPI
        val api = GooglePlayAPI(
            username,
            password
        )

        api.client = PlayUtils.createLoginClient()
        api.localization = localization
        api.useragent = USER_AGENT

        api.login()
        api.checkin()

        Account(
            username,
            password,
            api.token,
            api.androidID
        )
    }


}