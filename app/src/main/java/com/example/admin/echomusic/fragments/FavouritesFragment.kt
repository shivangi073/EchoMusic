package com.example.admin.echomusic.fragments


import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.admin.echomusic.R
import com.example.admin.echomusic.Songs
import com.example.admin.echomusic.adapters.FavouritesAdapter
import com.example.admin.echomusic.databases.EchoDatabase
import kotlinx.android.synthetic.main.fragment_favourites.*
import kotlinx.android.synthetic.main.fragment_main_screen.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class FavouritesFragment : Fragment() {
    var myactivity: Activity? = null

    var noFavorites: TextView? = null
    var nowPlayingBottomBar: RelativeLayout? = null
    var songTitle: TextView? = null
    var playPauseButton: ImageButton? = null
    var recyclerView: RecyclerView? = null
    var trackposition: Int = 0
    var favouriteContent_echodatabaseobject: EchoDatabase? = null
    var refreshList: ArrayList<Songs>? = null
    var getsongsfromDatabase: ArrayList<Songs>? = null

    object Statified {
        var mediaplayerobject_favouritefragment: MediaPlayer? = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourites, container, false)

        noFavorites = view?.findViewById(R.id.noFavouritesTextview)
        nowPlayingBottomBar = view.findViewById(R.id.hiddenbarfavscreen)
        //hiddenBarFavScreen = nowplayingbottombar
        songTitle = view.findViewById(R.id.songtitlefavscreen)
        playPauseButton = view?.findViewById(R.id.playpausebuttonFavScreen)
        recyclerView = view?.findViewById(R.id.favouriteRecyler)
        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        myactivity = context as Activity
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        myactivity = activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        favouriteContent_echodatabaseobject = EchoDatabase(myactivity)
        display_fav_by_searching()
        bottombarsetup()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
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
            songTitle?.setText(SongPlayingFragment.Statified.currentSongHelper?.songTitle)
            SongPlayingFragment.Statified.mediaPlayer?.setOnCompletionListener({
                songTitle?.setText(SongPlayingFragment.Statified.currentSongHelper?.songTitle)
                //error here            SongPlayingFragment.Staticated.onSongComplete()
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
            Statified.mediaplayerobject_favouritefragment = SongPlayingFragment.Statified.mediaPlayer
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

        playPauseButton?.setOnClickListener({
            if (SongPlayingFragment.Statified.mediaPlayer?.isPlaying as Boolean) {
                SongPlayingFragment.Statified.mediaPlayer?.pause()
                trackposition = SongPlayingFragment.Statified.mediaPlayer?.currentPosition as Int
                playPauseButton?.setBackgroundResource(R.drawable.play_icon)
            } else {
                SongPlayingFragment.Statified.mediaPlayer?.seekTo(trackposition)
                SongPlayingFragment.Statified.mediaPlayer?.start()
                playPauseButton?.setBackgroundResource(R.drawable.pause_icon)
            }
        })

    }

    fun display_fav_by_searching() {
        if (favouriteContent_echodatabaseobject?.checksize() as Int > 0) {
            refreshList = ArrayList<Songs>()
            getsongsfromDatabase = favouriteContent_echodatabaseobject?.querydblist()
            var fetchlistfromDevice = getsongsfromphone()
            if (fetchlistfromDevice != null) {
                for (i in 0..fetchlistfromDevice?.size - 1) {
                    for (j in 0..getsongsfromDatabase?.size as Int - 1) {
                        if ((getsongsfromDatabase?.get(j)?.songID) === (fetchlistfromDevice?.get(i)?.songID)) {
                            refreshList?.add((getsongsfromDatabase?.get(j) as ArrayList<Songs>)[j])
                        }
                    }
                }

            } else {

            }
            if (refreshList == null) {
                recyclerView?.visibility = View.INVISIBLE
                noFavorites?.visibility = View.VISIBLE
            } else {
                var favouriteAdapter = FavouritesAdapter(refreshList as ArrayList<Songs>, myactivity as Context)
                val mLayoutManager = LinearLayoutManager(activity)
                recyclerView?.layoutManager = mLayoutManager
                recyclerView?.itemAnimator = DefaultItemAnimator()
                recyclerView?.adapter = favouriteAdapter
                recyclerView?.setHasFixedSize(true)
            }

        } else {
            recyclerView?.visibility = View.INVISIBLE
            noFavorites?.visibility = View.VISIBLE
        }

    }
}
