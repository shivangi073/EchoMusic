package com.example.admin.echomusic.fragments


import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.admin.echomusic.R
import com.example.admin.echomusic.Songs
import com.example.admin.echomusic.adapters.MainScreenAdapter
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_main_screen.view.*
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */


class MainScreenFragment : Fragment() {

    var getsongslist: ArrayList<Songs>? = null
    var nowplayingbottombar: RelativeLayout? = null
    var playpausebutton: ImageButton? = null
    var songtitle: TextView? = null
    var visiblelayout: RelativeLayout? = null
    var nosongs: RelativeLayout? = null
    var recylerview: RecyclerView? = null
    var myactivity: Activity? = null
    var trackposition: Int = 0
    var _mainscreenadapter: MainScreenAdapter? = null
    var nowPlayingBottomBar: RelativeLayout? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        var view = inflater!!.inflate(R.layout.fragment_main_screen, container, false)
        setHasOptionsMenu(true)
        activity?.title = "All Songs"
        visiblelayout = view?.findViewById(R.id.visibleLayout)
        recylerview = view?.findViewById<RecyclerView>(R.id.mainscreenfragment_recyclerview)
        nowplayingbottombar = view?.findViewById<RelativeLayout>(R.id.nowplayingBottomBar)
        playpausebutton = view?.findViewById<ImageButton>(R.id.playpausebuttonmainscreen)
        songtitle = view?.findViewById<TextView>(R.id.songtitlemainscreen)
        nosongs = view?.findViewById<RelativeLayout>(R.id.nosongs)

        setHasOptionsMenu(true)
        return view

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getsongslist = getsongsfromphone()
        val prefs = activity?.getSharedPreferences("action_sort", Context.MODE_PRIVATE)
        val sort_by_name = prefs?.getString("sort_by_name", "true")
        val sort_by_recently_added = prefs?.getString("sort_by_recently_added", "false")

        if (getsongslist == null) {
            visiblelayout?.visibility = View.INVISIBLE
            nosongs?.visibility = View.VISIBLE
        } else {

            _mainscreenadapter = MainScreenAdapter(getsongslist as ArrayList<Songs>, myactivity as Context)

            //creating an object of layout manager
            val mLayoutManager = LinearLayoutManager(myactivity)
            recylerview?.layoutManager = mLayoutManager
            recylerview?.itemAnimator = DefaultItemAnimator()
            recylerview?.adapter = _mainscreenadapter
        }
        bottombarsetup()


        if (getsongslist != null) {
            if (sort_by_name!!.equals("true", ignoreCase = true)) {
                Collections.sort(getsongslist, Songs.Statified.nameComparator)
                _mainscreenadapter?.notifyDataSetChanged()
            } else if (sort_by_recently_added!!.equals("true", ignoreCase = true)) {
                Collections.sort(getsongslist, Songs.Statified.dateComparator)
                _mainscreenadapter?.notifyDataSetChanged()
            }
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.main, menu)
        return
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val switcher = item?.itemId
        if (switcher == R.id.sort_by_name) {

            val editor = myactivity?.getSharedPreferences("action_sort",
                    Context.MODE_PRIVATE)?.edit()
            editor?.putString("sort_by_name", "true")
            editor?.putString("sort_by_recently_added", "false")
            editor?.apply()
            if (getsongslist != null) {
                Collections.sort(getsongslist, Songs.Statified.nameComparator)
            }
            _mainscreenadapter?.notifyDataSetChanged()
            return false

        } else if (switcher == R.id.sort_by_recently_added) {
            val editortwo = myactivity?.getSharedPreferences("action_sort",
                    Context.MODE_PRIVATE)?.edit()
            editortwo?.putString("sort_by_recently_added", "true")
            editortwo?.putString("sort_by_name", "false")
            editortwo?.apply()
            if (getsongslist != null) {
                Collections.sort(getsongslist, Songs.Statified.dateComparator)
            }
            _mainscreenadapter?.notifyDataSetChanged()
            return false

        }
        return super.onOptionsItemSelected(item)

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        myactivity = context as Activity
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        myactivity = activity
    }


    fun getsongsfromphone(): ArrayList<Songs> {
        var arrayList = ArrayList<Songs>()
        var contentResolver = myactivity?.contentResolver
        var songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        //string used to identify a resource by system
        var songcursor = contentResolver?.query(songUri, null, null, null, null)
        //projection= to extract the specific column we need to pass a array
        if (songcursor != null && songcursor.moveToFirst()) {
            val songID = songcursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val songTitle = songcursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val songArtist = songcursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val songData = songcursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            val dateIndex = songcursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED)
            while (songcursor.moveToNext()) {
                var currentID = songcursor.getLong(songID)
                var currentTitle = songcursor.getString(songTitle)
                var currentArtist = songcursor.getString(songArtist)
                var currentData = songcursor.getString(songData)
                var currentDate = songcursor.getLong(dateIndex)
                arrayList.add(Songs(currentID, currentTitle, currentArtist, currentData, currentDate))
            }
        }
        return arrayList
    }

    fun bottombarsetup() {
        try {
            bottomBarClickHandler()
            songtitle?.setText(SongPlayingFragment.Statified.currentSongHelper?.songTitle)
            SongPlayingFragment.Statified.mediaPlayer?.setOnCompletionListener({
                songtitle?.setText(SongPlayingFragment.Statified.currentSongHelper?.songTitle)
                //           SongPlayingFragment.Staticated.onSongComplete()
            })
            if (SongPlayingFragment.Statified.mediaPlayer?.isPlaying as Boolean) {
                nowPlayingBottomBar?.visibility = View.VISIBLE
            } else {
                nowPlayingBottomBar?.visibility = View.INVISIBLE

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun bottomBarClickHandler() {
        nowPlayingBottomBar?.setOnClickListener({
            FavouritesFragment.Statified.mediaplayerobject_favouritefragment = SongPlayingFragment.Statified.mediaPlayer
            var object_songplayingfragment = SongPlayingFragment()
            var args = Bundle()
            args.putString("keysongtitle", SongPlayingFragment.Statified.currentSongHelper?.songTitle)
            args.putString("keypath", SongPlayingFragment.Statified.currentSongHelper?.songPath)
            args.putString("keysongartist", SongPlayingFragment.Statified.currentSongHelper?.songArtist)
            args.putInt("keysongid", SongPlayingFragment.Statified.currentSongHelper?.songId?.toInt() as Int)
            args.putInt("keysongposition", SongPlayingFragment.Statified.currentSongHelper?.currentPosition?.toInt() as Int)
            //to fetch the list of osngs we have to use parceableArrayList as our song class
            // is a combination of both Int , string and long object so we make our songs class
            // also Parceable by inheriting it from parceable class
            //parcelable meand it can b packed and  distributed  like a parcel to other classes
            //
            //parcelable is an interface for classes whose instances can be written to or restored from a parcel
            //
            args.putParcelableArrayList("songData", SongPlayingFragment.Statified.fetchSongs)
            //special key to tell song playing fragment that the favourite fragment is accesing the mediaplayer object by this key.
            args.putString("FavBottomBar", "Success")
            object_songplayingfragment.arguments = args

            fragmentManager?.beginTransaction()
                    ?.replace(R.id.details_fragment, object_songplayingfragment)
                    //add to back stack is the key used to make are fragment remain in the stack and not end
                    //it is not destroyed but pushed below currrent playing fragment using keey of string

                    ?.addToBackStack("SongPlayingFragment")
                    ?.commit()
        })
        playpausebutton?.setOnClickListener({
            if (SongPlayingFragment.Statified.mediaPlayer?.isPlaying as Boolean) {
                SongPlayingFragment.Statified.mediaPlayer?.pause()
                trackposition = SongPlayingFragment.Statified.mediaPlayer?.currentPosition as Int
                playpausebutton?.setBackgroundResource(R.drawable.play_icon)
            } else {
                SongPlayingFragment.Statified.mediaPlayer?.seekTo(trackposition)
                SongPlayingFragment.Statified.mediaPlayer?.start()
                playpausebutton?.setBackgroundResource(R.drawable.pause_icon)
            }
        })
    }


}
