package com.mdove.civilservantcommunity.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.account.UpdateUserInfoActivity
import com.mdove.civilservantcommunity.base.BaseFragment
import com.mdove.civilservantcommunity.config.AppConfig
import com.mdove.civilservantcommunity.feed.adapter.MePageAdapter
import com.mdove.civilservantcommunity.feed.viewmodel.MePageViewModel
import com.mdove.civilservantcommunity.account.bean.MePageDataResp
import com.mdove.civilservantcommunity.account.utils.IdentitysHelper
import com.mdove.civilservantcommunity.base.bean.UserInfo
import com.mdove.dependent.common.networkenhance.valueobj.Status
import kotlinx.android.synthetic.main.fragment_me_page.*

/**
 * Created by MDove on 2019-09-07.
 */
class MePageFragment : BaseFragment() {
    private lateinit var viewModel: MePageViewModel
    private lateinit var adapter: MePageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            viewModel = ViewModelProviders.of(it).get(MePageViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_me_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MePageAdapter()
        viewModel.data.observe(this, Observer { res ->
            when (res.status) {
                Status.SUCCESS -> {
                    res.data?.data?.let {
                        srf.isRefreshing = false
                        refreshUI(it)
                    }
                }
                Status.LOADING -> {
                    srf.isRefreshing = true
                }
                Status.ERROR -> {
                    srf.isRefreshing = false
                }
            }
        })


        srf.setOnRefreshListener {
            AppConfig.getUserInfo()?.let {
                viewModel.reqMePage(it.uid)
            }
        }

        btn_update.setOnClickListener {
            context?.let {
                UpdateUserInfoActivity.gotoUpdateUserInfo(it)
            }
        }

        AppConfig.getUserInfo()?.let {
            viewModel.reqMePage(it.uid)
        }
    }

    private fun refreshUI(resp: MePageDataResp) {
        AppConfig.setUserInfo(UserInfo(resp.uid, resp.userName))
        tv_name.text = resp.userName
        tv_type.text = IdentitysHelper.getIdentity(resp.userType)
        resp.articleList?.let {
            adapter.submitList(it)
        }
    }
}