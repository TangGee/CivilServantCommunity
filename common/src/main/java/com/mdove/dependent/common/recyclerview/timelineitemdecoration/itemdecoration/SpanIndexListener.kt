package com.mdove.dependent.common.recyclerview.timelineitemdecoration.itemdecoration

import android.view.View

/**
 * *          _       _
 * *   __   _(_)_   _(_) __ _ _ __
 * *   \ \ / / \ \ / / |/ _` | '_ \
 * *    \ V /| |\ V /| | (_| | | | |
 * *     \_/ |_| \_/ |_|\__,_|_| |_|
 *
 *
 * Created by vivian on 2017/6/23.
 *
 * copy from https://github.com/vivian8725118/TimeLine
 */

interface SpanIndexListener {
    fun onSpanIndexChange(view: View, spanIndex: Int)
}
