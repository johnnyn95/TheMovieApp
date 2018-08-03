package com.example.jnguyen.themovieapp.TheMovieDb;

import android.provider.BaseColumns;

public class FavouriteMoviesContract implements BaseColumns {
    public static final String TABLE_NAME = "favouriteMovies";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_SCORE = "score";
    public static final String COLUMN_OVERVIEW = "overview";
    public static final String COLUMN_IMAGEPATH = "imagePath";
    public static final String COLUMN_RELEASE_DATE = "releaseDate";
    public static final String COLUMN_POPULARITY = "popularity";

}
