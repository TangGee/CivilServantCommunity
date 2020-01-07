package com.mdove.civilservantcommunity.account.fragment

import android.graphics.Paint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mdove.civilservantcommunity.feed.MainFeedActivity
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.fragment.BaseFragment
import com.mdove.civilservantcommunity.config.AppConfig
import com.mdove.civilservantcommunity.account.IAccountHandle
import com.mdove.civilservantcommunity.account.ITransitionProvider
import com.mdove.civilservantcommunity.account.bean.LoginInfoParams
import com.mdove.civilservantcommunity.account.viewmodel.AccountViewModel
import com.mdove.dependent.common.networkenhance.valueobj.Status
import com.mdove.dependent.common.toast.ToastUtil
import com.mdove.dependent.common.utils.UIUtils
import com.mdove.dependent.common.utils.dismissLoading
import com.mdove.dependent.common.utils.showLoading
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_update_user_info.*

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
                    dismissLoading()
                    if (it.data?.status == 0) {
                        AppConfig.setUserInfo(it.data?.data?.userInfo)
                        context?.let {
                            MainFeedActivity.gotoMain(it)
                            activity?.finish()
                        }
                    } else {
                        ToastUtil.toast(it.data?.message ?: "登录失败 ", Toast.LENGTH_SHORT)
                    }
                }
                Status.LOADING -> {
                    showLoading()
                }
                Status.ERROR -> {
                    dismissLoading()
                    ToastUtil.toast(it.exception?.message ?: "登录失败 ", Toast.LENGTH_SHORT)
                }
            }
        })

        tv_ok.setOnClickListener {
            val phone = et_phone.text.toString().trim()
            val password = et_password.text.toString().trim()
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

    override fun onKeyBoardHide(height: Int) {
        tv_ok?.apply {
            (this.layoutParams as? ConstraintLayout.LayoutParams)?.let {
                it.bottomMargin = UIUtils.dip2Px(context, 36).toInt()
                this.layoutParams = it
            }
        }
    }

    override fun onKeyBoardShow(height: Int) {
        tv_ok?.apply {
            (this.layoutParams as? ConstraintLayout.LayoutParams)?.let {
                it.bottomMargin = height - UIUtils.dip2Px(context, 6).toInt()
                this.layoutParams = it
            }
        }
    }

    override fun providerView(): View? {
        return transition_account
    }
}