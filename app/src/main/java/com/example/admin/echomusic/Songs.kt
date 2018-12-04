package com.example.admin.echomusic

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class Songs(var songID: Long, var songTitle: String, var songArtist: String
            , var songData: String, var songAddedDate: Long) : Parcelable {
    override fun writeToParcel(dest: Parcel?, flags: Int) {

    }


    override fun describeContents(): Int {

        return 0

    }


    object Statified {
        /*Here we sort the songs according to their names*/
        var nameComparator: Comparator<Songs> = Comparator<Songs> { song1, song2 ->
            val songOne = song1.songTitle.toUpperCase()
            val songTwo = song2.songTitle.toUpperCase()
            songOne.compareTo(songTwo)
        }
        /*Here we sort them according to the date*/
        var dateComparator: Comparator<Songs> = Comparator<Songs> { song1, song2 ->
            val songOne = song1.songAddedDate.toDouble()
            val songTwo = song2.songAddedDate.toDouble()
            songTwo.compareTo(songOne)
        }
    }


}