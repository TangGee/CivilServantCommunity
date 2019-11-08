package com.mdove.civilservantcommunity.plan.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.customview.widget.ViewDragHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.feed.bean.FeedTimeLineFeedTodayPlansResp
import com.mdove.civilservantcommunity.plan.SinglePlanBean
import com.mdove.civilservantcommunity.plan.adapter.TimeScheduleAdapter
import com.mdove.civilservantcommunity.plan.model.TimeSchedulePlansParams
import com.mdove.civilservantcommunity.plan.model.TimeSchedulePlansStatus
import com.mdove.dependent.common.utils.UIUtils
import kotlinx.android.synthetic.main.layout_time_schedule.view.*

/**
 * Created by MDove on 2019-11-07.
 */
class TimeScheduleLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    ConstraintLayout(context, attrs) {
    private val timeViewInnerScopes = mutableListOf<LinearLayout>()
    private val timeViewOuterScopes = mutableListOf<ScrollView>()
    private val timeRectWidth = UIUtils.getScreenWidth(context) / 6
    private var timeRectHeight = timeRectWidth
    private var listener: OnTimeScheduleLayoutListener? = null
    // key是View的toString()
    private val curTouchMap = mutableMapOf<View, SinglePlanBean>()

    private val mViewDragHelper = ViewDragHelper.create(this, object : ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return child == fake_title_view
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            super.onViewReleased(releasedChild, xvel, yvel)
            (hitTimeInnerScope(releasedChild.left, releasedChild.top) as? LinearLayout)?.let {
                it.addView(createTextView(releasedChild).let { addView ->
                    releasedChild.visibility = View.GONE
                    curTouchMap[releasedChild]?.let {
                        listener?.onPlansHasAdded(it)
                    }
                    addView
                })
            } ?: also {
                fake_title_view.visibility = View.GONE
                curTouchMap[releasedChild]?.let {
                    listener?.onTouchViewStatusChange(it, TimeSchedulePlansStatus.SHOW)
                }
            }
        }

        override fun onViewPositionChanged(
            changedView: View,
            left: Int,
            top: Int,
            dx: Int,
            dy: Int
        ) {
            super.onViewPositionChanged(changedView, left, top, dx, dy)
            hitTimeOuterScope(left, top)?.let {
                val animatorX = ObjectAnimator.ofFloat(it, "scaleX", 1f, 0.8f, 1f)
                val animatorY = ObjectAnimator.ofFloat(it, "scaleY", 1f, 0.8f, 1f)
                AnimatorSet().apply {
                    duration = 500
                    playTogether(animatorX, animatorY)
                    start()
                }
            }
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            return top
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            return left
        }

        override fun getViewHorizontalDragRange(child: View): Int {
            return measuredWidth - child.measuredWidth
        }

        override fun getViewVerticalDragRange(child: View): Int {
            return measuredHeight - child.measuredHeight
        }
    })

    init {
        View.inflate(context, R.layout.layout_time_schedule, this)
        initScopes()
        rlv_plans.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rlv_plans.adapter = TimeScheduleAdapter(object : OnTimeScheduleAdapterListener {
            override fun onLongClick(data: SinglePlanBean, view: View) {
                fake_title_view.layoutParams =
                    fake_title_view.layoutParams.apply {
                        this.width = view.width
                        this.height = view.height
                        (this as? ConstraintLayout.LayoutParams)?.let {
                            it.topMargin = rlv_plans.top
                            it.leftMargin = view.left
                        }
                    }
                curTouchMap[fake_title_view] = data
                fake_title_view.text = data.content
                fake_title_view.visibility = View.VISIBLE
                curTouchMap[fake_title_view]?.let {
                    listener?.onTouchViewStatusChange(it, TimeSchedulePlansStatus.GONE)
                }
            }
        })
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        timeRectHeight = layout_time_1.height
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return mViewDragHelper.shouldInterceptTouchEvent(event) || super.onInterceptTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mViewDragHelper.processTouchEvent(event)
        return true
    }

    private fun hitTimeInnerScope(left: Int, top: Int): View? {
        val row = left / timeRectWidth
        val column = top / timeRectHeight
        var index = column * 4 + row
        if (index >= 24) {
            index = 23
        }
        return if (index >= 0) {
            timeViewInnerScopes[index]
        } else {
            null
        }
    }

    private fun hitTimeOuterScope(left: Int, top: Int): ScrollView? {
        val row = left / timeRectWidth
        val column = top / timeRectHeight
        val index = column * 4 + row
        return if (index in 0..23) {
            timeViewOuterScopes[index]
        } else {
            null
        }
    }

    private fun initScopes() {
        timeViewInnerScopes.add(hour_0)
        timeViewInnerScopes.add(hour_1)
        timeViewInnerScopes.add(hour_2)
        timeViewInnerScopes.add(hour_3)
        timeViewInnerScopes.add(hour_4)
        timeViewInnerScopes.add(hour_5)
        timeViewInnerScopes.add(hour_6)
        timeViewInnerScopes.add(hour_7)
        timeViewInnerScopes.add(hour_8)
        timeViewInnerScopes.add(hour_9)
        timeViewInnerScopes.add(hour_10)
        timeViewInnerScopes.add(hour_11)
        timeViewInnerScopes.add(hour_12)
        timeViewInnerScopes.add(hour_13)
        timeViewInnerScopes.add(hour_14)
        timeViewInnerScopes.add(hour_15)
        timeViewInnerScopes.add(hour_16)
        timeViewInnerScopes.add(hour_17)
        timeViewInnerScopes.add(hour_18)
        timeViewInnerScopes.add(hour_19)
        timeViewInnerScopes.add(hour_20)
        timeViewInnerScopes.add(hour_21)
        timeViewInnerScopes.add(hour_22)
        timeViewInnerScopes.add(hour_23)

        timeViewOuterScopes.add(s_hour_0)
        timeViewOuterScopes.add(s_hour_1)
        timeViewOuterScopes.add(s_hour_2)
        timeViewOuterScopes.add(s_hour_3)
        timeViewOuterScopes.add(s_hour_4)
        timeViewOuterScopes.add(s_hour_5)
        timeViewOuterScopes.add(s_hour_6)
        timeViewOuterScopes.add(s_hour_7)
        timeViewOuterScopes.add(s_hour_8)
        timeViewOuterScopes.add(s_hour_9)
        timeViewOuterScopes.add(s_hour_10)
        timeViewOuterScopes.add(s_hour_11)
        timeViewOuterScopes.add(s_hour_12)
        timeViewOuterScopes.add(s_hour_13)
        timeViewOuterScopes.add(s_hour_14)
        timeViewOuterScopes.add(s_hour_15)
        timeViewOuterScopes.add(s_hour_16)
        timeViewOuterScopes.add(s_hour_17)
        timeViewOuterScopes.add(s_hour_18)
        timeViewOuterScopes.add(s_hour_19)
        timeViewOuterScopes.add(s_hour_20)
        timeViewOuterScopes.add(s_hour_21)
        timeViewOuterScopes.add(s_hour_22)
        timeViewOuterScopes.add(s_hour_23)
    }

    fun updatePlans(data: List<TimeSchedulePlansParams>) {
        (rlv_plans.adapter as? TimeScheduleAdapter)?.submitList(data)
    }

    private fun createTextView(fakeView: View): TextView {
        return TextView(context).apply {
            setBackgroundResource(R.drawable.bg_round_blue)
            setPadding(
                UIUtils.dip2Px(4).toInt(),
                UIUtils.dip2Px(4).toInt(),
                UIUtils.dip2Px(4).toInt(),
                UIUtils.dip2Px(4).toInt()
            )
            gravity = Gravity.CENTER
            setTextColor(Color.WHITE)
            layoutParams =
                LinearLayout.LayoutParams(timeRectWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
                    .apply {
                        topMargin = UIUtils.dip2Px(4).toInt()
                        leftMargin = UIUtils.dip2Px(4).toInt()
                        rightMargin = UIUtils.dip2Px(4).toInt()
                    }
            text = (fakeView as? AppCompatTextView)?.text.toString()
        }
    }

    fun setListener(listener: OnTimeScheduleLayoutListener) {
        this.listener = listener
    }
}

interface OnTimeScheduleAdapterListener {
    fun onLongClick(data: SinglePlanBean, view: View)
}

interface OnTimeScheduleLayoutListener {
    fun onPlansHasAdded(data: SinglePlanBean)
    fun onTouchViewStatusChange(data: SinglePlanBean, status: TimeSchedulePlansStatus)
}