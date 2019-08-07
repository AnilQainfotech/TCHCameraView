package com.example.camerasamplecamerakit

import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.File


private val LOG_TAG = "CameraKitSamle->->"
fun notifyUserWithLog(msg: String, tag: String = "") {
    Log.d(LOG_TAG + tag, msg)
}

fun Context.providePhotosPath(): File {
    val filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    return File(filePath, this.getString(R.string.app_name))
}

internal fun Context.provideVideosPath(): File {

    val filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
    return File(filePath, this.getString(R.string.app_name))
}