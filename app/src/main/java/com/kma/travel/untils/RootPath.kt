package com.kma.travel.utils

import android.content.Context
import java.io.File

class RootPath private constructor() {
    companion object {
        private const val PINTEREST_DOWNLOADER = "/PinterestDownloader/"
        private var instance: RootPath? = null
        fun getInstance(): RootPath {
            if (instance == null) {
                instance = RootPath()
            }
            return instance!!
        }
    }


    fun getPinterestDownloadFolder(): File {
        val basePath = Utils.getBasePath()
        val dirName = basePath + PINTEREST_DOWNLOADER
        val file = File(dirName)
        if (!file.exists()) {
            file.mkdirs()
        }
        return file
    }


    fun getSaveFolder(context: Context): File {
        val file = File(context.filesDir, "PinterestDownloader")
        if (!file.exists()) {
            file.mkdirs()
        }
        return file
    }


}