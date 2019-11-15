package com.mdove.civilservantcommunity.plan.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.customview.widget.ViewDragHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.plan.adapter.TimeScheduleAdapter
import com.mdove.civilservantcommunity.plan.model.*
import com.mdove.civilservantcommunity.plan.utils.TimeScheduleHelper
import com.mdove.dependent.common.recyclerview.PaddingDecoration
import com.mdove.dependent.common.utils.UIUtils
import com.mdove.dependent.common.view.removeSelf
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
    private val touchViewMap = mutableMapOf<View, SinglePlanBeanToView>()
    // 记录当前从Time块中移出的View
    private var curTouchViewFromTime: View? = null
    private var lastScrollView: ScrollView? = null
    private var firstLoad = true

    private val mViewDragHelper = ViewDragHelper.create(this, object : ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return child == fake_title_view
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            super.onViewReleased(releasedChild, xvel, yvel)
            hitTimeInnerScope(
                releasedChild,
                releasedChild.left,
                releasedChild.top
            )?.let { pair ->
                val linearLayout = pair.first as? LinearLayout ?: return@let
                linearLayout.addView(createTextView(releasedChild).let { addView ->
                    fake_title_view.visibility = View.GONE
                    handleAddView(addView, pair.second)
                    lastScrollView?.let {
                        scrollAnim(it, true)
                    }
                    addView
                })
            } ?: also {
                fake_title_view.visibility = View.GONE
                lastScrollView?.let {
                    scrollAnim(it, true)
                }
                touchViewMap[fake_title_view]?.let {
                    if (it.isFromRlv) {
                        listener?.onTouchViewStatusChange(it.data, TimeSchedulePlansStatus.SHOW)
                    } else {
                        // 重新释放到Rlv中，并移出Time块中的Plan
                        curTouchViewFromTime?.removeSelf()
                        listener?.onPlansRelease(it.data)
                    }
                }
            }
        }

        @SuppressLint("ObjectAnimatorBinding")
        override fun onViewPositionChanged(
            changedView: View,
            left: Int,
            top: Int,
            dx: Int,
            dy: Int
        ) {
            super.onViewPositionChanged(changedView, left, top, dx, dy)
            hitTimeOuterScope(changedView, left, top)?.let {
                if (lastScrollView != it) {
                    // 移入
                    if (lastScrollView == null) {
                        scrollAnim(it, false)
                    } else {
                        // 老的移出
                        lastScrollView?.let {
                            scrollAnim(it, true)
                        }
                        // 新的移入
                        scrollAnim(it, false)
                    }
                    lastScrollView = it
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

    private fun handleAddView(
        addView: TextView,
        index: Int
    ) {
        touchViewMap[fake_title_view]?.let {
            // 为添加上的View，put坐标信息
            touchViewMap[addView] = SinglePlanBeanToView(
                false,
                it.data
            )
            // 通知Rlv移出Plan
            listener?.onPlansHasAdded(
                it.data,
                TimeScheduleHelper.getTimePairByIndex(index)
            )
        }
        handleAddTimePlans(addView)
    }

    init {
        View.inflate(context, R.layout.layout_time_schedule, this)
        initScopes()
        rlv_plans.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rlv_plans.addItemDecoration(PaddingDecoration(8, false))
        rlv_plans.adapter = TimeScheduleAdapter(object : OnTimeScheduleAdapterListener {
            override fun onClickGotoCreatePlans() {
                listener?.onClickGotoCreatePlans()
            }

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
                curTouchViewFromTime = null
                touchViewMap[fake_title_view] =
                    SinglePlanBeanToView(true, data)
                listener?.onTouchViewStatusChange(data, TimeSchedulePlansStatus.GONE)
                fake_title_view.text = data.content
                fake_title_view.visibility = View.VISIBLE
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

    private fun hitTimeInnerScope(releasedChild: View, left: Int, top: Int): Pair<View, Int>? {
        val centerX = left + releasedChild.width / 4
        val centerY = top + releasedChild.height / 2
        val row = centerX / timeRectWidth
        val column = top / timeRectHeight
        val index = column * 4 + row
        return if (index in 0..23) {
            Pair(timeViewInnerScopes[index], index)
        } else {
            null
        }
    }

    // 处理添加到Time块中的操作
    private fun handleAddTimePlans(addView: TextView) {
        val params = touchViewMap[addView] ?: return
        val listener = OnLongClickListener { view ->
            view.alpha = 0.5F
            val parentTop_ = view.top
            val parentLeft_ = view.left
            val paramsTop =
                (((view.parent as? ViewGroup)?.parent as? ViewGroup)?.parent as? ViewGroup)?.let {
                    it.top + parentTop_
                } ?: parentTop_
            val paramsLeft =
                ((view.parent as? ViewGroup)?.parent as? ViewGroup)?.let {
                    it.left + parentLeft_
                } ?: parentLeft_
            fake_title_view.layoutParams =
                fake_title_view.layoutParams.apply {
                    this.width = view.width
                    this.height = view.height
                    (this as? ConstraintLayout.LayoutParams)?.let { lp ->
                        lp.topMargin = paramsTop
                        lp.leftMargin = paramsLeft
                    }
                }
            fake_title_view.text = params.data.content
            touchViewMap[fake_title_view] = params.copy(isFromRlv = false)
            curTouchViewFromTime = view
            fake_title_view.visibility = View.VISIBLE
            true
        }
        addView.setOnLongClickListener(listener)
        // 添加到其他的Time格子中，此时需要将自己从父View中移除
        curTouchViewFromTime?.removeSelf()
        curTouchViewFromTime = null
    }

    private fun hitTimeOuterScope(touchView: View, left: Int, top: Int): ScrollView? {
        val centerX = left + touchView.width / 4
        val centerY = top + touchView.height / 2
        val row = centerX / timeRectWidth
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

    fun refreshDataAndView(data: List<TimeScheduleBaseParams>) {
        if (firstLoad) {
            firstLoad = false
            initAddView(data.filterIsInstance(TimeSchedulePlansParams::class.java))
        }
        updatePlans(data.filter {
            (it as? TimeSchedulePlansParams)?.timeSchedule == null
        }.takeIf {
            it.isNotEmpty()
        } ?: mutableListOf(TimeScheduleNoPlanParams()))
    }

    private fun initAddView(data: List<TimeSchedulePlansParams>) {
        data.forEach { params ->
            params.timeSchedule?.let {
                TimeScheduleHelper.getIndexByTimePair(it.first).takeIf {
                    it != -1
                }?.let {
                    timeViewInnerScopes[it].addView(
                        createTextView(params.data).apply {
                            // 为添加上的View，put坐标信息
                            touchViewMap[this] = SinglePlanBeanToView(
                                false,
                                params.data
                            )
                            handleAddView(this, it)
                        }
                    )
                }
            }
        }
    }

    private fun updatePlans(data: List<TimeScheduleBaseParams>) {
        (rlv_plans.adapter as? TimeScheduleAdapter)?.submitList(data)
        post {
            rlv_plans.scrollToPosition(0)
        }
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
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = UIUtils.dip2Px(4).toInt()
                    leftMargin = UIUtils.dip2Px(4).toInt()
                    rightMargin = UIUtils.dip2Px(4).toInt()
                }
            text = (fakeView as? AppCompatTextView)?.text.toString()
        }
    }

    private fun createTextView(bean: SinglePlanBean): TextView {
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
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = UIUtils.dip2Px(4).toInt()
                    leftMargin = UIUtils.dip2Px(4).toInt()
                    rightMargin = UIUtils.dip2Px(4).toInt()
                }
            text = bean.content
        }
    }

    fun setListener(listener: OnTimeScheduleLayoutListener) {
        this.listener = listener
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun scrollAnim(view: View, scaleBig: Boolean) {
        val animatorX = ObjectAnimator.ofFloat(
            view,
            "scaleX",
            if (scaleBig) 0.8F else 1F,
            if (scaleBig) 1F else 0.8F
        )
        val animatorY = ObjectAnimator.ofFloat(
            view,
            "scaleY",
            if (scaleBig) 0.8F else 1F,
            if (scaleBig) 1F else 0.8F
        )
        AnimatorSet().apply {
            duration = 300
            playTogether(animatorX, animatorY)
            start()
        }
    }
}

interface OnTimeScheduleAdapterListener {
    fun onLongClick(data: SinglePlanBean, view: View)
    fun onClickGotoCreatePlans()
}

interface OnTimeScheduleLayoutListener {
    // 添加到Time块中
    fun onPlansHasAdded(data: SinglePlanBean, planTime: Pair<Long, Long>)

    // 重新释放到Rlv中
    fun onPlansRelease(data: SinglePlanBean)

    fun onTouchViewStatusChange(data: SinglePlanBean, status: TimeSchedulePlansStatus)
    fun onClickGotoCreatePlans()
}

data class SinglePlanBeanToView(
    var isFromRlv: Boolean,// 标示这个Bean是来自Time块，还是Rlv
    var data: SinglePlanBean
)