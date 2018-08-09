package com.example.jnguyen.themovieapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.jnguyen.themovieapp.TheMovieDb.FavouriteMoviesContract;

public class FavouritesActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> ,
        FavouriteMoviesAdapter.FavouriteMoviesAdapterOnClickHandler{

    private static final int FAVOURITE_MOVIES_LOADER = 96;
    RecyclerView mMoviesList;

    FavouriteMoviesAdapter mAdapter;
    Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        mMoviesList = findViewById(R.id.rv_favourites_movies_list);

        getSupportLoaderManager().initLoader(FAVOURITE_MOVIES_LOADER,null,this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mMoviesList.setLayoutManager(linearLayoutManager);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(this) {
            @Nullable
            @Override
            public Cursor loadInBackground() {
            try{
                return getContentResolver().query(FavouriteMoviesContract.CONTENT_URI,
                        null,
                        null,
                        null,
                        FavouriteMoviesContract.COLUMN_TIMESTAMP);
            }catch (Exception e){
                Log.e("loader","Failed Loader:");
                e.printStackTrace();
                return null;
            }

            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mCursor = data;
        mAdapter = new FavouriteMoviesAdapter(this,mCursor,this);
        mMoviesList.setAdapter(mAdapter);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void onClick(ContentValues contentValues) {

    }

    @Override
    public void removeFromFavourites(ContentValues contentValues) {

    }
}
