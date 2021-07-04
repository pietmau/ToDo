package com.pppp.uielements

import android.util.Log

fun fooLog(text: String, tag: String = "foo") {
    if (BuildConfig.DEBUG) {
        Log.e(tag, text)
    }
}