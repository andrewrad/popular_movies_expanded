package com.a.b.moviesapp.other;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * This project uses a ContentProvider to access the SQLite Database for stored movies. A Content Provider
 * may be overkill for this type of project, but would be necessary if the app ever needs outside access
 * to the database. This level of abstraction helps safeguard unwelcome access to the database.
 * Created by Andrew on 1/21/2016.
 */
public class DataProvider extends ContentProvider {
    String TAG="DataProvider class";
    private MoviesDbHelper mOpenHelper;
    private static final String DBNAME = "mydb";
    private static final UriMatcher mMatcher=uriMatcher();

    /**
     * All requests to the database must match one of the schemes provided in the UriMatcher if there are
     * more than one type in each CRUD operation. For example, there are two type of queries in this project,
     * one that retrieves all data from the table, the other passes a selection for just one row
     * @return
     */
    static UriMatcher uriMatcher(){
        final UriMatcher match=new UriMatcher(UriMatcher.NO_MATCH);
        match.addURI(Constants.AUTHORITY,"insert",1);
        match.addURI(Constants.AUTHORITY, "get_stored_movies", 2);
        match.addURI(Constants.AUTHORITY, "delete",3);
        match.addURI(Constants.AUTHORITY, "get_favorite", 4);
        return match;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper= new MoviesDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor=null;
        Log.e(TAG,"Uri at query: "+uri);
        Log.e(TAG,"match: "+mMatcher.match(uri));
        switch (mMatcher.match(uri)){
            case 2:
                Log.e(TAG,"case 2");
                cursor=mOpenHelper.getReadableDatabase().query(Constants.TABLE_NAME,null,null,null,null,null,null);
                break;
            case 4:
                cursor=mOpenHelper.getReadableDatabase().query(Constants.TABLE_NAME,projection,selection,selectionArgs,null,null,null);
                break;
        }
        return cursor;
    }

    /**
     * Required for ContentProvider
     * @param uri
     * @return null
     */
    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    /**
     * Used to insert favorite movies into the database. Since a favorite movie may already be in the database,
     * the insertWithOnConflict() function is used to force an over write. If given more time on the project, I
     * should change that to detect a match in the database and trigger an update() instead
     * @param uri
     * @param values
     * @return
     */
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri=null;

        Log.e(TAG,"Uri at insert: "+uri);
        Log.e(TAG,"match: "+mMatcher.match(uri));

        /*switch statement not needed if matching only one type of insert. However, it is left in for future
        insert types */
        switch (mMatcher.match(uri)){
            case 1:
                Long row= null;
                row = db.insertWithOnConflict(Constants.TABLE_NAME,null,values,SQLiteDatabase.CONFLICT_IGNORE);
                Log.e(TAG, "inserted row: " + row);
                returnUri = uri;
                break;
        }
        return returnUri;
    }

    /**
     * Deletes a movie from the favorites database based on unique movie id provided from themoviedb.com api
     * @param uri
     * @param selection movie_id for movie needed to be deleted
     * @param selectionArgs
     * @return rows deleted, -1 if nothing deleted
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Log.e(TAG, "Data Provider DELETE method");
        int rows=db.delete(Constants.TABLE_NAME, Constants.MOVIE_ID+ "=" +selection,null);
        return rows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
