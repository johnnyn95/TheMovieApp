package com.example.jnguyen.themovieapp.TheMovieDb;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavouriteMoviesContract implements BaseColumns {

    public static final String CONTENT_AUTHORITY = "com.example.jnguyen.themovieapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FAVOURITE_MOVIES = "favouriteMovies";

    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_FAVOURITE_MOVIES)
            .build();


    public static final String TABLE_NAME = "favouriteMovies";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_SCORE = "vote_average";
    public static final String COLUMN_OVERVIEW = "overview";
    public static final String COLUMN_IMAGEPATH = "poster_path";
    public static final String COLUMN_VOTE_COUNT = "vote_count";
    public static final String COLUMN_RELEASE_DATE = "release_date";
    public static final String COLUMN_POPULARITY = "popularity";
    public static final String COLUMN_ADULT = "adult";
    public static final String COLUMN_TIMESTAMP = "timestamp";

}
