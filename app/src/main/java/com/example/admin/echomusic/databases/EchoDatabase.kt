package com.example.admin.echomusic.databases

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.admin.echomusic.Songs
import java.lang.Exception


class EchoDatabase : SQLiteOpenHelper {

    //sqliteopenhelper provides utilities for database
    var _songlist = ArrayList<Songs>() // to store the data of one songs from the database.

    //to avoid new instatnce of child  objects
    object Staticated {
        val DB_VERSION = 1
        val DB_NAME = "FavoriteDatabase"
        val TABLE_NAME = "FavoriteTable"
        val COLUMN_ID = "SongID"
        val COLUMN_SONG_TITLE = "SongTitle"
        val COLUMN_SONG_ARTIST = "SongArtist"
        val COLUMN_SONG_PATH = "SongPath"

    }
    //If already a table with the same name is present in the database, then this method is
    //skipped

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE " + Staticated.TABLE_NAME + "( " + Staticated.COLUMN_ID +
                " INTEGER," + Staticated.COLUMN_SONG_ARTIST + " STRING," + Staticated.COLUMN_SONG_TITLE
                + " STRING, " + Staticated.COLUMN_SONG_PATH + " STRING);")
    }
    //check for spaces correctly while writing. like " STRING," space with String

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings |File Templates.
    }


    constructor(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?,
                version: Int) : super(context, name, factory, version) {
    }

    constructor(context: Context?) : super(context, Staticated.DB_NAME, null, Staticated.DB_VERSION) {}


    fun storeAsFavorite(id: Int?, artist: String?, songTitle: String?, path: String?) {
/*The function writableDatabase is used to open the db for editing so that changes
can be made to the database*/
        val db = this.writableDatabase // open the database
        //contet values to be processed by the content resolver class can work on
        //content resolver provides access to the content.

        val contentValues_object = ContentValues()
        //.put() to add content to contenvalues object
        contentValues_object.put(Staticated.COLUMN_ID, id)
        contentValues_object.put(Staticated.COLUMN_SONG_ARTIST, artist)
        contentValues_object.put(Staticated.COLUMN_SONG_TITLE, songTitle)
        contentValues_object.put(Staticated.COLUMN_SONG_PATH, path)

        db.insert(Staticated.TABLE_NAME, null, contentValues_object)

        //if content values are empty null means that no values will be inserted.
        db.close()
    }


    fun querydblist(): ArrayList<Songs>? {

        try {
            //? if nothing in database then null will be returned from this function
            val db = this.readableDatabase
            // for reading the database
            //QUERY
            var query_params = "SELECT * FROM " + Staticated.TABLE_NAME
            var cursor = db.rawQuery(query_params, null)
            //selection arguments if the query has dynamic data which can change during passing

            if (cursor.moveToFirst()) {
                do {
                    var _id = cursor.getInt(cursor.getColumnIndexOrThrow(Staticated.COLUMN_ID))
                    var _title = cursor.getString(cursor.getColumnIndexOrThrow(Staticated.COLUMN_SONG_TITLE))
                    var _artist = cursor.getString(cursor.getColumnIndexOrThrow(Staticated.COLUMN_SONG_ARTIST))
                    var _path = cursor.getString(cursor.getColumnIndexOrThrow(Staticated.COLUMN_SONG_PATH))
                    _songlist.add(Songs(_id.toLong(), _title, _artist, _path, 0))
                } while (cursor.moveToNext())
            } else {
                return null
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return _songlist
    }

    fun checkIfIDExist(_id: Int): Boolean {
        var storeid = -500
        // 500 is any no. to keep a check
        val db = this.readableDatabase
        val query_params = "SELECT * FROM " + Staticated.TABLE_NAME + " WHERE SongID = '$_id'"
        // dollar_id in single quotes is a syntax
        val cursor = db.rawQuery(query_params, null)
        if (cursor.moveToFirst()) {
            do {
                storeid = cursor.getInt(cursor.getColumnIndexOrThrow(Staticated.COLUMN_ID))
            } while (cursor.moveToNext())

        } else {
            return false
        }
        return storeid != -500
    }

    fun deleteFavorite(_id: Int) {
        val db = this.writableDatabase
        // as we want to perform something on the databse
        db.delete(Staticated.TABLE_NAME, Staticated.COLUMN_ID + " = " + _id, null)
        db.close()
    }


    //get the size of number of elements in the database at present is given by the checksize function
    fun checksize(): Int {
        var counter = 0
        var db = this.readableDatabase
        var query_params = "SELECT * FROM " + Staticated.TABLE_NAME
        val cursor = db.rawQuery(query_params, null)
        if (cursor.moveToFirst()) {
            do {
                counter = counter + 1
            } while (cursor.moveToNext())
        } else {
            return 0
        }
        return counter

    }
}