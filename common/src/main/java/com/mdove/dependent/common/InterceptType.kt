package com.mdove.dependent.common

import androidx.annotation.IntDef
import com.mdove.dependent.common.InterceptType.Companion.DEFAULT
import com.mdove.dependent.common.InterceptType.Companion.INTERCEPT_TOUCHEVENT
import com.mdove.dependent.common.InterceptType.Companion.NO_INTERCEPT_TOUCHEVENT
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Suppress("DEPRECATED_JAVA_ANNOTATION")
@IntDef(DEFAULT, INTERCEPT_TOUCHEVENT, NO_INTERCEPT_TOUCHEVENT)
@Retention(RetentionPolicy.SOURCE)
annotation class InterceptType {
    companion object {
        /**
         * 默认
         */
        const val DEFAULT = 0
        /**
         * 拦截事件
         */
        const val INTERCEPT_TOUCHEVENT = 1

        /**
         * 不拦截事件
         */
        const val NO_INTERCEPT_TOUCHEVENT = 2
    }
}