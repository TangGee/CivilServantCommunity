package com.mdove.civilservantcommunity.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.BaseActivity
import kotlinx.android.synthetic.main.activity_test_touch.*

class TestTouchActivity :BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_touch)
        rlv.layoutManager=LinearLayoutManager(this)
        rlv.adapter=object:RecyclerView.Adapter<RecyclerView.ViewHolder>(){
            override fun getItemCount(): Int {
                return 10
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                if(viewType ==1){
                    return   ViewHolder1(LayoutInflater.from(parent.context).inflate(R.layout.item_text_touch,parent,false))
                }else{
                    return   ViewHolder2(LayoutInflater.from(parent.context).inflate(R.layout.item_text,parent,false))
                }
            }

            override fun getItemViewType(position: Int): Int {
                if(position==0){
                    return 1
                }
                return 0
            }
        }
    }

    inner class ViewHolder1(itemVIew:View):RecyclerView.ViewHolder(itemVIew){}
    inner class ViewHolder2(itemVIew:View):RecyclerView.ViewHolder(itemVIew){}
}