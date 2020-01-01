package com.mdove.civilservantcommunity.roles

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.fragment.BaseFragment
import com.mdove.civilservantcommunity.roles.SelectRolesActivity.Companion.INTENT_PARAMS
import com.mdove.civilservantcommunity.roles.adapter.OnSelectRolesListener
import com.mdove.civilservantcommunity.roles.adapter.SelectRolesAdapter
import kotlinx.android.synthetic.main.fragment_select_roles.*

/**
 * Created by MDove on 2019-11-16.
 */
class SelectRolesFragment : BaseFragment() {
    private lateinit var viewModel: SelecctRolesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(SelecctRolesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_roles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rlv.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        viewModel.data.observe(this, Observer {
            rlv.adapter = SelectRolesAdapter(it, object : OnSelectRolesListener {
                override fun onClick(role: SelectRolesBean) {
                    val intent = Intent()
                    intent.putExtra(
                        INTENT_PARAMS,
                        role
                    )
                    activity?.setResult(Activity.RESULT_OK, intent)
                    activity?.finish()
                }
            })
        })
    }
}