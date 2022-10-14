package com.first_Ideall.fragments_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.first_Ideall.R
import com.first_Ideall.adapters.CategoryAdapter
import com.first_Ideall.model.Category
import kotlinx.android.synthetic.main.fragment_present_profile.*

class PresentProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_present_profile, container, false)
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)

        //getting users and setting recyclerview
        rv_presentCategory.layoutManager = LinearLayoutManager(activity)
        rv_presentCategory.hasFixedSize()
        rv_presentCategory.adapter = CategoryAdapter(this.requireActivity(), getCategoryLists())

    }

    fun getCategoryLists() : ArrayList<Category>{
        //Give items to menu // (this part is just data test)

        val sub_menu1 = ArrayList<String>()
        sub_menu1.add("Health")
        sub_menu1.add("Exersice")
        sub_menu1.add("Nutrition")

        val sub_menu2 = ArrayList<String>()
        sub_menu2.add("Mindset")
        sub_menu2.add("Learning")

        val sub_menu3 = ArrayList<String>()
        sub_menu3.add("Self Image")
        sub_menu3.add("Courage")

        val sub_menu4 = ArrayList<String>()
        sub_menu4.add("Family")
        sub_menu4.add("Friends")
        sub_menu4.add("Social")

        val sub_menu5 = ArrayList<String>()
        sub_menu5.add("Self Love")
        sub_menu5.add("Relationship")

        val sub_menu6 = ArrayList<String>()
        sub_menu6.add("Work")
        sub_menu6.add("Learning")

        val sub_menu7 = ArrayList<String>()
        sub_menu7.add("Lifestyle")
        sub_menu7.add("Plan")

        val sub_menu8 = ArrayList<String>()
        sub_menu8.add("Full New Life Vision")

        val menus = ArrayList<Category>()
        menus.add(Category("Health and Fitness", sub_menu1,  R.drawable.lights))
        menus.add(Category("Intelectual", sub_menu2, R.drawable.lights))
        menus.add(Category("Character",sub_menu3,R.drawable.lights))
        menus.add(Category("Social", sub_menu4, R.drawable.lights))
        menus.add(Category("Love", sub_menu5, R.drawable.lights))
        menus.add(Category("Carrer", sub_menu6, R.drawable.lights))
        menus.add(Category("Financial", sub_menu7, R.drawable.lights))
        menus.add(Category("Vision", sub_menu8, R.drawable.lights))

        return menus

    }
}

