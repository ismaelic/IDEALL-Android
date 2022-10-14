package com.first_Ideall.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.first_Ideall.*
import com.first_Ideall.fragments.*
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Setting the default theme insted of the splash screen before charging page
        setTheme(R.style.Theme_ideall)
        setContentView(R.layout.activity_home)

        //MeowNavigation Code
        nav_menu.add(MeowBottomNavigation.Model(0, R.drawable.ic_home_work))
        nav_menu.add(MeowBottomNavigation.Model(1, R.drawable.ic_ideas))
        nav_menu.add(MeowBottomNavigation.Model(2, R.drawable.ic_chats))
        nav_menu.add(MeowBottomNavigation.Model(3, R.drawable.ic_formation))
        nav_menu.add(MeowBottomNavigation.Model(4, R.drawable.ic_stats))

        //Setting up the first Fragment to show up
        addFragment(HomeFragment.newInstance())
        nav_menu.show(0 )

        //Setting the buttons to open the fragments
        nav_menu.setOnClickMenuListener {
            when(it.id){
                0 ->{
                    replaceFragment(HomeFragment.newInstance())
                }
                1 ->{
                    replaceFragment(IdeasFragment.newInstance())
                }
                2 ->{
                    replaceFragment(UsersFragment.newInstance())
                }
                3 ->{
                    replaceFragment(FormationFragment.newInstance())
                }
                4 ->{
                    replaceFragment(StatsFragment.newInstance())
                }
                else -> {
                    replaceFragment(HomeFragment.newInstance())

                }
            }
        }
    }

    //Replace fragment so we can add fragment to the screen
    private fun replaceFragment (fragment:Fragment){
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.fl_menu,fragment).addToBackStack(Fragment::class.java.simpleName).commit()

    }
    //Adding the first fragment to then replace it more
    private fun addFragment (fragment:Fragment){
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.add(R.id.fl_menu,fragment).addToBackStack(Fragment::class.java.simpleName).commit()

    }

}