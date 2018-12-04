package com.example.admin.echomusic.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.admin.echomusic.R
import com.example.admin.echomusic.activities.MainActivity
import com.example.admin.echomusic.fragments.AboutUsFragment
import com.example.admin.echomusic.fragments.MainScreenFragment
import com.example.admin.echomusic.fragments.SettingsFragment
import com.example.admin.echomusic.fragments.SongPlayingFragment
import kotlinx.android.synthetic.main.app_bar_main.view.*

class NavigationDrawerAdapter(_contentList: ArrayList<String>, _getImages: IntArray, _context: Context)
    : RecyclerView.Adapter<NavigationDrawerAdapter.NavViewHolder>() {

    var contentList: ArrayList<String>? = null
    var getImages: IntArray? = null
    var mcontext: Context? = null

    init {
        this.contentList = _contentList
        this.getImages = _getImages
        this.mcontext = _context

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavViewHolder {
        var itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.raw_custom_navigation_drawer, parent, false)


        var object_navviewholder = NavViewHolder(itemView)
        return object_navviewholder

    }

    override fun getItemCount(): Int {
        return (contentList as ArrayList).size
    }

    override fun onBindViewHolder(holder: NavViewHolder, position: Int) {

        holder?.icon_GET?.setBackgroundResource(getImages?.get(position) as Int)
        holder?.text_GET?.setText(contentList?.get(position))
        holder?.contentholder?.setOnClickListener(

                {
                    if (position == 0) {
                        val object_mainscreenfragment = MainScreenFragment()

                        (mcontext as MainActivity).supportFragmentManager
                                .beginTransaction()
                                .replace(R.id.details_fragment, object_mainscreenfragment)
                                .commit()
                    } else if (position == 1) {
                        val object_favouritefragment = SongPlayingFragment()
                        (mcontext as MainActivity).supportFragmentManager
                                .beginTransaction()
                                .replace(R.id.details_fragment, object_favouritefragment)
                                .commit()

                    } else if (position == 2) {
                        val object_settingsfragment = SettingsFragment()
                        (mcontext as MainActivity).supportFragmentManager
                                .beginTransaction()
                                .replace(R.id.details_fragment, object_settingsfragment)
                                .commit()
                    } else {
                        val object_aboutusfragment = AboutUsFragment()
                        (mcontext as MainActivity).supportFragmentManager
                                .beginTransaction()
                                .replace(R.id.details_fragment, object_aboutusfragment)
                                .commit()
                    }
                    MainActivity.Statified.drawerLayout?.closeDrawers()

                })
    }

    class NavViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        var icon_GET: ImageView? = null
        var text_GET: TextView? = null
        var contentholder: RelativeLayout? = null

        init {

            icon_GET = itemView?.findViewById(R.id.icon_navdrawer_image_in_navdrawer)
            //taking over control by the custom view object i.e. itemView to find the id of other elements
            text_GET = itemView?.findViewById(R.id.text_navdrawer_textview_in_navdrawer)
            contentholder = itemView?.findViewById(R.id.navdrawer_item_content_holder_it_holds_content_of_navdrawer)
        }


    }

}