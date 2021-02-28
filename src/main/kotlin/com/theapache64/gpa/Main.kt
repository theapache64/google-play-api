package com.theapache64.gpa

import com.theapache64.gpa.core.Play

fun main(args: Array<String>) {
    val username = System.getenv("GOOGLE_USERNAME")
    val password = System.getenv("GOOGLE_PASSWORD")

    Play.auth(username, password)

}