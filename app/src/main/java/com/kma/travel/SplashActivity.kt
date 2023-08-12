package com.kma.travel

import com.kma.travel.databinding.ActivitySplashBinding
import com.kma.travel.ui.base.AbsBaseActivity

class SplashActivity : AbsBaseActivity<ActivitySplashBinding>() {
    override fun getFragmentID(): Int {
        return R.id.splashNavContainerView
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }
}