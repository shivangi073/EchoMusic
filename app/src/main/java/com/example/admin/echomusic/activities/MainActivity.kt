package com.example.admin.echomusic.activities

import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import com.example.admin.echomusic.R
import com.example.admin.echomusic.adapters.NavigationDrawerAdapter
import com.example.admin.echomusic.fragments.MainScreenFragment

class MainActivity : AppCompatActivity() {


    var navDrawerIconsList: ArrayList<String> = arrayListOf()

    var images_navDrawer = intArrayOf(R.drawable.navigation_allsongs, R.drawable.navigation_favorites,
            R.drawable.navigation_settings, R.drawable.navigation_aboutus)


    object Statified {
        var drawerLayout: DrawerLayout? = null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        MainActivity.Statified.drawerLayout = findViewById(R.id.drawer_layout)


        var toggle = ActionBarDrawerToggle(this@MainActivity, MainActivity.Statified.drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        MainActivity.Statified.drawerLayout?.setDrawerListener(toggle)
        toggle.syncState()


        navDrawerIconsList.add("All Songs")
        navDrawerIconsList.add("Favourites")
        navDrawerIconsList.add("Settings")
        navDrawerIconsList.add("About Us")


        var mainscreenfragment = MainScreenFragment()
        this.supportFragmentManager.beginTransaction()
                .add(R.id.details_fragment, mainscreenfragment,
                        "this is the string for main Screen Fragment making it unique transaction")
                .commit()


        var _navigationAdapter = NavigationDrawerAdapter(navDrawerIconsList, images_navDrawer, this)
        _navigationAdapter.notifyDataSetChanged()


        val recycler_view_object = findViewById<RecyclerView>(R.id.navigation_recycler_view_in_main_activity)
        recycler_view_object.layoutManager = LinearLayoutManager(this)
        recycler_view_object.itemAnimator = DefaultItemAnimator()
        recycler_view_object.adapter = _navigationAdapter
        recycler_view_object.setHasFixedSize(true)
    }

    override fun onStart() {
        super.onStart()
    }
}
