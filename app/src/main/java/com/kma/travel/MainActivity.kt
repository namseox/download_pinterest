package com.kma.travel


import androidx.activity.ComponentActivity
import com.kma.travel.databinding.ActivityMainBinding
import com.kma.travel.ui.base.AbsBaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AbsBaseActivity<ActivityMainBinding>() {
    override fun getFragmentID(): Int {
        return R.id.navContainerViewMain
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }


}