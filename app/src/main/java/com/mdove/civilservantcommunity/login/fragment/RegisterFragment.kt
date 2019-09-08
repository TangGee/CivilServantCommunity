package com.mdove.civilservantcommunity.login.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.BaseFragment
import com.mdove.civilservantcommunity.login.IAccountHandle
import com.mdove.civilservantcommunity.login.ITransitionProvider
import com.mdove.civilservantcommunity.login.bean.RegisterInfoParams
import com.mdove.civilservantcommunity.login.viewmodel.AccountViewModel
import com.mdove.civilservantcommunity.view.MultiLineChooseLayout
import com.mdove.dependent.common.networkenhance.valueobj.Status
import com.mdove.dependent.common.toast.ToastUtil
import kotlinx.android.synthetic.main.fragment_register.*

/**
 * Created by MDove on 2019-09-02.
 */
class RegisterFragment : BaseFragment(), ITransitionProvider {
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
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layout_identity.setList(mAccountViewModel.identitys)
        layout_identity.setOnItemClickListener(object : MultiLineChooseLayout.onItemClickListener {
            override fun onItemClick(position: Int, text: String) {
                mAccountViewModel.onClickIdentity(position)
            }
        })

        mAccountViewModel.registerResp.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    ToastUtil.toast(it.data?.data?.uid ?: "", Toast.LENGTH_SHORT)
                }
                Status.ERROR -> {
                    ToastUtil.toast(it.data?.message ?: "", Toast.LENGTH_SHORT)
                }
            }
        })

        tv_ok.setOnClickListener {
            val phone = et_phone.text.toString()
            val password = et_password.text.toString()
            val userType = mAccountViewModel.selectIdentity
            if (!TextUtils.isEmpty(phone) &&
                !TextUtils.isEmpty(password) &&
                !TextUtils.isEmpty(userType)
            ) {
                mAccountViewModel.reqRegister(
                    RegisterInfoParams(
                        phone,
                        password,
                        userType!!
                    )
                )
            }
        }

        tv_back.setOnClickListener {
            (activity as? IAccountHandle)?.let{
                it.onBackLogin()
            }
        }
    }

    override fun providerView(): View? {
        return transition_account
    }
}