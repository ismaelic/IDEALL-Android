package com.first_Ideall.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.first_Ideall.R
import kotlinx.android.synthetic.main.item_sub_category.view.*

class SubCategoryAdapter (var c : Context, var lists : ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        var v = LayoutInflater.from(c).inflate(R.layout.item_sub_category, p0, false)
        return Item(v)
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        (p0 as Item).bindData(lists[p1])
    }

    class Item(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindData(_list : String){
            itemView.subMenuButton.text = _list
        }
    }
}