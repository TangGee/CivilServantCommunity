package com.mdove.civilservantcommunity.timelinetest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.view.timelineitemdecoration.util.Util

/**
 * *          _       _
 * *   __   _(_)_   _(_) __ _ _ __
 * *   \ \ / / \ \ / / |/ _` | '_ \
 * *    \ V /| |\ V /| | (_| | | | |
 * *     \_/ |_| \_/ |_|\__,_|_| |_|
 *
 *
 * Created by vivian on 2017/6/9.
 */

class DotTimeLineAdapter : RecyclerView.Adapter<DotTimeLineAdapter.ViewHolder> {
    internal var mContext: Context
    internal var mList: List<Event>
    internal var colors =
        intArrayOf(-0x5294, -0x9d0bcc, -0x212588, -0x812301, -0xa70216, -0x238a1)//颜色组

    fun setList(list: List<Event>) {
        mList = list
    }

    constructor(context: Context) {
        mContext = context
    }

    constructor(context: Context, list: List<Event>) {
        mContext = context
        mList = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.pop_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.time.text = Util.LongtoStringFormat(1000 * mList[position].time)
        holder.textView.text = mList[position].event
        holder.time.setTextColor(colors[position % colors.size])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    internal inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var time: TextView
        var textView: TextView

        init {
            time = view.findViewById<View>(R.id.time) as TextView
            textView = view.findViewById<View>(R.id.text) as TextView
        }
    }

}
