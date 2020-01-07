package com.mdove.civilservantcommunity.setting.hide

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.fragment.BaseFragment
import com.mdove.civilservantcommunity.setting.hide.viewmodel.HideRecordViewModel
import com.mdove.civilservantcommunity.setting.utils.HideRecorder
import kotlinx.android.synthetic.main.fragment_settings.*

/**
 * Created by MDove on 2020-01-06.
 */
class HideRecordFragment : BaseFragment() {
    private lateinit var viewModel: HideRecordViewModel
    private val mAdapter = HideRecordAdapter(object : OnShowClickListener {
        override fun onClick(type: Int) {
            viewModel.showTypeLiveData.value = type
            HideRecorder.removeHideRecord(type)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(HideRecordViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_hide_record, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_toolbar.setTitle(getString(R.string.string_setting_hide))
        view_toolbar.setToolbarBackgroundIsNull()
        view_toolbar.setColorForAll(Color.WHITE)

        rlv.layoutManager = LinearLayoutManager(context)
        rlv.adapter = mAdapter
        viewModel.observerData().observe(this, Observer {
            mAdapter.submitList(it)
        })
    }
}