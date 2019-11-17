package com.mdove.dependent.common.view

import android.app.Activity
import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.mdove.dependent.common.R
import kotlinx.android.synthetic.main.view_normal_toolbar.view.*

/**
 * Created by MDove on 2019-09-16.
 */
class NormalToolbar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    ConstraintLayout(context, attrs) {
    private var listener: OnToolbarListener? = null

    init {
        View.inflate(context, R.layout.view_normal_toolbar, this)
        btn_back.setOnClickListener {
            (context as? Activity)?.let {
                it.finish()
            }
        }
        setBackgroundResource(R.drawable.bg_normal_toolbar)
        btn_right.setOnClickListener {
            listener?.onRightBtnClick()
        }
    }

    fun setColorForAll(colorRes: Int) {
        tv_title.setTextColor(colorRes)
        btn_right.setTextColor(colorRes)
        btn_back.setColorFilter(colorRes, PorterDuff.Mode.SRC_IN)
    }

    fun setTitle(title: String) {
        tv_title.text = title
    }

    fun setRightBtnTitle(title: String) {
        btn_right.text = title
    }

    fun setListener(listener: OnToolbarListener) {
        this.listener = listener
    }

    fun setToolbarBackground(res: Int) {
        setBackgroundResource(res)
    }

    fun setToolbarBackgroundIsNull() {
        background = null
    }
}

interface OnToolbarListener {
    fun onRightBtnClick()
}