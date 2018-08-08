package com.example.jnguyen.themovieapp.TheMovieDb;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MovieProvider extends ContentProvider {

    //public static final int CODE_ALL_MOVIES = 100;
    public static final int CODE_ALL_MOVIES = 101;
    private  static final UriMatcher sUriMatcher = buildUriMatcher();

    private FavouriteMoviesDbHelper mFavouriteMoviesDbHelper;


    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(FavouriteMoviesContract.CONTENT_AUTHORITY,FavouriteMoviesContract.PATH_FAVOURITE_MOVIES,CODE_ALL_MOVIES);
        //uriMatcher.addURI(FavouriteMoviesContract.CONTENT_AUTHORITY,FavouriteMoviesContract.PATH_FAVOURITE_MOVIES,CODE_ADULT_MOVIES);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mFavouriteMoviesDbHelper = new FavouriteMoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mFavouriteMoviesDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri ;
        switch (match) {
            case CODE_ALL_MOVIES:
            long id = db.insert(FavouriteMoviesContract.TABLE_NAME,null,values );
            if(id > 0){
                returnUri = FavouriteMoviesContract.CONTENT_URI;//ContentUris.withAppendedId(FavouriteMoviesContract.CONTENT_URI,id);
            } else {
                throw new android.database.SQLException("Unsuccessful insert in: " + uri);
            }
            break;
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
