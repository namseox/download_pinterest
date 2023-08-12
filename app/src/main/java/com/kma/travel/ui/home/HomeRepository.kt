package com.kma.travel.ui.home

import android.util.Log
import com.google.gson.Gson
import com.kma.travel.data.model.Video
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jsoup.Jsoup
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor() {
    var check: Boolean = true
    fun checkLink(
        url: String
    ): Flow<Video?> {
        return flow {
            try {

                var resultLink: String? = null
                val doc = Jsoup.connect(url).get()
                val scripts = doc.select("script")


                for (i in scripts) {

                    val start = i.toString().indexOf("current_url")
                    var end = i.toString().indexOf("sent", start) - 1

                    if (end < start + 5 || end > start + 100) {
                        end = i.toString().indexOf("debug", start) - 3
                    }

                    if (start > 0 && end > 0) {
                        val link = (i.toString().substring(start + 14, end))

                        resultLink = link
                    }

                }

                if (resultLink != null) {
                    emit(getInformationDownload(resultLink))

                } else {
                    emit(null)
                }


            } catch (ex: UnknownHostException) {
                emit(null)
            } catch (ex: Exception) {
                emit(null)
            }
        }
    }

    private fun getInformationDownload(url: String): Video? {
        return try {
            var result: Video? = null
            var stringJson = ""

            val doc = Jsoup.connect(url).get()
            if (doc.toString().contains("storypin")) {

                check = false
            }
            val scripts = doc.select("script")
            for (i in scripts) {
                if (i.toString().indexOf("\"contentUrl\":\"https://v.pinimg.com/videos") > 0) {
                    stringJson = i.toString().substring(64, i.toString().length - 9)
                }
            }

            if (stringJson.isNotEmpty() && check) {
                val gson = Gson()
                return gson.fromJson(stringJson, Video::class.java)
            } else {
                return null
            }


        } catch (ex: UnknownHostException) {
            null
        } catch (ex: Exception) {
            null
        }

    }


}