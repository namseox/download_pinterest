package com.kma.travel.ui.home


import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.kma.travel.R
import com.kma.travel.data.api.reponse.DataResponse
import com.kma.travel.data.api.reponse.LoadingStatus
import com.kma.travel.data.model.Video
import com.kma.travel.databinding.FragmentHomeBinding
import com.kma.travel.ui.base.AbsBaseFragment
import com.kma.travel.ui.home.data.DownloadStatus
import com.kma.travel.ui.main.MainFragment
import com.kma.travel.utils.SharedPreferenceUtils
import com.kma.travel.utils.Utils.convertDuration
import com.kma.travel.utils.Utils.setOnSingClickListener
import com.kma.travel.utils.Utils.validUrl
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : AbsBaseFragment<FragmentHomeBinding>() {
    @Inject
    lateinit var sharedPreferenceUtils: SharedPreferenceUtils
    private val mViewModel: ViewModelHome by viewModels()
    override fun getLayout(): Int = R.layout.fragment_home

    private lateinit var videos: Video

    override fun initView() {

        mViewModel.checkLinkLiveData.observe(viewLifecycleOwner) {
            it?.let {
                if (it.loadingStatus == LoadingStatus.Success) {
                    val video = (it as DataResponse.DataSuccess).body
                    if (video != null) {
                        videos = video
                        val duration = convertDuration(video.duration)
                        binding.tvDuration.text = duration
                        Glide.with(requireContext())
                            .load(video.thumbnailUrl)
                            .into(binding.imvImage)
                        binding.textInputLayout.error = null
                        binding.tvDuration.visibility = View.VISIBLE
                        binding.btnDownload.visibility = View.VISIBLE
                        binding.btnGet.visibility = View.GONE
                        binding.btnGetCheck.visibility = View.GONE
                        binding.textInputLayout.isEnabled = false
                        binding.textInputLayout.setEndIconTintList(ContextCompat.getColorStateList(requireContext(), R.color.disable_text_input))
                        binding.edtLink.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.disable_text_input))

                    }


                }

                if (it.loadingStatus == LoadingStatus.Error) {
                    binding.textInputLayout.error = "Invalid link"
                    binding.btnGet.visibility = View.VISIBLE
                    binding.btnGetCheck.visibility = View.GONE
                }
            }

        }

        mViewModel.downloadStatus.observe(this) {
            it?.let {

                if (it.downloadState == DownloadStatus.DownloadState.Complete) {
                    binding.textInputLayout.isEnabled = true
                    binding.textInputLayout.setEndIconTintList(ContextCompat.getColorStateList(requireContext(), R.color.enable_text_input))
                    binding.edtLink.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.enable_text_input))
                }

                if (it.downloadState == DownloadStatus.DownloadState.Failed) {
                    binding.textInputLayout.isEnabled = true
                    binding.textInputLayout.setEndIconTintList(ContextCompat.getColorStateList(requireContext(), R.color.enable_text_input))
                    binding.edtLink.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.enable_text_input))
                }

            }
        }



        binding.btnRefresh.setOnClickListener {
            binding.edtLink.text = null
            binding.textInputLayout.isEnabled = true
            binding.textInputLayout.setEndIconTintList(ContextCompat.getColorStateList(requireContext(), R.color.enable_text_input))
            binding.edtLink.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.enable_text_input))
        }

        binding.textInputLayout.setEndIconOnClickListener {
            pasteText()
        }


        binding.edtLink.doOnTextChanged { text, _, _, _ ->
            Glide.with(requireContext())
                .load(R.drawable.bg_thumbnail_img)
                .into(binding.imvImage)
            binding.tvDuration.visibility = View.GONE
            binding.btnDownload.visibility = View.GONE
            binding.btnGet.visibility = View.GONE
            binding.btnGetCheck.visibility = View.VISIBLE

            if (text!!.isEmpty()) {
                binding.textInputLayout.error = null
                binding.btnGet.visibility = View.VISIBLE
                binding.btnGetCheck.visibility = View.GONE
            } else {
                val validUrl = validUrl(text.toString())
                if (!validUrl) {
                    binding.textInputLayout.error = "Invalid link"
                    binding.btnGet.visibility = View.VISIBLE
                    binding.btnGetCheck.visibility = View.GONE
                } else {
                    binding.textInputLayout.error = null
                    binding.btnGet.visibility = View.GONE
                    binding.btnGetCheck.visibility = View.VISIBLE
                }
            }

        }


        binding.btnGetCheck.setOnSingClickListener {
            if (mViewModel.checkForInternet(requireContext())) {
                showCheckLinkDialog()
                mViewModel.checkStory.observe(viewLifecycleOwner) {
                    if (!it ) {
                        mViewModel.checkStory.value = true
                    }
                }
            } else {
                Toast.makeText(requireContext(), "No internet", Toast.LENGTH_LONG).show()
            }

        }
        binding.btnDownload.setOnSingClickListener {

            if (mViewModel.checkForInternet(requireContext())) {
                showDownloadDialog()
                mViewModel.downloadFiles(requireContext(), videos.name, videos.contentUrl)
            } else {
                Toast.makeText(requireContext(), "No internet", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun showCheckLinkDialog() {
        val url = binding.edtLink.text.toString()
        val dialog = ProgressBarDialog.create(url, object : ProgressBarDialog.IListener {
            override fun listenerCancelDialog() {
                mViewModel.cancelJobCheckLink()
            }


        })
        dialog.show(childFragmentManager, "Check Link Dialog")

    }

    private fun showDownloadDialog() {
        sharedPreferenceUtils.putIsSuccessFul(false)
        val dialog = DownloadDialog.create(object : DownloadDialog.IListener {
            override fun listenerCancelDialog() {
                val isDownLoadSuccessFul = sharedPreferenceUtils.getIsSuccessFul()
                if (isDownLoadSuccessFul) {
                    (requireParentFragment().childFragmentManager.primaryNavigationFragment as MainFragment).binding.viepager.currentItem =
                        1
                    Glide.with(requireContext())
                        .load(R.drawable.bg_thumbnail_img)
                        .into(binding.imvImage)
                    binding.tvDuration.visibility = View.GONE
                    binding.btnDownload.visibility = View.GONE
                    binding.btnGet.visibility = View.GONE
                    binding.btnGetCheck.visibility = View.GONE
                    binding.textInputLayout.error = null
                    binding.edtLink.text = null
                } else {
                    mViewModel.cancelJobDownloadFile()
                }
            }

            override fun downloadSuccess() {
                (requireParentFragment().childFragmentManager.primaryNavigationFragment as MainFragment).binding.viepager.currentItem =
                    1
                Glide.with(requireContext())
                    .load(R.drawable.bg_thumbnail_img)
                    .into(binding.imvImage)
                binding.tvDuration.visibility = View.GONE
                binding.btnDownload.visibility = View.GONE
                binding.btnGet.visibility = View.GONE
                binding.btnGetCheck.visibility = View.GONE
                binding.textInputLayout.error = null
                binding.edtLink.text = null
            }

        })
        dialog.show(childFragmentManager, "Download Dialog")

    }


    private fun pasteText() {
        val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        var pasteData = ""

        // If it does contain data, decide if you can handle the data.
        if (!clipboard!!.hasPrimaryClip()) {
            //Log.d("asdfasdfafsda", "clip1 = $pasteData")
        } else if (!clipboard.primaryClipDescription!!.hasMimeType(
                MIMETYPE_TEXT_PLAIN
            )
        ) {

            // since the clipboard has data but it is not plain text
            //Log.d("asdfasdfafsda", "clip2 = $pasteData")
            val item = clipboard.primaryClip!!.getItemAt(0)
            pasteData = item.text.toString()
            binding.edtLink.setText(pasteData)
        } else {

            //since the clipboard contains plain text.
            val item = clipboard.primaryClip!!.getItemAt(0)

            // Gets the clipboard as text.
            pasteData = item.text.toString()
            binding.edtLink.setText(pasteData)
            //Log.d("asdfasdfafsda", "clip3 = $pasteData")

        }
    }


}