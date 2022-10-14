package com.first_Ideall.fragments_profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.first_Ideall.R
import com.first_Ideall.adapters.CategoryAdapter
import com.first_Ideall.model.Category
import kotlinx.android.synthetic.main.fragment_ideall_profile.*


class IdeallProfileFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ideall_profile, container, false)
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)

        //getting users and setting recyclerview
        rv_ideallCategory.layoutManager = LinearLayoutManager(activity)
        rv_ideallCategory.hasFixedSize()
        rv_ideallCategory.adapter = CategoryAdapter(this.requireActivity(), getCategoryLists())

    }

    fun getCategoryLists() : ArrayList<Category>{
        //Give items to menu // (this part is just data test)

        val sub_menu1 = ArrayList<String>()
        sub_menu1.add("Health")
        sub_menu1.add("Exersice")
        sub_menu1.add("Nutrition")

        val sub_menu4 = ArrayList<String>()
        sub_menu4.add("Self Love")
        sub_menu4.add("Partner")


        val menus = ArrayList<Category>()
        menus.add(Category("Health and Fitness", sub_menu1,  R.drawable.lights))

        menus.add(Category("Family/Social", sub_menu4, R.drawable.lights))


        return menus

    }

}