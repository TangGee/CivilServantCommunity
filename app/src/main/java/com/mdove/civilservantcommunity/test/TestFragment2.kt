package com.mdove.civilservantcommunity.test

import android.content.Context
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
class TestFragment2 :BaseFragment() {

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Log.d("mdove","onAttach -> TestFragment2")
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("mdove","onCreateView -> TestFragment2")
        return inflater.inflate(R.layout.fragment_test2, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("mdove","onDestroyView -> TestFragment2")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("mdove","onDetach -> TestFragment2")
    }
}