package com.mdove.civilservantcommunity.roles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.fragment.BaseFragment

/**
 * Created by MDove on 2019-11-16.
 */
class SelectRolesFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_roles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}