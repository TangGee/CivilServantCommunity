package com.mdove.dependent.common.view

import android.view.View
import android.view.ViewConfiguration

/**
 * Created by MDove on 2019-09-15.
 */
abstract class DebounceOnClickListener : View.OnClickListener {
    private var delay = DEFAULT_DELAY

    constructor(delay: Long) {
        this.delay = delay
    }

    override fun onClick(v: View) {
        if (enabled) {
            enabled = false
            v.postDelayed(ENABLE_AGAIN, delay)
            this.doClick(v)
        }

    }

    fun setDelay(delay: Long) {
        this.delay = delay
    }

    abstract fun doClick(var1: View?)

    companion object {
        val DEFAULT_DELAY = ViewConfiguration.getDoubleTapTimeout().toLong()
        private var enabled = true
        private val ENABLE_AGAIN = { DebounceOnClickListener.enabled = true }
    }
}