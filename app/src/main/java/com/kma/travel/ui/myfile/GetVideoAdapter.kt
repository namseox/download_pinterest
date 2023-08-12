package com.kma.travel.ui.myfile

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.kma.travel.R
import com.kma.travel.data.model.VideoStorage
import com.kma.travel.databinding.ItemVideoBinding
import com.kma.travel.utils.Utils.setMargin
import java.util.*

class GetVideoAdapter(var onClick: onClick, var onClickItemVideo: OnClickItemVideo) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var data = mutableListOf<VideoStorage>()
    fun getData(data: List<VideoStorage>){
        this.data.clear()
        this.data.addAll(data)
        val comparator = compareBy<VideoStorage> { it.date }
        this.data.sortWith(comparator.reversed())
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding,data.size)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(position)
            holder.binding.imv3dot.setOnClickListener(){
                onClick.clickDot(data[position],holder.binding)
            }
            holder.binding.itemVideo.setOnClickListener {
                onClickItemVideo.onClickItemVideo(data[position], holder.binding)
            }
        }
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder( val binding: ItemVideoBinding, val size: Int) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.videoModel = data[position]
            if(position == size-1){
                var a = binding.itemVideo.context.resources.getDimension(R.dimen.dp_16).toInt()
                binding.itemVideo.setMargin(0,a,0,a)
            }

        }
    }
}
interface onClick{
    fun clickDot(video : VideoStorage, binding: ItemVideoBinding){
    }
}
interface OnClickItemVideo{
    fun onClickItemVideo(video : VideoStorage, binding: ItemVideoBinding){
    }
}


