package com.mdove.civilservantcommunity.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.mdove.dependent.apiservice.AppDependsProvider
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.BaseFragment
import com.mdove.civilservantcommunity.base.threadpool.FastMain
import com.mdove.civilservantcommunity.base.threadpool.MDoveApiPool
import com.mdove.civilservantcommunity.view.MultiLineChooseLayout
import com.mdove.dependent.common.toast.ToastUtil
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by MDove on 2019-09-02.
 */
class RegisterFragment : BaseFragment() {
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let{
            registerViewModel = ViewModelProviders.of(it).get(RegisterViewModel::class.java)
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
        layout_identity.setList(arrayListOf("在校生", "在职人员", "专职备考", "宝妈奶爸", "已上岸"))
        layout_identity.setOnItemClickListener(object :MultiLineChooseLayout.onItemClickListener{
            override fun onItemClick(position: Int, text: String) {
                registerViewModel.onClickIdentity(position)
            }
        })
        tv_ok.setOnClickListener {
            CoroutineScope(coroutineContext+ MDoveApiPool).launch {
                val resp =
                    AppDependsProvider.networkService.networkClient.get("https://www.baidu.com")
                withContext(FastMain) {
                    ToastUtil.toast(resp, Toast.LENGTH_SHORT)
                }
            }
        }
    }
}