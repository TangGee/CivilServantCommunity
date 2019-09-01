package com.mdove.civilservantcommunity.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mdove.civilservantcommunity.R
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by MDove on 2019/8/31.
 */
class NestedActivity :AppCompatActivity() {
    fun haha(arr:IntArray){
        arr[0] = arr[0] - 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val arr= intArrayOf(1)
        haha(arr)
        Log.d("mdove","${arr[0]}")
        rlv.layoutManager=LinearLayoutManager(this)
        rlv.adapter=object :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
            override fun getItemCount(): Int {
                return 20
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                    holder.itemView.findViewById<TextView>(R.id.tv_name).text="Haha"
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                return object:RecyclerView.ViewHolder(LayoutInflater.from(this@NestedActivity).
                        inflate(R.layout.item_text,parent,false)){
                }
            }
        }

        rlv2.layoutManager=LinearLayoutManager(this)
        rlv2.adapter=object :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
            override fun getItemCount(): Int {
                return 20
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                holder.itemView.findViewById<TextView>(R.id.tv_name).text="~~~~~Nested~~~~~~~"
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                return object:RecyclerView.ViewHolder(LayoutInflater.from(this@NestedActivity).
                        inflate(R.layout.item_text,parent,false)){
                }
            }

        }
    }
}