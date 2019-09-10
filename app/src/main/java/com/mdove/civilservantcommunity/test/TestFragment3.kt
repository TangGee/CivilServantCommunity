package com.mdove.civilservantcommunity.test

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.BaseFragment

/**
 * Created by zhaojing on 2019-09-07.
 */
class TestFragment3 :BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("mdove","onCreateView -> TestFragment3")
        return inflater.inflate(R.layout.fragment_test3, container, false)
    }
}