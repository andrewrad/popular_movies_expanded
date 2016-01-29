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
 * Created by Andrew on 1/21/2016.
 */
public class DataProvider extends ContentProvider {
    String TAG="DataProvider class";
    private MoviesDbHelper mOpenHelper;
    private static final String DBNAME = "mydb";
    private static final UriMatcher mMatcher=uriMatcher();

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

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri=null;

        Log.e(TAG,"Uri at insert: "+uri);
        Log.e(TAG,"match: "+mMatcher.match(uri));

        switch (mMatcher.match(uri)){
            case 1:
                Long row= null;

                row = db.insertWithOnConflict(Constants.TABLE_NAME,null,values,SQLiteDatabase.CONFLICT_IGNORE);

//                if ( row > 0 ) {
                    Log.e(TAG, "inserted row: " + row);
                    returnUri = uri;
//                }else {
//                    throw new android.database.SQLException("Failed to insert row into " + uri);
//                }
                break;
        }
        return returnUri;
    }

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
