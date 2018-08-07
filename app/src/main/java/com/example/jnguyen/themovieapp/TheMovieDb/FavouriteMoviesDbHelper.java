package com.example.jnguyen.themovieapp.TheMovieDb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FavouriteMoviesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favouriteMovies.db";
    private static final int DATABASE_VERSION = 1;

    public FavouriteMoviesDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVOURITE_MOVIES_TABLE = "CREATE TABLE " +
                FavouriteMoviesContract.TABLE_NAME + " (" +
                FavouriteMoviesContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavouriteMoviesContract.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavouriteMoviesContract.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                FavouriteMoviesContract.COLUMN_SCORE + " REAL NOT NULL, " +
                FavouriteMoviesContract.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                FavouriteMoviesContract.COLUMN_IMAGEPATH + " TEXT NOT NULL, " +
                FavouriteMoviesContract.COLUMN_POPULARITY + " TEXT NOT NULL, " +
                FavouriteMoviesContract.COLUMN_ADULT + " TEXT NOT NULL, " +
                FavouriteMoviesContract.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" + ");";
        Log.d("sqllite",SQL_CREATE_FAVOURITE_MOVIES_TABLE);
        //TODO finish SQLite table creation
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavouriteMoviesContract.TABLE_NAME);
        onCreate(db);
    }
}
