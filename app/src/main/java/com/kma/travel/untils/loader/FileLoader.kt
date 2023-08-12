package com.kma.travel.utils.loader

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.kma.travel.data.model.VideoStorage
import com.kma.travel.utils.RootPath

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


object FileLoader {
    val videoList = mutableListOf<VideoStorage>()

    fun selectFile(context: Context) {
        var i = 1
        videoList.clear()
        var file = RootPath.getInstance().getSaveFolder(context)
        file.walk().forEach {
            if (i == 1) {
                i = 2
            } else {
                var name = it.name
                var date = Date(it.lastModified())
                var size = it.length()/1024
                videoList += VideoStorage(name, date, size, it.path)
            }
        }

    }

}

