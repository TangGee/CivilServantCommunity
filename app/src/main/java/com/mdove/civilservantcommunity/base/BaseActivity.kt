package com.mdove.civilservantcommunity.base

import android.os.Bundle
import android.view.View
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.launcher.BaseLauncherActivity
import com.mdove.dependent.common.threadpool.FastMainScope
import com.mdove.civilservantcommunity.view.utils.StatusBarUtil
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext
import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR


/**
 * Created by MDove on 2019-09-02.
 */
open class BaseActivity : BaseLauncherActivity(), CoroutineScope {

    companion object {
        const val TYPE_ANIM_NORMAL = 0
        const val TYPE_ANIM_NONE = 1
        const val TYPE_ANIM_DEFAULT = 2
        const val TYPE_ANIM_UP_DOWN = 3

        const val BUNDLE_WITH_TRANSITION = "with_transition"

        val SLIDE_IN_LEFT_NORMAL = R.anim.mdove_slide_in_left
        val SLIDE_OUT_RIGHT_NORMAL = R.anim.mdove_slide_out_right
        val SLIDE_IN_RIGHT_NORAML = R.anim.mdove_slide_in_right
        val SLIDE_OUT_LEFT_NORMAL = R.anim.mdove_slide_out_left

        val SLIDE_IN_UP_NORMAL = R.anim.mdove_slide_in_bottom
        val SLIDE_OUT_DOWN_NORMAL = R.anim.mdove_slide_out_bottom

        val SLIDE_IN_LEFT_NONE = R.anim.none
        val SLIDE_OUT_RIGHT_NONE = R.anim.none
        val SLIDE_IN_RIGHT_NONE = R.anim.none
        val SLIDE_OUT_LEFT_NONE = R.anim.none

        val STAY = R.anim.stay
    }

    open var animType = TYPE_ANIM_NORMAL

    override val coroutineContext: CoroutineContext
        get() = FastMainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (enableTranslucent()) {
            StatusBarUtil.setTranslucent(this)
        }
        startAnim(animType)
    }

    fun setStatusBarTextColorIsBlack(isBlack: Boolean = false) {
        if (isBlack) {
            // 设置状态栏黑色
            window.decorView.systemUiVisibility =
                window.decorView.systemUiVisibility or SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            // 清除状态栏黑色字体颜色
            window.decorView.systemUiVisibility =
                window.decorView.systemUiVisibility xor SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    open fun enableTranslucent(): Boolean {
        return true
    }

    override fun finish() {
        super.finish()
        finishAnim(animType)
    }

    private fun startAnim(type: Int) {
        when (type) {
            TYPE_ANIM_DEFAULT -> {
                overridePendingTransition(SLIDE_IN_RIGHT_NORAML, SLIDE_OUT_LEFT_NORMAL)
            }
            TYPE_ANIM_NONE -> {
                overridePendingTransition(SLIDE_IN_LEFT_NONE, SLIDE_OUT_RIGHT_NONE)
            }
            TYPE_ANIM_NORMAL -> {
                overridePendingTransition(SLIDE_IN_RIGHT_NORAML, STAY)
            }
            TYPE_ANIM_UP_DOWN -> {
                overridePendingTransition(SLIDE_IN_UP_NORMAL, STAY)
            }
        }
    }

    private fun finishAnim(type: Int) {
        when (type) {
            TYPE_ANIM_DEFAULT -> {
                overridePendingTransition(SLIDE_IN_LEFT_NORMAL, SLIDE_OUT_RIGHT_NORMAL)
            }
            TYPE_ANIM_NONE -> {
                overridePendingTransition(SLIDE_IN_LEFT_NONE, SLIDE_OUT_RIGHT_NONE)
            }
            TYPE_ANIM_NORMAL -> {
                overridePendingTransition(SLIDE_IN_LEFT_NONE, SLIDE_OUT_RIGHT_NORMAL)
            }
            TYPE_ANIM_UP_DOWN -> {
                overridePendingTransition(SLIDE_IN_LEFT_NONE, SLIDE_OUT_DOWN_NORMAL)
            }
        }
    }
}