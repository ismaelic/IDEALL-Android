package com.first_Ideall.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.first_Ideall.fragments_idea.FirstFragment
import com.first_Ideall.fragments_idea.ForthFragment
import com.first_Ideall.fragments_idea.SecondFragment
import com.first_Ideall.fragments_idea.ThirdFragment
import com.first_Ideall.databinding.ActivityIdeaBinding
import com.first_Ideall.model.IDEA_ID_EXTRA
import com.first_Ideall.model.Idea
import com.first_Ideall.model.ideaList
import kotlinx.android.synthetic.main.activity_idea.*


class IdeaActivity : AppCompatActivity() {

    private lateinit var binding : ActivityIdeaBinding
    private lateinit var idea : Idea
    private var ideaID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Biding data from the other page
        binding = ActivityIdeaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Biding data to this page from idea List
        ideaID = intent.getIntExtra(IDEA_ID_EXTRA,-1)
        idea = ideaFromID(ideaID)!!

        if(idea != null){
            binding.cover.setBackgroundResource(idea.image)
            binding.name.hint = idea.name
        }

        //Add photo on in the fragment
        cover.setOnClickListener{
            pickImageGalery()
        }


        //Fragments in the page
        val adapter = MyViewPagerFragmentAdapter(supportFragmentManager)
        adapter.addFragment(FirstFragment(), "Creation")
        adapter.addFragment(SecondFragment(), "Porpuse")
        adapter.addFragment(ThirdFragment(), "Plan")
        adapter.addFragment(ForthFragment(), "Result")

        vp_Idea.adapter = adapter
        tabIdea.setupWithViewPager(vp_Idea)

        //Setting icons in layout
        tabIdea.getTabAt(0)?.setIcon(com.first_Ideall.R.drawable.logo1)
        tabIdea.getTabAt(1)?.setIcon(com.first_Ideall.R.drawable.logo2)
        tabIdea.getTabAt(2)?.setIcon(com.first_Ideall.R.drawable.logo3)
        tabIdea.getTabAt(3)?.setIcon(com.first_Ideall.R.drawable.logo4)

    }

    private fun ideaFromID(ideaID: Int): Idea? {
        for (idea in ideaList){
            if(idea.id == ideaID){
                return idea
            }
        }
        return null
    }

    class MyViewPagerFragmentAdapter (manager: FragmentManager) :  FragmentPagerAdapter(manager){
        private val fragmentList : MutableList<Fragment> = ArrayList()
        private val titleList : MutableList<String> = ArrayList()

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }
        override fun getCount(): Int {
            return titleList.size
        }

        fun addFragment (fragment: Fragment, title : String){
            fragmentList.add(fragment)
            titleList.add(title)
        }

        override fun getPageTitle (position: Int) :CharSequence?{
            return titleList[position]
        }

    }

    //Add photo on in the activity
    companion object{
        val IMAGE_PICK_CODE = 100
    }

    private fun pickImageGalery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE);

    }
    override fun onActivityResult (requestCode:Int,resultCode:Int, data : Intent?){
        super.onActivityResult(requestCode,resultCode,data)
        if(requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK){
            cover.setImageURI(data?.data)
             //cover.setBackgroundResource(data?.data.toString().toInt()
        }
    }

}

