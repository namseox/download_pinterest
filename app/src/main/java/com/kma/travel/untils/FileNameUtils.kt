package com.kma.travel.utils

import android.util.Log
import java.text.Normalizer
import java.util.regex.Pattern

object FileNameUtils {
    fun convertSpaceToSnakeCaseNaming(fileName: String): String {
        val fileNameFilter = "?\"*"
        val key =  fileName.filterNot { fileNameFilter.indexOf(it) > -1 }.trim()
            .replace("\\s+".toRegex(), "_")
      return  deAccent(key) ?:key
    }

    fun convertSnakeToSpaceCaseNaming(fileName: String): String {
        return fileName.trim().replace("_".toRegex(), " ")
    }

    private fun deAccent(str: String?): String? {
        val nfdNormalizedString: String = Normalizer.normalize(str, Normalizer.Form.NFC)
        val pattern: Pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
        Log.d("....../........",pattern.matcher(nfdNormalizedString).replaceAll(""))
        return pattern.matcher(nfdNormalizedString).replaceAll("")
    }
}