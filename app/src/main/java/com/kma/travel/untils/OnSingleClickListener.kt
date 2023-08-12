package com.kma.travel.utils

import android.os.SystemClock
import android.view.View

abstract class OnSingleClickListener : View.OnClickListener {
    companion object {
        const val MIN_CLICK_INTERVAL = 2000L
    }

    private var mLastClickTime = 0L

    abstract fun onSingleClick(view: View)

    override fun onClick(v: View?) {
        val currentClickTime = SystemClock.elapsedRealtime()
        if (currentClickTime - mLastClickTime <= MIN_CLICK_INTERVAL) return
        mLastClickTime = currentClickTime
        onSingleClick(v!!)
    }
}