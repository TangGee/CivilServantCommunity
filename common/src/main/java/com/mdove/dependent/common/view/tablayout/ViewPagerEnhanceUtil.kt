package com.mdove.dependent.common.view.tablayout

import android.animation.ArgbEvaluator
import androidx.viewpager.widget.ViewPager

fun ArgbEvaluator.calculateGradualColor(positionOffset: Float, fromColor: Int, toColor: Int): Int {
    return evaluate(positionOffset, fromColor, toColor) as Int
}

fun ViewPager.scrollNextPosition(position: Int): Int {
    var currentPosition = currentItem
    return if (currentPosition == position) {//按下向左拖动
        currentPosition + 1
    } else {//按下向右拖动
        position
    }
}

fun SlidingTabLayout.gradualColor(currentPosition: Int, nextPosition: Int, currentPositionColor: Int, nextPositionColor: Int) {
    var current = currentPositionColor
    var next = nextPositionColor
    if (currentPosition < nextPosition) {////按下向左拖动
    } else {//按下向右拖动，根据onPageScrolled回调适配
        current = nextPositionColor
        next = currentPositionColor
    }
    getTitleView(currentPosition).setTextColor(current)
    getTitleView(nextPosition).setTextColor(next)
}