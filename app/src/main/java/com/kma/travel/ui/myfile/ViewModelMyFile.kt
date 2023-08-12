package com.kma.travel.ui.myfile

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kma.travel.data.model.VideoStorage
import com.kma.travel.utils.loader.FileLoader

class ViewModelMyFile : ViewModel() {
    var listVideo: MutableLiveData<List<VideoStorage>> = MutableLiveData()
    fun getVideo(context: Context): MutableLiveData<List<VideoStorage>> {
        FileLoader.selectFile(context)
        listVideo.postValue(FileLoader.videoList)
        return listVideo

    }
}