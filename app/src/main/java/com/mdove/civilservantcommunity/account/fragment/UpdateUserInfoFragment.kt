package com.mdove.civilservantcommunity.account.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.account.viewmodel.AccountViewModel
import com.mdove.civilservantcommunity.base.BaseFragment
import com.mdove.civilservantcommunity.config.AppConfig
import com.mdove.civilservantcommunity.view.MultiLineChooseLayout
import com.mdove.dependent.common.networkenhance.valueobj.Status
import com.mdove.dependent.common.toast.ToastUtil
import com.mdove.dependent.common.utils.dismissLoading
import com.mdove.dependent.common.utils.showLoading
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
            val name = et_user_name.text.toString().trim()
            if (!TextUtils.isEmpty(name)) {
                viewModel.updateUserInfo(name)
            }else{
                ToastUtil.toast("信息更新不可为空",Toast.LENGTH_SHORT)
            }
        }

        layout_identity.setOnItemClickListener(object : MultiLineChooseLayout.onItemClickListener {
            override fun onItemClick(position: Int, text: String) {
                viewModel.onClickIdentity(position)
            }
        })

        viewModel.updateUserInfoResp.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissLoading()
                    it.data?.message?.let { msg ->
                        ToastUtil.toast(msg, Toast.LENGTH_SHORT)
                    }
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

    private fun initUI() {
        layout_identity.setList(viewModel.identitys)
        et_user_name.hint = viewModel.userInfo.username
        view_toolbar.setTitle("更改信息")
    }
}