package com.mdove.civilservantcommunity.account.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.account.bean.UpdateUserInfoParams
import com.mdove.civilservantcommunity.account.viewmodel.AccountViewModel
import com.mdove.civilservantcommunity.base.BaseFragment
import com.mdove.civilservantcommunity.config.AppConfig
import com.mdove.dependent.common.networkenhance.valueobj.Status
import kotlinx.android.synthetic.main.fragment_me.*
import kotlinx.android.synthetic.main.fragment_update_user_info.*

/**
 * Created by MDove on 2019-09-16.
 */
class UpdateUserInfoFragment : BaseFragment() {
    private lateinit var viewModel: AccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            viewModel = ViewModelProviders.of(it).get(AccountViewModel::class.java)
        }
        AppConfig.getUserInfo()?.let {
            viewModel.userInfo = it
        } ?: also {
            activity?.finish()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_update_user_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        btn_ok.setOnClickListener {
            viewModel.updateUserInfo(
                UpdateUserInfoParams(
                    null,
                    viewModel.userInfo.uid,
                    et_user_name.text.toString(),
                    null
                )
            )
        }

        viewModel.updateUserInfoResp.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {

                }
                Status.LOADING -> {

                }
                Status.ERROR -> {

                }
            }
        })
    }

    private fun initUI() {
        et_user_name.hint = viewModel.userInfo.username
        view_toolbar.setTitle("更改信息")
    }
}