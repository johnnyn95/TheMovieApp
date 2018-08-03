package com.example.jnguyen.themovieapp.TheMovieDb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavouriteMoviesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favouriteMovies.db";
    private static final int DATABASE_VERSION = 1;

    public FavouriteMoviesDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        final String SQL_FAVOURITE_MOVIES_TABLE = "CREATE TABLE " +
//                FavouriteMoviesContract.TABLE_NAME + " (" +
//                FavouriteMoviesContract.COLUMN_TITLE + " TEXT NOT NULL, " +
//                FavouriteMoviesContract.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
//                FavouriteMoviesContract.COLUMN_SCORE + " REAL, "
        //TODO finish SQLite table creation
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
