package com.mdove.civilservantcommunity.login.fragment

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mdove.civilservantcommunity.MainActivity
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.BaseFragment
import com.mdove.civilservantcommunity.config.AppConfig
import com.mdove.civilservantcommunity.login.IAccountHandle
import com.mdove.civilservantcommunity.login.ITransitionProvider
import com.mdove.civilservantcommunity.login.bean.LoginInfoParams
import com.mdove.civilservantcommunity.login.bean.UserInfo
import com.mdove.civilservantcommunity.login.viewmodel.AccountViewModel
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
                    AppConfig.setUserInfo(UserInfo(it.data?.data ?: ""))
                    ToastUtil.toast(it.data?.data ?: "", Toast.LENGTH_SHORT)
                    val intent = Intent(activity, MainActivity::class.java)
                    activity?.startActivity(intent)
                    activity?.finish()
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