package com.example.admin.echomusic.adapters

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.example.admin.echomusic.R
import com.example.admin.echomusic.Songs
import com.example.admin.echomusic.fragments.SongPlayingFragment
import kotlinx.android.synthetic.main.row_custom_mainscreen_adapter.view.*

class MainScreenAdapter(_songDetails: ArrayList<Songs>, _context: Context)
    : RecyclerView.Adapter<MainScreenAdapter.MyViewHolder>() {
    var songdetails: ArrayList<Songs>? = null
    var mcontext: Context? = null

    init {
        this.songdetails = _songDetails
        this.mcontext = _context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.row_custom_mainscreen_adapter, parent, false)

        return MyViewHolder(itemView)

    }

    override fun getItemCount(): Int {
        if (songdetails == null) {
            return 0
        } else {
            return (songdetails as ArrayList<Songs>).size
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val songObject = songdetails?.get(position)
        holder.trackTitle?.text = songObject?.songTitle
        holder.trackArtist?.text = songObject?.songArtist

        /*
        it will play the song on click by calling the song playing fragment.
        args or arguments are used to communicate between two fragments and multiple arguments can be wrapped
        within an entity called bundle
        we can use bundle to pass data or arguments
        so that we can know which song is being played and which next or previous song to be played

        */

        holder.contentHolder?.setOnClickListener {


            var object_songplayingfragment = SongPlayingFragment()
            var args = Bundle()
            args.putString("keysongtitle", songObject?.songTitle)
            args.putString("keypath", songObject?.songData)
            args.putString("keysongartist", songObject?.songArtist)
            args.putInt("keysongid", songObject?.songID?.toInt() as Int)
            args.putInt("keysongposition", position)
            args.putString("MainScreenBottomBar", "Success")
            //to fetch the list of osngs we have to use parceableArrayList as our song class
            // is a combination of both Int , string and long object so we make our songs class
            // also Parceable by inheriting it from parceable class
            //parcelable meand it can b packed and  distributed  like a parcel to other classes
            //
            //parcelable is an interface for classes whose instances can be written to or restored from a parcel
            //
            args.putParcelableArrayList("songData", songdetails)

            object_songplayingfragment.arguments = args
            (mcontext as FragmentActivity).supportFragmentManager

                    .beginTransaction()
                    .replace(R.id.details_fragment, object_songplayingfragment)
                    .addToBackStack("songplayingfragment")
                    .commit()
        }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var trackTitle: TextView? = null
        var trackArtist: TextView? = null
        var contentHolder: RelativeLayout? = null

        init {
            trackTitle = view.findViewById<TextView>(R.id.trackTitle)
            trackArtist = view.findViewById<TextView>(R.id.trackArtist)
            contentHolder = view.findViewById<RelativeLayout>(R.id.contentRow)
        }
    }


}