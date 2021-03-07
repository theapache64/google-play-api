package com.theapache64.gpa.api

import com.akdeniz.googleplaycrawler.GooglePlayAPI
import com.theapache64.gpa.model.Account
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object Play {
    private const val USER_AGENT =
        "Android-Finsky/13.1.32-all (versionCode=81313200,sdk=24,device=dream2lte,hardware=dream2lte,product=dream2ltexx,build=NRD90M:user)"

    /**
     * To login and get userToken and gsfId
     */
    suspend fun login(
        username: String,
        password: String,
        locale: String = PlayUtils.getLocalization(),
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Account = withContext(dispatcher) {

        // Building GooglePlayAPI
        val api = GooglePlayAPI(
            username,
            password
        ).apply {
            client = PlayUtils.createLoginClient()
            localization = locale
            useragent = USER_AGENT
        }


        // Requesting for login
        api.login()

        // To get GSF id
        api.checkin()

        Account(
            username,
            password,
            api.token,
            api.androidID,
            locale
        )
    }

    fun getApi(account: Account): GooglePlayAPI {
        return GooglePlayAPI(
            account.username,
            account.password,
            account.gsfId
        ).apply {
            localization = account.locale
            token = account.token
        }
    }


}