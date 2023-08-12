package com.kma.travel.ui.splash

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kma.travel.R
import com.kma.travel.databinding.FragmentPolicyAndTermBinding
import com.kma.travel.ui.base.AbsBaseFragment
import kotlinx.coroutines.CoroutineScope


class PolicyAndTermFragment : AbsBaseFragment<FragmentPolicyAndTermBinding>() {
    private val args: PolicyAndTermFragmentArgs by navArgs()
    override fun getLayout(): Int = R.layout.fragment_policy_and_term

    override fun initView() {
        binding.wvPolicy.setBackgroundColor(Color.WHITE)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        var webView = binding.wvPolicy
        val settings: WebSettings = webView.getSettings()

        settings.javaScriptEnabled = true
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY)

        webView.getSettings().setUseWideViewPort(true)
        webView.getSettings().setLoadWithOverviewMode(true)

//        var progressBar = ProgressBar(requireActivity())
//        progressBar.setVisibility(View.VISIBLE);
        var progressDialog = ProgressDialog(requireContext());
        progressDialog.setMessage("Loading...");
        webView.settings.builtInZoomControls = false
        webView.settings.displayZoomControls = false

        webView.setWebViewClient(object : WebViewClient() {
//            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
//
//                view.loadUrl(url)
//
//                return true
//            }

            override fun onPageFinished(view: WebView, url: String) {
//                progressBar.setVisibility(View.GONE);
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            override fun onReceivedError(
                view: WebView, errorCode: Int, description: String, failingUrl: String
            ) {
                Toast.makeText(requireActivity(), "Error:$description", Toast.LENGTH_SHORT).show()
            }
        })
        Handler(Looper.getMainLooper()).postDelayed({
            when (args.param) {
                1 -> {
                    webView.loadUrl("file:///android_asset/term_of_service.html")
                    binding.toolbar.title = "Term Of Service"
                }
                2 -> {
                    webView.loadUrl("file:///android_asset/policy.html")
                    binding.toolbar.title = "Private Policy"

                }
                3 -> {
                    webView.loadUrl("file:///android_asset/disclaimer.html")
                    binding.toolbar.title = "Disclaimer"

                }

                4 -> {
                    webView.loadUrl("file:///android_asset/howtouser.html")
                    binding.toolbar.title = "Download Instruction"

                }

            }
        },1000L)
        progressDialog.show()

    }
}
