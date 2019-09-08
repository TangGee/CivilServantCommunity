package com.mdove.dependent.common.view.tablayout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.mdove.dependent.common.R

class MainFeedTabView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.layout_tab, this)
    }
}