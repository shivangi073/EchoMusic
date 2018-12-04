package com.example.admin.echomusic

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class Songs(var songID: Long, var songTitle: String, var songArtist: String
            , var songData: String, var songAddedDate: Long) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readLong()) {
    }

    // i have changed the songs and added the creater implementation here
    //this is a change from original code
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeLong(songID)
        dest?.writeString(songTitle)
        dest?.writeString(songArtist)
        dest?.writeString(songData)
        dest?.writeLong(songAddedDate)
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

    companion object CREATOR : Parcelable.Creator<Songs> {
        override fun createFromParcel(parcel: Parcel): Songs {
            return Songs(parcel)
        }

        override fun newArray(size: Int): Array<Songs?> {
            return arrayOfNulls(size)
        }
    }


}