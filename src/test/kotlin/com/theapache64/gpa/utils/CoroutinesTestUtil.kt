package com.theapache64.gpa.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

fun runBlockingTest(block: suspend (scope: CoroutineScope) -> Unit) = runBlocking {
    block(this)
    Unit
}