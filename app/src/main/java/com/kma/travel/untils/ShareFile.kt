package com.kma.travel.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File

object ShareFile {
    fun shareFile(path: String,context: Context) {
        Log.d("///////", "path " + path)
        val internalFile = File("$path")
        val contentUri = FileProvider.getUriForFile(
            context,
            context.packageName + ".provider",
            internalFile
        )
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "*/*"
        sharingIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        sharingIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
        context.startActivity(Intent.createChooser(sharingIntent, "Share"))
    }
}