package com.theapache64.gpa

import java.util.prefs.Preferences

fun main(args: Array<String>) {
    val username = System.getenv("GOOGLE_USERNAME")
    val password = System.getenv("GOOGLE_PASSWORD")

    val preference = Preferences.userRoot().node("my_prefs")
    println("Get -> ${preference.get("super-key", null)}")
    preference.put("super-key", "super-value")
    println("Get 2-> ${preference.get("super-key", null)}")

    // Play.auth(username, password)

}