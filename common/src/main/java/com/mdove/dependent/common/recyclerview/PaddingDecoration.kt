package com.mdove.dependent.common.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mdove.dependent.common.utils.UIUtils

/**
 * Created by MDove on 2019-11-03.
 */
class PaddingDecoration(padding: Int, private val type: PaddingType = PaddingType.TOP) :
    RecyclerView.ItemDecoration() {
    private var mPadding = UIUtils.dip2Px(8).toInt()

    init {
        if (padding != 8) {
            mPadding = UIUtils.dip2Px(padding).toInt()
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition == RecyclerView.NO_POSITION) {
            return
        }
        if (itemPosition == 0) {
            when (type) {
                PaddingType.TOP -> {
                    outRect.top = mPadding
                }
                PaddingType.RIGHT -> {
                    outRect.right = mPadding
                }
                else -> {
                }
            }
        }

        if (itemPosition == (parent.adapter?.itemCount ?: 0) - 1) {
            when (type) {
                PaddingType.LEFT -> {
                    outRect.left = mPadding
                }
                PaddingType.BOTTOM -> {
                    outRect.bottom = mPadding
                }
                else -> {
                }
            }
        }

//        val adapter = parent.adapter
//        if (adapter != null && itemPosition == adapter.itemCount - 1) {
//            outRect.bottom = mPadding
//        }
    }
}

enum class PaddingType {
    TOP,
    BOTTOM,
    RIGHT,
    LEFT
}
