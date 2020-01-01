package com.mdove.civilservantcommunity.roles.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.roles.SelectRolesBean

/**
 * Created by MDove on 2020-01-01.
 */
class SelectRolesAdapter(val data: List<SelectRolesBean>, val listener: OnSelectRolesListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RolesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_select_roles, parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RolesViewHolder).bind(data[position])
    }

    inner class RolesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title = itemView.findViewById<TextView>(R.id.tv_title)
        private val flag = itemView.findViewById<TextView>(R.id.tv_flag)

        fun bind(bean: SelectRolesBean) {
            itemView.setOnClickListener {
                listener.onClick(bean)
            }
            title.text = bean.title
            flag.visibility = if (bean.isSys) View.VISIBLE else View.GONE
        }

    }
}

interface OnSelectRolesListener {
    fun onClick(role: SelectRolesBean)
}