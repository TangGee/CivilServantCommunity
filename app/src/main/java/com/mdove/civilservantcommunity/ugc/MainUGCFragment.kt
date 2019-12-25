package com.mdove.civilservantcommunity.ugc

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.fragment.BaseFragment
import com.mdove.civilservantcommunity.config.AppConfig
import com.mdove.civilservantcommunity.ugc.adapter.OnTopicSelectListener
import com.mdove.civilservantcommunity.ugc.adapter.UgcRlvTopicAdapter
import com.mdove.civilservantcommunity.ugc.bean.UGCRlvTopicBean
import com.mdove.civilservantcommunity.ugc.viewmodel.MainUGCViewModel
import com.mdove.civilservantcommunity.view.MultiLineChooseLayout
import com.mdove.dependent.common.networkenhance.valueobj.Status
import com.mdove.dependent.common.recyclerview.PaddingDecoration
import com.mdove.dependent.common.recyclerview.PaddingType
import com.mdove.dependent.common.toast.ToastUtil
import com.mdove.dependent.common.utils.dismissLoading
import com.mdove.dependent.common.utils.showLoading
import com.mdove.dependent.common.view.OnToolbarListener
import kotlinx.android.synthetic.main.fragment_edit_plan_container.*
import kotlinx.android.synthetic.main.fragment_main_ugc.*
import kotlinx.android.synthetic.main.fragment_main_ugc.view_toolbar

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

    private val adapter = UgcRlvTopicAdapter(object : OnTopicSelectListener {
        override fun onSelect(bean: UGCRlvTopicBean, select: Boolean) {
            viewModel.clickTopicLiveData.value = bean.copy(selectStatus = select)
        }
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewModel.topics.observe(this, Observer {
            adapter.submitList(it)
        })

        layout_identity.setOnItemClickListener(object :
            MultiLineChooseLayout.onItemClickListener {
            override fun onItemClick(position: Int, text: String) {
                viewModel.onSelectType(text)
            }
        })

        viewModel.getTopics()

        view_toolbar.setListener(object :OnToolbarListener {
            override fun onRightBtnClick() {
                val title = et_title.text.toString()
                val content = et_content.text.toString()
                AppConfig.getUserInfo()?.let {
                    viewModel.postQuestion(it, title, content)?.observe(activity!!, Observer {
                        when (it.status) {
                            Status.SUCCESS -> {
                                dismissLoading()
                                activity?.finish()
                                ToastUtil.toast("请求成功:${it.data?.message ?: ""}")
                            }
                            Status.ERROR -> {
                                dismissLoading()
                                ToastUtil.toast("请求失败:${it.data?.message ?: ""}")
                            }
                            Status.LOADING -> {
                                showLoading()
                            }
                        }
                    })
                }
            }
        })

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

    private fun initView() {
        view_toolbar.setTitle("求助大神")
        view_toolbar.setRightBtnTitle("发送")
        view_toolbar.setToolbarBackgroundIsNull()
        view_toolbar.setColorForAll(Color.BLACK)

        layout_identity.setList(viewModel.typeTitles)
        rlv_topic.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rlv_topic.addItemDecoration(PaddingDecoration(12, PaddingType.LEFT))
        rlv_topic.adapter = adapter
    }
}