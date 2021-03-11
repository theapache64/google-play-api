package com.github.theapache64.gpa.model

data class Account(
    val username: String,
    val password: String,
    val token: String,
    val gsfId: String,
    val locale: String
)