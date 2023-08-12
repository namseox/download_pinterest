package com.kma.travel.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.kma.travel.R
import com.kma.travel.data.api.reponse.LoadingStatus
import com.kma.travel.databinding.ProgressBarDialogBinding
import com.kma.travel.ui.base.dialog.BaseDialog
import com.kma.travel.ui.base.dialog.BaseListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProgressBarDialog : BaseDialog<ProgressBarDialogBinding, ProgressBarDialog.IListener>() {
    private var url: String = ""

    private val mHomeViewModel by viewModels<ViewModelHome>({ requireParentFragment() })

    override fun initViewModel() {

    }

    override fun initView() {
        //binding.viewModel = mHomeViewModel

    }

    override fun getLayout(): Int {
        return R.layout.progress_bar_dialog
    }

    companion object {
        const val KEY_PROGRESS_BAR_DIALOG = "Key_Progress_Bar_Dialog"
        fun create(url: String, listener: IListener): ProgressBarDialog {
            val dialog = ProgressBarDialog()
            val bundle = Bundle()
            bundle.putString(KEY_PROGRESS_BAR_DIALOG, url)
            dialog.listener = listener
            dialog.arguments = bundle
            return dialog
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
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
        //fun listenerDismissDialog()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogFullscreen)
        url = requireArguments().getString(KEY_PROGRESS_BAR_DIALOG)!!
        mHomeViewModel.checkLink(url)

    }

    private fun observer() {
        mHomeViewModel.checkLinkLiveData.observe(this) {
            it?.let {
                if (it.loadingStatus == LoadingStatus.Success) {
                    dismiss()
                }

                if (it.loadingStatus == LoadingStatus.Error) {
                    dismiss()
                }

            }
        }


    }


}