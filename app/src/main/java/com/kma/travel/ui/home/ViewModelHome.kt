package com.kma.travel.ui.home

import android.content.Context
import androidx.lifecycle.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kma.travel.data.api.reponse.DataResponse
import com.kma.travel.data.api.reponse.LoadingStatus
import com.kma.travel.data.model.Video
import com.kma.travel.ui.home.data.DownloadStatus
import com.kma.travel.ui.home.data.repository.DownloadRepository
import com.kma.travel.utils.CheckInternet
import com.kma.travel.utils.FileNameUtils
import com.kma.travel.utils.RootPath
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ViewModelHome @Inject constructor(
    private val homeRepository: HomeRepository,
    private val downloadRepository: DownloadRepository
) : ViewModel() {
    var checkStory: MutableLiveData<Boolean> = MutableLiveData()

    val downloadStatus =
        MutableLiveData(DownloadStatus(DownloadStatus.DownloadState.Idle, 0))

    val checkLinkLiveData =
        MutableLiveData<DataResponse<Video?>>(DataResponse.DataIdle())

    private var jobCheckLink: Job? = null

    fun cancelJobCheckLink() {
        jobCheckLink?.cancel()

    }



    fun checkLink(
        url: String
    ) {
        checkLinkLiveData.value = DataResponse.DataLoading(LoadingStatus.Loading)
        jobCheckLink = viewModelScope.launch(Dispatchers.IO) {
            downloadFile(url)
        }

    }

    private suspend fun downloadFile(
        url: String
    ) {
        homeRepository.checkLink(url)
            .flowOn(Dispatchers.Default)
            .collect {
                it?.let {

                }
                if (it != null) {
                    checkLinkLiveData.postValue(DataResponse.DataSuccess(it))
                } else {
                    checkLinkLiveData.postValue(DataResponse.DataError())
                }
                //checkLinkLiveData.postValue(it)
                var check = homeRepository.check
                checkStory.postValue(check)
                homeRepository.check = true
            }


    }


    private var jobDownloadFile: Job? = null

    fun cancelJobDownloadFile() {
        jobDownloadFile?.cancel()
    }

    fun downloadFiles(
        context: Context, itemId: String,
        url: String
    ) {
        downloadStatus.value = (DownloadStatus(DownloadStatus.DownloadState.Idle, 0))
        jobDownloadFile = viewModelScope.launch(Dispatchers.IO) {
            val pinterestFile = File(
                RootPath.getInstance().getSaveFolder(context),
                FileNameUtils.convertSpaceToSnakeCaseNaming(itemId).plus(".mp4")
            )
            downloadFile(context, itemId, url)
            if (!pinterestFile.exists()) {
                //downloadFile(context, itemId, url)
            } else {
                downloadStatus.postValue(DownloadStatus(DownloadStatus.DownloadState.Complete, 100))

            }

        }

    }

    private suspend fun downloadFile(
        context: Context, itemId: String,
        url: String
    ) {
        if (downloadStatus.value!!.downloadState != DownloadStatus.DownloadState.Downloading) {
            downloadRepository.downloadPinterestDownloader(context, itemId, url)
                .flowOn(Dispatchers.Default)
                .collect {
                    it?.let {
                        if (it.downloadState == DownloadStatus.DownloadState.Complete) {
                        }

                    }
                    downloadStatus.postValue(it)
                }
        }

    }

    fun checkForInternet(context: Context): Boolean {
        return CheckInternet.checkForInternet(context)
    }
}