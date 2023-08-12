package com.kma.travel.ui.playvideo

import android.content.Intent
import android.net.Uri
import android.widget.ImageButton
import androidx.activity.OnBackPressedCallback
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.kma.travel.R
import com.kma.travel.databinding.FragmentPlayBinding
import com.kma.travel.ui.base.AbsBaseFragment
import com.kma.travel.ui.myfile.MyFileDialog
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class PlayFragment : AbsBaseFragment<FragmentPlayBinding>() {
    private val mViewModel: PlayViewModel by viewModels()
    private val args: PlayFragmentArgs by navArgs()
    private lateinit var playPauseBtn: ImageButton
    private lateinit var backBtn: ImageButton
    private lateinit var deleteBtn: ImageButton
    private lateinit var shareBtn: ImageButton
    private lateinit var volBtn: ImageButton

    private var exoPlayer: ExoPlayer? = null
    private var playbackPosition = 0L
    private var playWhenReady = true
    private var urlVideo = ""


    override fun getLayout(): Int {
        return R.layout.fragment_play
    }

    override fun initView() {
        onBackPressed()
        urlVideo = args.url
        playPauseBtn = binding.root.findViewById(R.id.playPauseBtn)
        backBtn = binding.root.findViewById(R.id.backBtn)
        deleteBtn = binding.root.findViewById(R.id.btnDelete)
        shareBtn = binding.root.findViewById(R.id.btnShare)
        volBtn = binding.root.findViewById(R.id.volumeDialog)


        //preparePlayer()
        initializeLayout()
        initializeBinding()

    }


    private fun initializeBinding() {
        playPauseBtn.setOnClickListener {
            if (exoPlayer!!.isPlaying) pauseVideo()
            else playVideo()

        }
        backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        deleteBtn.setOnClickListener {
            val file = File(urlVideo)
            val myFileDialog = MyFileDialog.create(object : MyFileDialog.IListener {
                override fun delete() {
                    mViewModel.deleteFile(file = file)
                    findNavController().previousBackStackEntry?.savedStateHandle?.set("key", true)
                    findNavController().popBackStack()

                }
            })

            myFileDialog.show(parentFragmentManager, "Delete Video Dialog")


        }

        shareBtn.setOnClickListener {
            shareFile(urlVideo)
        }

        volBtn.setOnClickListener {
            val volDialog = VolumeDialog.create(object : VolumeDialog.IListener {

            })
            volDialog.show(childFragmentManager, "Volume Dialog")

        }

    }

    private fun initializeLayout() {

    }

    private fun releasePlayer() {
        exoPlayer?.let { player ->
            playbackPosition = player.currentPosition
            playWhenReady = player.playWhenReady
            player.release()
            exoPlayer = null
        }
    }


    override fun onPause() {
        releasePlayer()
        super.onPause()

        //releasePlayer()

    }

    override fun onResume() {
        preparePlayer()
        super.onResume()
    }

    private fun preparePlayer() {
        try {
            exoPlayer?.release()
        } catch (_: Exception) {
        }
        exoPlayer = ExoPlayer.Builder(requireContext()).build()
        exoPlayer!!.repeatMode = ExoPlayer.REPEAT_MODE_ONE
        exoPlayer?.playWhenReady = true
        binding.playerView.player = exoPlayer
        val file = File(urlVideo)

        val uri = Uri.fromFile(file)
        val mediaItem = MediaItem.fromUri(uri)
//        val mediaSource =
//            HlsMediaSource.Factory(defaultHttpDataSourceFactory).createMediaSource(mediaItem)
        exoPlayer?.apply {
            //setMediaSource(mediaSource)
            setMediaItem(mediaItem)
            seekTo(playbackPosition)
            this@PlayFragment.playWhenReady = playWhenReady
            prepare()
        }
        playVideo()
    }

    private fun playVideo() {

        playPauseBtn.setImageResource(R.drawable.ic_pause_video)
        exoPlayer!!.play()
    }

    private fun pauseVideo() {
        playPauseBtn.setImageResource(R.drawable.ic_play_video)
        exoPlayer!!.pause()
    }

    private fun shareFile(path: String) {
        val internalFile = File(path)
        val contentUri = FileProvider.getUriForFile(
            requireActivity(),
            requireActivity().packageName + ".provider",
            internalFile
        )
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "*/*"
        sharingIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        sharingIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
        startActivity(Intent.createChooser(sharingIntent, "Share"))
    }

    private fun onBackPressed() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()

            }

        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }


}