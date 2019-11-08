package com.mdove.dependent.common.view

import android.view.View
import android.view.ViewGroup

/**
 * Created by MDove on 2019-11-07.
 */

fun View.removeSelf(){
    (this.parent as? ViewGroup)?.let { parent ->
        parent.removeView(this)
    }
}