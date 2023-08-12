package com.kma.travel.ui.home.data.repository

import android.content.Context
import android.util.Log
import com.kma.travel.ui.home.data.DownloadStatus
import com.kma.travel.utils.FileNameUtils
import com.kma.travel.utils.RootPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadRepository @Inject constructor() {
    fun downloadPinterestDownloader(
        context: Context, itemId: String,
        url: String
    ): Flow<DownloadStatus?> {
        return flow {
            var newName = FileNameUtils.convertSpaceToSnakeCaseNaming(itemId).plus(".mp4")
            newName = newName.replace('/', '_')
            Log.d("afdafdsafsda", "fs = $newName")
            val okHttpClient: OkHttpClient =
                OkHttpClient.Builder().readTimeout((10 * 1000).toLong(), TimeUnit.MILLISECONDS)
                    .build()

            val tempFile = File(RootPath.getInstance().getSaveFolder(context), newName)
            var input: InputStream? = null

            val output: OutputStream = FileOutputStream(tempFile)
            try {
                val request: Request = Request.Builder()
                    .url(url)
                    .build()
                val response: Response = okHttpClient.newCall(request).execute()
                if (response.isSuccessful) {
                    input = response.body!!.byteStream()
                    val tLength = response.body!!.contentLength()
                    val data = ByteArray(1024)
                    var total: Long = 0
                    var count: Int
                    while (input.read(data).also { count = it } != -1) {
                        total += count.toLong()
                        output.write(data, 0, count)
                        if (tLength > 0) {
                            emit(
                                DownloadStatus(
                                    DownloadStatus.DownloadState.Downloading,
                                    (total * 100 / tLength).toInt()
                                )
                            )
                        }
                    }
                    output.flush()

                    emit(DownloadStatus(DownloadStatus.DownloadState.Complete, 100))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                tempFile.delete()
                emit(DownloadStatus(DownloadStatus.DownloadState.Failed, 0))
            } finally {
                if (input != null) {
                    try {
                        input.close()
                    } catch (_: IOException) {
                    }
                }
                try {
                    output.close()
                } catch (_: IOException) {
                }

            }
        }
    }


}