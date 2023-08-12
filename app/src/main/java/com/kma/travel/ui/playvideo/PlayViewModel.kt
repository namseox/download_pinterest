package com.kma.travel.ui.playvideo

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PlayViewModel @Inject constructor() : ViewModel() {

     fun deleteFile(file: File) {
        file.delete()
    }
}