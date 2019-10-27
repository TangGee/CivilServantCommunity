package com.mdove.civilservantcommunity.feed.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.plan.PlanToFeedParams

/**
 * Created by MDove on 2019-10-27.
 */
class FeedPlanTodayItemsAdapter constructor(
    private val mContext: Context,
    private val mParams: PlanToFeedParams
) :
    RecyclerView.Adapter<FeedPlanTodayItemsAdapter.ViewHolder>() {
    var colors =
        intArrayOf(-0x5294, -0x9d0bcc, -0x212588, -0x812301, -0xa70216, -0x238a1)//颜色组

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_feed_today_plan_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = mParams.data[position].planTitle
    }

    override fun getItemCount(): Int {
        return mParams.data.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textView: TextView

        init {
            textView = view.findViewById<View>(R.id.text) as TextView
        }
    }

}
