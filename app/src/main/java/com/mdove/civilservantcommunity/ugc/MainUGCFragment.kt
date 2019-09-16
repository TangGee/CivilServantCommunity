package com.mdove.civilservantcommunity.ugc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.BaseFragment
import com.mdove.civilservantcommunity.config.AppConfig
import com.mdove.civilservantcommunity.ugc.viewmodel.MainUGCViewModel
import com.mdove.civilservantcommunity.view.MultiLineChooseLayout
import com.mdove.dependent.common.networkenhance.valueobj.Status
import com.mdove.dependent.common.utils.dismissLoading
import com.mdove.dependent.common.utils.showLoading
import kotlinx.android.synthetic.main.fragment_main_ugc.*

/**
 * Created by MDove on 2019-09-15.
 */
class MainUGCFragment : BaseFragment() {
    private lateinit var viewModel: MainUGCViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            viewModel = ViewModelProviders.of(it).get(MainUGCViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_ugc, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_toolbar.setTitle("分享")
        layout_identity.setList(viewModel.typeTitles)
        layout_identity.setOnItemClickListener(object : MultiLineChooseLayout.onItemClickListener {
            override fun onItemClick(position: Int, text: String) {
                viewModel.onSelectType(text)
            }
        })

        btn_ok.setOnClickListener {
            val title = et_title.text.toString()
            val content = et_content.text.toString()
            AppConfig.getUserInfo()?.let {
                viewModel.post(it, title, content)
            }
        }

        viewModel.postResp.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissLoading()
                }
                Status.LOADING -> {
                    showLoading()
                }
                Status.ERROR -> {
                    dismissLoading()
                }
            }
        })
    }
}