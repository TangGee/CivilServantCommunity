package com.mdove.dependent.common.view

import android.app.Activity
import android.content.Context
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

    init {
        View.inflate(context, R.layout.view_normal_toolbar, this)
        btn_back.setOnClickListener {
            (context as? Activity)?.let {
                it.finish()
            }
        }
        setBackgroundResource(R.drawable.bg_normal_toolbar)
    }

    fun setTitle(title: String) {
        tv_title.text = title
    }
}