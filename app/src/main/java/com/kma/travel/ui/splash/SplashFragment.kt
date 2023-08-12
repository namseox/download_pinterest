package com.kma.travel.ui.splash

import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.navigation.fragment.findNavController
import com.kma.travel.MainActivity
import com.kma.travel.R
import com.kma.travel.databinding.FragmentSplashBinding
import com.kma.travel.ui.base.AbsBaseFragment
import com.kma.travel.utils.SharedPreferenceUtils

class SplashFragment : AbsBaseFragment<FragmentSplashBinding>() {
    override fun getLayout(): Int = R.layout.fragment_splash
    private val handler = Handler(Looper.myLooper()!!)
    private val runnable = Runnable {
        if (SharedPreferenceUtils.getInstance(requireContext()).getAcceptPolicy()) {
            requireActivity().startActivity(Intent(context, MainActivity::class.java))
            requireActivity().finish()
        } else {
            findNavController().navigate(R.id.action_splashFragment_to_confirmFragment)
        }
    }

    override fun initView() {

    }

    override fun onStart() {
        super.onStart()
        handler.postDelayed(runnable, 2000)
    }
    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }
}