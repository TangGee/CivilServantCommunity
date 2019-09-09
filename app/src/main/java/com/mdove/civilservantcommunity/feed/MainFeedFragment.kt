package com.mdove.civilservantcommunity.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.BaseFragment
import com.mdove.civilservantcommunity.feed.adapter.MainFeedAdapter
import com.mdove.civilservantcommunity.feed.viewmodel.MainFeedViewModel
import com.mdove.dependent.common.networkenhance.valueobj.Status
import kotlinx.android.synthetic.main.fragment_main_feed.*

/**
 * Created by MDove on 2019-09-06.
 */
class MainFeedFragment : BaseFragment() {
    private lateinit var feedViewModel: MainFeedViewModel
    private val adapter = MainFeedAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            feedViewModel = ViewModelProviders.of(it).get(MainFeedViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rlv.adapter = adapter
        rlv.layoutManager = LinearLayoutManager(context)
        feedViewModel.data.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.data?.let { list ->
                        adapter.submitList(list)
                        sfl.isRefreshing = false
                        rlv.updateEmptyUI()
                    }
                }
                Status.LOADING -> {
                    sfl.isRefreshing = true
                    rlv.updateEmptyUI()
                }
                Status.ERROR -> {
                    sfl.isRefreshing = false
                }
            }
        })

        feedViewModel.reqFeed()
    }
}