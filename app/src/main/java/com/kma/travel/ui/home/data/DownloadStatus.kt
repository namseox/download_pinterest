package com.kma.travel.ui.home.data

data class DownloadStatus(val downloadState: DownloadState, val percentage: Int) {
    enum class DownloadState {
        Idle, Downloading,Unzip, Complete, Failed
    }
}