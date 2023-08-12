package com.kma.travel.ui.playvideo

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.kma.travel.R
import com.kma.travel.databinding.VolumeDialogItemBinding
import com.kma.travel.ui.base.dialog.BaseDialog
import com.kma.travel.ui.base.dialog.BaseListener
import com.kma.travel.utils.WindowUtils
import kotlin.math.ceil


class VolumeDialog : BaseDialog<VolumeDialogItemBinding, VolumeDialog.IListener>() {


    interface IListener : BaseListener {
        //fun delete()
    }

    override fun initViewModel() {

    }

    override fun initView() {
        binding.volClose.setOnClickListener {
            dismiss()
        }
        setVolume()
        val audioManager = this.context?.getSystemService(Context.AUDIO_SERVICE) as AudioManager?
        binding.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                audioManager!!.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
                val mediaVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                val maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

                val volPer = ceil(((mediaVolume.toDouble() / maxVol.toDouble()) * 100))
                binding.voLNumber.text =
                    volPer.toString().substring(0, volPer.toString().length - 2)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    override fun getLayout(): Int = R.layout.volume_dialog_item

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setLayout(
            (WindowUtils.getScreenWidth() * 0.86).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawableResource(R.drawable.bg_dialog_progress_bar)
    }

    companion object {
        fun create(listener: VolumeDialog.IListener): VolumeDialog {
            val dialog = VolumeDialog()
            dialog.listener = listener
            return dialog
        }
    }

    private fun setVolume() {
        val audioManager = this.context?.getSystemService(Context.AUDIO_SERVICE) as AudioManager?
        binding.seekBar.max = audioManager!!.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        binding.seekBar.progress = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

        val mediaVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

        val volPer = ceil(((mediaVolume.toDouble() / maxVol.toDouble()) * 100))
        binding.voLNumber.text = volPer.toString().substring(0, volPer.toString().length - 2)
    }


}