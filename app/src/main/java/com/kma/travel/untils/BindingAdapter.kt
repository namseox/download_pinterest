package com.kma.travel.utils

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.kma.travel.R
import com.kma.travel.data.model.VideoStorage

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


@BindingAdapter("setImage")
fun ImageView.setImage(path: String) {
    Glide.with(this)
        .load(path)
        .error(R.color.black)
        .into(this)

}

@BindingAdapter("setData")
fun TextView.setData(videoStorage: VideoStorage) {
    var timenow = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
    var time = videoStorage.date.toString().substring(11,13)+":" +videoStorage.date.toString().substring(14,16)
    val DateFor = SimpleDateFormat("dd-MM-yyyy")
    val stringDate = DateFor.format(videoStorage.date)

Log.d("////",stringDate +"\n"+ timenow)
    if (stringDate.equals(timenow)){
        this.text = (Math.round((videoStorage.size.toDouble() / 1024.0) * 10.0)/ 10.0).toString() + " MB, " + "Today " + time
    }else{
        this.text = (Math.round((videoStorage.size.toDouble() / 1024.0) * 10.0)/ 10.0).toString() + " MB, " + stringDate +" " + time
    }

}
