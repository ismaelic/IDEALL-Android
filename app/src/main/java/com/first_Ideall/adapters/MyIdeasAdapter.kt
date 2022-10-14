package com.first_Ideall.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.first_Ideall.databinding.ItemMyIdeasBinding
import com.first_Ideall.listeners.IdeaClickListener
import com.first_Ideall.model.Idea

class MyIdeasAdapter (
    private val ideas: List<Idea>,
    private val clickListener: IdeaClickListener
    ) : RecyclerView.Adapter<MyIdeasAdapter.MyIdeasViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyIdeasViewHolder {
            val from = LayoutInflater.from(parent.context)
            val binding = ItemMyIdeasBinding.inflate(from, parent, false)
            return MyIdeasViewHolder(binding,clickListener)

        }

        override fun onBindViewHolder(holder: MyIdeasViewHolder, position: Int) {
            holder.bindIdea(ideas[position])
        }

        override fun getItemCount(): Int {
            return ideas.size
        }

    class MyIdeasViewHolder(
        private val MyIdeaCardBinding: ItemMyIdeasBinding,
        private val clickListener: IdeaClickListener
    ): RecyclerView.ViewHolder(MyIdeaCardBinding.root)
    {
        fun bindIdea (idea: Idea){
            //MyIdeaCardBinding.user.id.text = idea.user
            MyIdeaCardBinding.cover.setBackgroundResource(idea.image)
            MyIdeaCardBinding.name.text = idea.name

            MyIdeaCardBinding.cardView.setOnClickListener{
                clickListener.onClick(idea)
            }
        }
    }
}