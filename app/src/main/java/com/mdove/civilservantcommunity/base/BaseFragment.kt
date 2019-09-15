package com.mdove.civilservantcommunity.base

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import com.mdove.civilservantcommunity.base.fragment.OnFragmentVisibilityChangedListener
import com.mdove.dependent.common.threadpool.FastMainScope
import kotlinx.coroutines.CoroutineScope
import java.util.ArrayList
import kotlin.coroutines.CoroutineContext

/**
 * Created by MDove on 2019-09-03.
 */
open class BaseFragment : Fragment(), CoroutineScope, View.OnAttachStateChangeListener {
    private val mListenerList = ArrayList<OnFragmentVisibilityChangedListener>()
    /**
     * 是否可见（Activity处于前台、Tab被选中、Fragment被添加、Fragment没有隐藏、Fragment.View已经Attach）
     */
    protected var mVisible = false

    override val coroutineContext: CoroutineContext
        get() = FastMainScope()

    override fun onResume() {
        super.onResume()
        onActiveChanged(true)
    }

    override fun onPause() {
        super.onPause()
        onActiveChanged(false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        addOnVisibilityChangedListener(this@, false)
    }

    override fun onDetach() {
        removeAllOnVisibilityChangedListener()
        super.onDetach()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        // hidden 表示是否是隐藏的，后续 checkVisibility 里面的 mVisible 表示是否可见
        // 所以这两个应该是相反的
        checkVisibility(!hidden, "onHiddenChanged")
    }

    /**
     * Tab切换时会回调此方法。对于没有Tab的页面，[Fragment.getUserVisibleHint]默认为true。
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        checkVisibility(isVisibleToUser, "setUserVisibleHint")
    }

    override fun onViewDetachedFromWindow(v: View?) {
        v?.removeOnAttachStateChangeListener(this)
        checkVisibility(false, "onViewDetachedFromWindow")
    }

    override fun onViewAttachedToWindow(v: View?) {
        checkVisibility(true, "onViewAttachedToWindow")
    }

    /**
     * ParentActivity可见性改变
     */
    private fun onActiveChanged(visible: Boolean) {
        checkVisibility(visible, "onActiveChanged")
    }

    /**
     * 检查可见性是否变化
     *
     * @param expected 可见性期望的值。只有当前值和expected不同，才需要做判断
     */
    private fun checkVisibility(expected: Boolean, where: String) {
        if (expected == mVisible) {
            //这里会有重复进入，没问题
        } else {
            val superVisible = super.isVisible()
            val hintVisible = userVisibleHint
            val visible =superVisible && hintVisible
            if (visible != mVisible) {
                mVisible = visible
                onVisibilityChanged(mVisible, where)
            }
        }
    }

    /**
     * 可见性改变
     */
    protected fun onVisibilityChanged(visible: Boolean, where: String) {
        if (!mListenerList.isEmpty()) {
            for (listener in mListenerList) {
                listener.onFragmentVisibilityChanged(visible)
            }
        }
    }

    /**
     * 是否可见（Activity处于前台、Tab被选中、Fragment被添加、Fragment没有隐藏、Fragment.View已经Attach）
     */
    fun isFragmentVisible(): Boolean {
        return mVisible
    }

    private fun addOnVisibilityChangedListener(
        listener: OnFragmentVisibilityChangedListener,
        sticky: Boolean?
    ) {
        if (!mListenerList.contains(listener)) {
            mListenerList.add(listener)
        }
        if (sticky!!) {
            listener.onFragmentVisibilityChanged(mVisible)
        }
    }

    fun addOnVisibilityChangedListener(listener: OnFragmentVisibilityChangedListener) {
        addOnVisibilityChangedListener(listener, false)
    }

    fun removeOnVisibilityChangedListener(listener: OnFragmentVisibilityChangedListener) {
        mListenerList.remove(listener)
    }

    fun removeAllOnVisibilityChangedListener() {
        mListenerList.clear()
    }

}