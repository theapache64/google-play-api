package com.github.theapache64.gpa.api

import com.akdeniz.googleplaycrawler.GooglePlayAPI
import com.github.theapache64.gpa.core.SearchEngineResultPage
import com.github.theapache64.gpa.model.Account
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

    fun search(
        query: String,
        api: GooglePlayAPI,
        _serp: SearchEngineResultPage? = null
    ): SearchEngineResultPage {

        var serp = _serp
        var nextPageUrl: String? = null

        if (serp != null) {
            // second+ time
            nextPageUrl = serp.nextPageUrl
        }

        if (serp == null) {
            serp = SearchEngineResultPage(SearchEngineResultPage.SEARCH)
        }

        serp.append(api.searchApp(query))

        if (nextPageUrl == null) {
            // first time
            nextPageUrl = serp.nextPageUrl
        }

        if (nextPageUrl?.isNotBlank() == true) {
            serp.append(api.getList(nextPageUrl))
        }

        return serp
    }


}