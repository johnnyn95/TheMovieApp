package com.example.jnguyen.themovieapp.Utilities;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class TheMovieDbJSONUtils {

    private static final String RESULTS = "results";
    private static final String RESULTS_COUNT = "total_results";
    private static final String PAGES_COUNT = "total_pages";
    private static final String MOVIE_TITLE = "title";
    private static final String MOVIE_SCORE = "vote_average";
    private static final String MOVIE_IMAGE_PATH = "poster_path";
    private static final String VOTE_COUNT = "vote_count";
    private static final String POPULARITY = "popularity";
    private static final String ADULT = "adult";
    private static final String OVERVIEW = "overview";
    private static final String RELEASE_DATE = "release_date";

    public static String getMovieTitleKey(){
        return MOVIE_TITLE;
    }
    public static String getMovieScoreKey(){
        return MOVIE_SCORE;
    }
    public static String getMovieImagePathKey(){
        return MOVIE_IMAGE_PATH;
    }
    public static String getMovieVoteCountKey(){
        return VOTE_COUNT;
    }
    public static String getMoviePopularityKey(){
        return POPULARITY;
    }
    public static String getMovieAdultKey(){
        return ADULT;
    }
    public static String getMovieOverviewKey(){
        return OVERVIEW;
    }
    public static String getMovieReleaseDateKey(){
        return RELEASE_DATE;
    }


    public static ContentValues[] getMoviesContentValuesFromJson(String jsonResult)
            throws JSONException {
        JSONObject moviesJson = new JSONObject(jsonResult);

        if(moviesJson.has(RESULTS_COUNT) && moviesJson.getInt(RESULTS_COUNT) == 0){
            return null;
        }
        JSONArray moviesJsonArray = moviesJson.getJSONArray(RESULTS);
        ContentValues[] movieContentValues = new ContentValues[moviesJsonArray.length()];

        for (int i = 0; i < moviesJsonArray.length();i++){
            JSONObject movieJson = moviesJsonArray.getJSONObject(i);

            String movieTitle = movieJson.getString(MOVIE_TITLE);
            Double movieScore = movieJson.getDouble(MOVIE_SCORE);
            String movieImagePath = movieJson.getString(MOVIE_IMAGE_PATH);
            int movieVoteCount = movieJson.getInt(VOTE_COUNT);
            Double moviePopularity = movieJson.getDouble(POPULARITY);
            boolean movieAdult = movieJson.getBoolean(ADULT);
            String movieOverview = movieJson.getString(OVERVIEW);
            String movieReleaseDate = movieJson.getString(RELEASE_DATE);



            ContentValues movieValues = new ContentValues();
            movieValues.put(MOVIE_TITLE,movieTitle);
            movieValues.put(MOVIE_SCORE,movieScore);
            movieValues.put(MOVIE_IMAGE_PATH,movieImagePath);
            movieValues.put(VOTE_COUNT,movieVoteCount);
            movieValues.put(POPULARITY,moviePopularity);
            movieValues.put(ADULT,movieAdult);
            movieValues.put(OVERVIEW,movieOverview);
            movieValues.put(RELEASE_DATE,movieReleaseDate);
            movieContentValues[i] = movieValues;
        }
        return movieContentValues;
    }

    public static int getMoviesQueryPageCount(String jsonResult)throws JSONException {
        JSONObject moviesJson = new JSONObject(jsonResult);
        return moviesJson.getInt(PAGES_COUNT);
    }

    public static Bundle getMovieFromContentValue(ContentValues contentValues){
        Bundle bundle = new Bundle();
        bundle.putString(MOVIE_TITLE,contentValues.getAsString(MOVIE_TITLE));
        bundle.putString(MOVIE_IMAGE_PATH,contentValues.getAsString(MOVIE_IMAGE_PATH));
        bundle.putString(OVERVIEW,contentValues.getAsString(OVERVIEW));
        bundle.putString(MOVIE_SCORE,contentValues.getAsString(MOVIE_SCORE));
        bundle.putString(POPULARITY,contentValues.getAsString(POPULARITY));
        bundle.putString(RELEASE_DATE,contentValues.getAsString(RELEASE_DATE));
        bundle.putString(VOTE_COUNT,contentValues.getAsString(VOTE_COUNT));
        bundle.putString(ADULT,contentValues.getAsString(ADULT));

        return bundle;
    }
}
