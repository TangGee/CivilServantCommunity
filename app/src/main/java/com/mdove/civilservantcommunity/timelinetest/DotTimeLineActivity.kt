package com.mdove.civilservantcommunity.timelinetest

import android.graphics.Color
import android.os.Bundle
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

import com.mdove.civilservantcommunity.R
import com.mdove.dependent.common.recyclerview.timelineitemdecoration.itemdecoration.DotItemDecoration

import java.util.ArrayList

class DotTimeLineActivity : AppCompatActivity() {
    lateinit var mRecyclerView: RecyclerView

    var mList: MutableList<Event> = ArrayList()
    lateinit var mAdapter: DotTimeLineAdapter
    lateinit var mItemDecoration: DotItemDecoration

    internal var times =
            longArrayOf(1497229200, 1497240000, 1497243600, 1497247200, 1497249000, 1497252600)
    var events = arrayOf("去小北门拿快递", "跟同事一起聚餐", "写文档", "和产品开会", "整理开会内容", "提交代码到git上")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dot_timeline_layout)

        mRecyclerView = findViewById<View>(R.id.recycler_view) as RecyclerView
//        mRecyclerView.layoutManager =
//            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mItemDecoration = DotItemDecoration.Builder(this)
                .setOrientation(DotItemDecoration.VERTICAL)//if you want a horizontal item decoration,remember to set horizontal orientation to your LayoutManager
                .setItemStyle(DotItemDecoration.STYLE_DRAW)
                .setTopDistance(20f)//dp
                .setItemInterVal(10f)//dp
                .setItemPaddingLeft(30f)//default value equals to item interval value
                .setItemPaddingRight(20f)//default value equals to item interval value
                .setDotColor(Color.WHITE)
                .setDotRadius(5)//dp
                .setDotPaddingTop(0)
                .setDotInItemOrientationCenter(false)//set true if you want the dot align center
                .setLineColor(Color.BLACK)
                .setLineWidth(3f)//dp
                .setOnlyLeftMarginLeft(40F)
                .setEndText("END")
                .setOnlyLeft(true)
                .setTextColor(Color.WHITE)
                .setTextSize(16f)//sp
                .setDotPaddingText(2f)//dp.The distance between the last dot and the end text
                .setBottomDistance(40f)//you can add a distance to make bottom line longer
                .create()
//        mItemDecoration.setSpanIndexListener { view, spanIndex ->
//            Log.i("Info", "view:$view  span:$spanIndex")
//            view.(if (spanIndex == 0) R.drawable.bg_hot_article_top1_stub_ else R.drawable.bg_hot_article_top2_stub_)
//        }
        mRecyclerView.addItemDecoration(mItemDecoration)

        for (i in times.indices) {
            val event = Event()
            event.time = times[i]
            event.event = events[i]
            mList.add(event)
        }

        mAdapter = DotTimeLineAdapter(this, mList)
        mRecyclerView.adapter = mAdapter
    }
}
