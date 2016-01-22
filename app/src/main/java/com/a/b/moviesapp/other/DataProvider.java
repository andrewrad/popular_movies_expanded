package com.a.b.moviesapp.other;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Andrew on 1/21/2016.
 */
public class DataProvider extends ContentProvider {

    private MoviesDbHelper mOpenHelper;
    private static final String DBNAME = "mydb";
    private static final UriMatcher mMatcher=uriMatcher();

    static UriMatcher uriMatcher(){
        final UriMatcher match=new UriMatcher(UriMatcher.NO_MATCH);
        match.addURI(Constants.AUTHORITY,"insert",1);
        match.addURI(Constants.AUTHORITY,"getStoredMovies",2);
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
        switch (mMatcher.match(uri)){
            case 2:
                cursor=mOpenHelper.getReadableDatabase().query(Constants.TABLE_NAME,null,null,null,null,null,Constants.DATE);
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
        switch (mMatcher.match(uri)){
            case 1:
                db.insert(Constants.TABLE_NAME,null,values);
                break;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
