package com.first_Ideall.adapters

import android.content.Context
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.first_Ideall.R
import com.first_Ideall.model.Category
import kotlinx.android.synthetic.main.item_category.view.*


class CategoryAdapter(var c : Context, var lists: ArrayList<Category>,
                        ) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        var v = LayoutInflater.from(c).inflate(R.layout.item_category, parent, false)

        //Init the menu's closed
        v.rv_submenulist.visibility = View.GONE
        v.v_separation.visibility = View.GONE
        v.ib_unroll.animate().rotation(180f).withLayer().setDuration(0).start()

        v.ib_unroll.setOnClickListener {

            //Animate collapse menu
           val animationUp : Animation = AnimationUtils.loadAnimation(c, R.anim.slide_up)
           val animationDown : Animation = AnimationUtils.loadAnimation(c, R.anim.slide_down)

            if(v.rv_submenulist.visibility == View.VISIBLE){
                v.v_separation.startAnimation(animationUp)
                v.rv_submenulist.startAnimation(animationUp)
                v.ib_unroll.animate().rotation(180f).withLayer().setDuration(200).start()
                v.rv_submenulist.visibility = View.GONE
                v.v_separation.visibility = View.GONE
                TransitionManager.beginDelayedTransition(parent)

            }
            else{
                v.rv_submenulist.visibility = View.VISIBLE
                v.v_separation.visibility = View.VISIBLE
                v.rv_submenulist.startAnimation(animationDown)
                v.v_separation.startAnimation(animationDown)
                v.ib_unroll.animate().rotation(0f).withLayer().setDuration(200).start()
                TransitionManager.beginDelayedTransition(parent)
            }
        }

        return CategoryViewHolder(v)

    }


    override fun getItemCount(): Int {
        return lists.size
    }
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bindData(lists[position], c)
    }


    class CategoryViewHolder(itemView: View
    ) : RecyclerView.ViewHolder(itemView){
        fun bindData(category : Category, c: Context){
            itemView.iv_headermenu.setBackgroundResource(category.menuImg)
            itemView.tv_menuTitle.text = category.title
            itemView.rv_submenulist.layoutManager = LinearLayoutManager(c)
            itemView.rv_submenulist.hasFixedSize()
            itemView.rv_submenulist.adapter = SubCategoryAdapter(c, category.subMenus)
        }
    }



}