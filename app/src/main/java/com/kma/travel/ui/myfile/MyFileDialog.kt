package com.kma.travel.ui.myfile

import android.os.Bundle
import com.kma.travel.R
import com.kma.travel.databinding.MyFileDialogBinding
import com.kma.travel.ui.base.dialog.BaseDialog
import com.kma.travel.ui.base.dialog.BaseListener
import com.kma.travel.ui.home.DownloadDialog
import java.io.File

class MyFileDialog(): BaseDialog<MyFileDialogBinding, MyFileDialog.IListener>() {

    interface IListener : BaseListener {
        fun delete()
    }
    override fun initViewModel() {
        setStyle(STYLE_NORMAL, R.style.Theme_App_Dialog_FullScreen)
    }

    override fun initView() {
        binding.tvDelete.setOnClickListener(){
            listener?.delete()
            dismiss()
        }
        binding.tvCancel.setOnClickListener(){
            dismiss()
        }
    }

    override fun getLayout(): Int = R.layout.my_file_dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    companion object {
        fun create(listener: MyFileDialog.IListener): MyFileDialog {
            val dialog = MyFileDialog()
            dialog.listener = listener
            return dialog
        }
    }
}