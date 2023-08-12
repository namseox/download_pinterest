package com.kma.travel.ui.myfile


import android.content.Intent
import android.util.Log
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.activity.OnBackPressedCallback
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kma.travel.R
import com.kma.travel.data.model.VideoStorage
import com.kma.travel.databinding.FragmentMyFileBinding
import com.kma.travel.databinding.ItemVideoBinding
import com.kma.travel.ui.base.AbsBaseFragment
import com.kma.travel.ui.base.dialog.popup.ActionAdapter
import com.kma.travel.ui.base.dialog.popup.ActionModel
import com.kma.travel.ui.base.dialog.popup.ListActionPopup

import com.kma.travel.ui.main.MainFragmentDirections
import com.kma.travel.utils.ShareFile
import java.io.File
import java.lang.reflect.Type


class MyFileFragment : AbsBaseFragment<FragmentMyFileBinding>(), onClick ,OnClickItemVideo{
    private val listActionPopup by lazy { ListActionPopup(requireActivity()) }
    private val actions = arrayListOf<ActionModel>(ActionModel("Delete"), ActionModel("Share"))
    lateinit var viewModelMyFile: ViewModelMyFile
    var data = listOf<VideoStorage>()
    lateinit var adapterVideo: GetVideoAdapter
    override fun getLayout(): Int = R.layout.fragment_my_file

    override fun initView() {
        onBackPressed()
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>("key")?.observe(viewLifecycleOwner) {result ->
            if (result) {
                loadVideo()
            }
        }
        adapterVideo = GetVideoAdapter(this, this)
        viewModelMyFile = ViewModelMyFile()
        loadVideo()

    }


    fun loadVideo() {
        viewModelMyFile.getVideo(requireContext()).observe(viewLifecycleOwner) {
            data = it
            if (data.size < 1) {
                binding.tvNovideo.text = "No Video"
            } else {
                binding.tvNovideo.text = ""
            }
            setVideoRecycleView()
        }
    }

    fun setVideoRecycleView() {
        adapterVideo.getData(data)
        binding.rcvVideo.adapter = adapterVideo
        val manager = GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)
        binding.rcvVideo.layoutManager = manager

    }


    override fun onClickItemVideo(video: VideoStorage, binding: ItemVideoBinding) {
        super.onClickItemVideo(video, binding)
        findNavController().navigate(MainFragmentDirections.actionGlobalPlayFragment(video.url))
    }


    override fun clickDot(video: VideoStorage, binding: ItemVideoBinding) {
        val videoFile: File = File(
            video.url
        )
        listActionPopup.showPopup(binding.imv3dot, actions,object : ActionAdapter.OnActionClickListener{
            override fun onItemActionClick(position: Int) {
                when(position){
                    0 -> {
                        var myFileDialog = MyFileDialog.create(object : MyFileDialog.IListener {
                            override fun delete() {
                                deleteFile(videoFile)
                            }
                        })
                        myFileDialog.show(childFragmentManager, "myfileDialog")
                    }
                    else -> ShareFile.shareFile(video.url, requireActivity())
                }
            }

        })
    }


    private var isViewShown = false

    @Suppress("DEPRECATION")
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (view != null && isVisibleToUser) {
            isViewShown =
                true
            loadVideo()
        } else {
            isViewShown = false
        }
    }

    fun deleteFile(file: File) {
        file.delete()
        loadVideo()
    }

    private fun onBackPressed() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (listActionPopup.isShowing()) {
                    listActionPopup.dismiss()
                } else {
                    requireActivity().finish()
                }

            }

        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

}