package com.mdove.civilservantcommunity.account.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.account.UpdateUserInfoActivity
import com.mdove.civilservantcommunity.account.bean.UserInfoParams
import com.mdove.civilservantcommunity.account.utils.IdentitysHelper
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

    companion object {
        val PARAMS_FRAGMENT = "user_info_params_fragment"
        fun newInstance(params: UserInfoParams): UpdateUserInfoFragment {
            return UpdateUserInfoFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PARAMS_FRAGMENT, params)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            viewModel = ViewModelProviders.of(it).get(AccountViewModel::class.java)
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
        arguments?.getParcelable<UserInfoParams>(PARAMS_FRAGMENT)?.let {
            updateUI(it)
        } ?: also {
            activity?.finish()
        }
        btn_ok.setOnClickListener {
            val name = et_user_name.text.toString().trim()
            if (viewModel.hasChange(name)) {
                viewModel.updateUserInfo(name)
            } else {
                ToastUtil.toast("信息更新不可为空", Toast.LENGTH_SHORT)
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
                    it.data?.let { msg ->
                        ToastUtil.toast(msg.toString(), Toast.LENGTH_SHORT)
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

        viewModel.userInfoParamsLiveData.observe(this, Observer { params ->
            activity?.let {
                if (!it.isFinishing) {
                    val intent = Intent()
                    intent.putExtra(UpdateUserInfoActivity.INTENT_PARAMS, params)
                    it.setResult(Activity.RESULT_OK, intent)
                    it.finish()
                }
            }
        })
    }

    private fun updateUI(params: UserInfoParams) {
        viewModel.userInfoParamsFromMePage = params
        layout_identity.setList(viewModel.identitys)
        et_user_name.hint = params.userName
        layout_identity.setIndexItemSelected(IdentitysHelper.identityKeys.indexOf(params.userType))
        view_toolbar.setTitle("更改信息")
    }
}