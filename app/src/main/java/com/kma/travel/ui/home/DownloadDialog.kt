package com.kma.travel.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.kma.travel.R
import com.kma.travel.databinding.DownloadDialogBinding
import com.kma.travel.ui.base.dialog.BaseDialog
import com.kma.travel.ui.base.dialog.BaseListener
import com.kma.travel.ui.home.data.DownloadStatus
import com.kma.travel.utils.SharedPreferenceUtils
import com.kma.travel.utils.ToastUtils
import com.kma.travel.utils.Utils.setOnSingClickListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DownloadDialog : BaseDialog<DownloadDialogBinding, DownloadDialog.IListener>() {
    @Inject
    lateinit var sharedPreferenceUtils: SharedPreferenceUtils

    private val mHomeViewModel by viewModels<ViewModelHome>({ requireParentFragment() })


    override fun initViewModel() {

    }

    override fun initView() {
        binding.btnOk.setOnSingClickListener {
            listener?.downloadSuccess()
            dismiss()
        }

        binding.numBerProgressBar.isIndeterminate = false
        observer()
        binding.tvCancle.setOnSingClickListener {
            mHomeViewModel.cancelJobDownloadFile()
            dismiss()
        }
    }

    override fun getLayout(): Int {
        return R.layout.download_dialog
    }

    companion object {
        fun create(listener: IListener): DownloadDialog {
            val dialog = DownloadDialog()
            dialog.listener = listener
            return dialog
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //dialog?.setCanceledOnTouchOutside(false)
        /*dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )*/
        dialog?.window?.setBackgroundDrawableResource(R.drawable.bg_dialog_progress_bar)
        dialog?.window?.setWindowAnimations(R.style.DialogFullscreen)
        dialog?.setOnCancelListener {

            dismiss()
            listener?.listenerCancelDialog()

        }

        /* dialog?.setOnDismissListener {
             listener?.listenerDismissDialog()
         }
 */

    }

    interface IListener : BaseListener {
        fun listenerCancelDialog()
        fun downloadSuccess()
        //fun listenerDismissDialog()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogFullscreen)
    }

    @SuppressLint("SetTextI18n")
    private fun observer() {
        mHomeViewModel.downloadStatus.observe(this) {
            it?.let {
                if (it.downloadState == DownloadStatus.DownloadState.Downloading) {
                    binding.tvDurationDownload.text = "${it.percentage}%"
                    setProgressBar(it.percentage)
                }
                if (it.downloadState == DownloadStatus.DownloadState.Complete) {
                    //dismiss()
                    sharedPreferenceUtils.putIsSuccessFul(true)
                    binding.cardView1.visibility = View.GONE
                    binding.cardView2.visibility = View.VISIBLE
                    ToastUtils.getInstance(requireActivity()).showToast("Successful Download")

                }

                if (it.downloadState == DownloadStatus.DownloadState.Failed) {
                    dismiss()
                    ToastUtils.getInstance(requireActivity()).showToast("Downloaded Fail")
                }

            }
        }


    }

    private fun setProgressBar(duration: Int) {
        binding.numBerProgressBar.setProgressCompat(duration, true)
    }


}