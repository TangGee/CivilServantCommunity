package com.mdove.civilservantcommunity.feed

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.BaseFragment
import com.mdove.civilservantcommunity.detailfeed.DetailFeedActivity
import com.mdove.civilservantcommunity.detailfeed.DetailFeedActivity.Companion.DETAIL_FEED_ACTIVITY_PARAMS
import com.mdove.civilservantcommunity.detailfeed.bean.DetailFeedParams
import com.mdove.civilservantcommunity.feed.adapter.MainFeedAdapter
import com.mdove.civilservantcommunity.feed.adapter.OnMainFeedClickListener
import com.mdove.civilservantcommunity.feed.bean.FeedDataResp
import com.mdove.civilservantcommunity.feed.viewmodel.MainFeedViewModel
import com.mdove.dependent.common.networkenhance.valueobj.Status
import com.mdove.dependent.common.toast.ToastUtil
import kotlinx.android.synthetic.main.fragment_main_feed.*

/**
 * Created by MDove on 2019-09-06.
 */
class MainFeedFragment : BaseFragment() {
    private lateinit var feedViewModel: MainFeedViewModel
    private val adapter = MainFeedAdapter(object : OnMainFeedClickListener {
        override fun onClick(resp: FeedDataResp) {
            resp.aid?.let {
                val intent = Intent(activity, DetailFeedActivity::class.java)
                intent.putExtra(DETAIL_FEED_ACTIVITY_PARAMS, DetailFeedParams(it))
                activity?.startActivity(intent)
            }
        }
    })

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
                    ToastUtil.toast("请求失败", Toast.LENGTH_SHORT)
                }
            }
        })

        sfl.setOnRefreshListener {
            feedViewModel.reqFeed()
        }

        feedViewModel.reqFeed()

    }
}