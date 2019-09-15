package com.mdove.civilservantcommunity.ugc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.BaseFragment
import com.mdove.civilservantcommunity.ugc.viewmodel.MainUGCViewModel
import com.mdove.civilservantcommunity.view.MultiLineChooseLayout
import kotlinx.android.synthetic.main.fragment_main_ugc.*

/**
 * Created by MDove on 2019-09-15.
 */
class MainUGCFragment : BaseFragment() {
    private lateinit var viewModel: MainUGCViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            viewModel = ViewModelProviders.of(it).get(MainUGCViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_ugc, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layout_identity.setList(viewModel.typeTitles)
        layout_identity.setOnItemClickListener(object : MultiLineChooseLayout.onItemClickListener {
            override fun onItemClick(position: Int, text: String) {
                viewModel.onSelectType(text)
            }
        })

        btn_ok.setOnClickListener {

        }
    }
}