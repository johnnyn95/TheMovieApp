package com.example.jnguyen.themovieapp.TheMovieDb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FavouriteMoviesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favouriteMovies.db";
    private static final int DATABASE_VERSION = 5;

    public FavouriteMoviesDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVOURITE_MOVIES_TABLE = "CREATE TABLE " +
                FavouriteMoviesContract.TABLE_NAME + " (" +
                FavouriteMoviesContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavouriteMoviesContract.COLUMN_TITLE + " TEXT, " +
                FavouriteMoviesContract.COLUMN_OVERVIEW + " TEXT, " +
                FavouriteMoviesContract.COLUMN_SCORE + " TEXT, " +
                FavouriteMoviesContract.COLUMN_VOTE_COUNT + " TEXT, " +
                FavouriteMoviesContract.COLUMN_RELEASE_DATE + " TEXT, " +
                FavouriteMoviesContract.COLUMN_IMAGEPATH + " TEXT, " +
                FavouriteMoviesContract.COLUMN_POPULARITY + " TEXT, " +
                FavouriteMoviesContract.COLUMN_ADULT + " TEXT, " +
                FavouriteMoviesContract.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" + ");";
        Log.d("sqllite",SQL_CREATE_FAVOURITE_MOVIES_TABLE);
        db.execSQL(SQL_CREATE_FAVOURITE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavouriteMoviesContract.TABLE_NAME);
        onCreate(db);
    }
}
