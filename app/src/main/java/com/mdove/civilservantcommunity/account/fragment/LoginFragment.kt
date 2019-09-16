package com.mdove.civilservantcommunity.account.fragment

import android.graphics.Paint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mdove.civilservantcommunity.feed.MainFeedActivity
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.BaseFragment
import com.mdove.civilservantcommunity.config.AppConfig
import com.mdove.civilservantcommunity.account.IAccountHandle
import com.mdove.civilservantcommunity.account.ITransitionProvider
import com.mdove.civilservantcommunity.account.bean.LoginInfoParams
import com.mdove.civilservantcommunity.account.viewmodel.AccountViewModel
import com.mdove.dependent.common.networkenhance.valueobj.Status
import com.mdove.dependent.common.toast.ToastUtil
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * Created by MDove on 2019-09-07.
 */
class LoginFragment : BaseFragment(), ITransitionProvider {

    private lateinit var mAccountViewModel: AccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            mAccountViewModel = ViewModelProviders.of(it).get(AccountViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_register.paint.flags = Paint.UNDERLINE_TEXT_FLAG
        btn_register.setOnClickListener {
            (activity as? IAccountHandle)?.let {
                it.onClickRegister()
            }
        }

        mAccountViewModel.loginResp.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data?.status == 0) {
                        AppConfig.setUserInfo(it.data?.data?.userInfo)
                        context?.let {
                            MainFeedActivity.gotoMain(it)
                        }
                    }else{
                        ToastUtil.toast(it.data?.message ?: "登录失败 ", Toast.LENGTH_SHORT)
                    }
                }
                Status.ERROR -> {
                    ToastUtil.toast(it.data?.message ?: "登录失败 ", Toast.LENGTH_SHORT)
                }
            }
        })

        tv_ok.setOnClickListener {
            val phone = et_phone.text.toString()
            val password = et_password.text.toString()
            if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password)) {
                mAccountViewModel.reqLogin(LoginInfoParams(phone, password))
            } else {
                ToastUtil.toast(
                    if (TextUtils.isEmpty(phone)) "账号不可为空~" else "密码不可为空~",
                    Toast.LENGTH_SHORT
                )
            }
        }
    }

    override fun providerView(): View? {
        return transition_account
    }
}