package com.a.b.moviesapp.other;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Basic database helper class that stores favorited movies. This class initially sets up the named columns
 * for individual data items and provides an update method
 * Created by Andrew on 1/21/2016.
 */
public class MoviesDbHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "movies.db";

    private static final int DATABASE_VERSION = 5;
    private static final String CREATE_MOVIES_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + " (" +
            Constants.SQL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Constants.MOVIE_ID + " TEXT UNIQUE NOT NULL, "+
            Constants.TITLE + " TEXT NOT NULL, " +
            Constants.POSTER_PATH +" TEXT, "+
            Constants.BACKDROP_PATH +" TEXT, "+
            Constants.DATE + " TEXT NOT NULL, " +
            Constants.RATING + " REAL NOT NULL, " +
            Constants.OVERVIEW + " TEXT NOT NULL, " +
            Constants.TRAILERS + " TEXT NOT NULL, " +
            Constants.REVIEWS + " TEXT NOT NULL, " +
            Constants.FAVORITED+ " INTEGER NOT NULL"+
            " );";

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
         db.execSQL(CREATE_MOVIES_TABLE);
    }

    /**
     * Currently set to drop the old database on an upgrade. Will need to be rewritten if the app goes live;
     * no one wants all their saved data to be lost if the app upgrades.
     * @param db passed by Android
     * @param oldVersion passed by Android
     * @param newVersion passed by Android
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO Change onUpgrade to save old database and migrate to new database
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        onCreate(db);
    }
}
