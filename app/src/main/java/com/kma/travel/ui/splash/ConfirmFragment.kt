package com.kma.travel.ui.splash

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.kma.travel.MainActivity
import com.kma.travel.R
import com.kma.travel.databinding.FragmentConfirmBinding
import com.kma.travel.ui.base.AbsBaseFragment
import com.kma.travel.utils.SharedPreferenceUtils

class ConfirmFragment : AbsBaseFragment<FragmentConfirmBinding>() {
    private lateinit var mText: String
    override fun getLayout(): Int = R.layout.fragment_confirm

    override fun initView() {
        mText = requireContext().resources.getString(R.string.agree_term_of_service_and_policy)

        val spannableString = SpannableString(mText)

        val clickTermsOfService: ClickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                val action = ConfirmFragmentDirections.actionConfirmFragmentToPolicyAndTermFragment().setParam(1)
                findNavController().navigate(action)
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.BLACK
                ds.isUnderlineText = true
                ds.typeface = Typeface.DEFAULT_BOLD
            }
        }

        val clickPrivacyPolicy: ClickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                val action =ConfirmFragmentDirections.actionConfirmFragmentToPolicyAndTermFragment().setParam(2)
                findNavController().navigate(action)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.BLACK
                ds.isUnderlineText = true
                ds.typeface = Typeface.DEFAULT_BOLD

            }
        }
        val clickDisclaimer: ClickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                val action =ConfirmFragmentDirections.actionConfirmFragmentToPolicyAndTermFragment().setParam(3)
                findNavController().navigate(action)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.BLACK
                ds.isUnderlineText = true
                ds.typeface = Typeface.DEFAULT_BOLD

            }
        }

        spannableString.setSpan(clickTermsOfService, 40, 56, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(clickPrivacyPolicy, 58, 72, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(clickDisclaimer, 77, 87, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvServiceAndPolicy.setText(spannableString, TextView.BufferType.SPANNABLE)
        binding.tvServiceAndPolicy.movementMethod = LinkMovementMethod.getInstance()

        binding.btnStart.setOnClickListener {
            SharedPreferenceUtils.getInstance(requireActivity()).putAcceptPolicy(true)
            gotoMainActivity()
        }
        binding.ivClose.setOnClickListener { requireActivity().finish() }

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })
    }

    private fun gotoMainActivity() {
        requireActivity().startActivity(Intent(context, MainActivity::class.java))
        requireActivity().finish()
    }
}