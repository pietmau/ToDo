package com.pppp.uielements

import android.media.effect.Effect
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

fun fooLog(text: String, tag: String = "foo") {
    if (BuildConfig.DEBUG) {
        Log.e(tag, text)
    }
}
