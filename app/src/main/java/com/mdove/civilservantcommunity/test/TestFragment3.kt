package com.mdove.civilservantcommunity.test

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_test3.*

/**
 * Created by zhaojing on 2019-09-07.
 */
class TestFragment3 :BaseFragment() {
    companion object {
        const val FRAGMENT_PARAM = "fragment_params"
        fun newInstance(params: FragmentParams): Fragment =
                TestFragment3().apply {
                    arguments = Bundle().apply {
                        putParcelable(FRAGMENT_PARAM, params)
                    }
                }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_test3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<FragmentParams>(TestFragment1.FRAGMENT_PARAM)?.let {
            tv_title.text = it.title
        }
    }
}