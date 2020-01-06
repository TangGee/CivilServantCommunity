package com.mdove.civilservantcommunity.setting

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mdove.civilservantcommunity.MyApplication
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.fragment.BaseFragment
import com.mdove.civilservantcommunity.setting.hide.HideRecordActivity
import kotlinx.android.synthetic.main.fragment_settings.*

/**
 * Created by MDove on 2020-01-06.
 */
class SettingFragment : BaseFragment() {
    private val settingTitles = mutableListOf(
        SettingItemBean(MyApplication.getInst().getString(R.string.string_setting_hide), SETTING_TYPE_HIDE),
        SettingItemBean("哈哈")
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_toolbar.setTitle("设置")
        view_toolbar.setToolbarBackgroundIsNull()
        view_toolbar.setColorForAll(Color.WHITE)

        rlv.layoutManager = LinearLayoutManager(context)
        rlv.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                return object : RecyclerView.ViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_settings,
                        parent,
                        false
                    )
                ) {}
            }

            override fun getItemCount(): Int {
                return settingTitles.size
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                holder.itemView.findViewById<TextView>(R.id.tv_title).apply {
                    val bean = settingTitles[position]
                    text = bean.title
                    this.setOnClickListener {
                        if (bean.type == SETTING_TYPE_HIDE) {
                            activity?.let {
                                HideRecordActivity.goto(it)
                            }
                        }
                    }
                }
            }
        }
    }
}