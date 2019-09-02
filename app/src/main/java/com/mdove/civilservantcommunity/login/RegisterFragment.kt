package com.mdove.civilservantcommunity.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.view.MultiLineChooseLayout
import kotlinx.android.synthetic.main.fragment_register.*

/**
 * Created by MDove on 2019-09-02.
 */
class RegisterFragment : Fragment() {
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

        }
    }
}