package com.a.b.moviesapp.other;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Andrew on 1/21/2016.
 */
public class MoviesDbHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "movies.db";

    private static final int DATABASE_VERSION = 4;
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
            Constants.REVIEWS + " TEXT NOT NULL " +
            " );";

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
         db.execSQL(CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        onCreate(db);
    }
}
