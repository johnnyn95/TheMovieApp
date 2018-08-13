package com.example.jnguyen.themovieapp;

import android.Manifest;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jnguyen.themovieapp.TheMovieDb.FavouriteMoviesContract;
import com.example.jnguyen.themovieapp.TheMovieDb.FavouriteMoviesDbHelper;
import com.example.jnguyen.themovieapp.Utilities.NetworkUtils;
import com.example.jnguyen.themovieapp.Utilities.PageManager;
import com.example.jnguyen.themovieapp.Utilities.TheMovieDbJSONUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

import javax.xml.datatype.Duration;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<ContentValues[]> ,
        SharedPreferences.OnSharedPreferenceChangeListener ,
        MoviesAdapter.MoviesAdapterOnClickHandler{

    private static final int MOVIE_SEARCH_LOADER = 69;
    private static final String SEARCH_QUERY_URL_EXTRA = "query";

    EditText mSearchQuery;
    TextView mErrorMessageDisplay;
    ProgressBar mLoadingIndicator;
    RecyclerView mMoviesList;

    ContentValues[] moviesList;
    String searchQueryUrl;
    SQLiteDatabase mDb;
    MoviesAdapter mMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        setupThemeSharedPreferences(sharedPreferences);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchQuery =  findViewById(R.id.et_search_box);

        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mMoviesList = findViewById(R.id.rv_movies_list);

        if(savedInstanceState != null){
            searchQueryUrl = savedInstanceState.getString(SEARCH_QUERY_URL_EXTRA);
        }

        getSupportLoaderManager().initLoader(MOVIE_SEARCH_LOADER,null, this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mMoviesList.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    public void setupThemeSharedPreferences(SharedPreferences sharedPreferences){
        this.setTheme(getSharedPreferenceTheme(sharedPreferences));
    }

    public int getSharedPreferenceTheme(SharedPreferences sharedPreferences){
        int theme = 0;
        if(sharedPreferences.getString(getString(R.string.pref_theme_key),getString(R.string.pref_theme_light_value)) == getString(R.string.pref_theme_light_value)){
            theme = R.style.AppTheme;
            Log.d("theme","is light");
        }
        if(sharedPreferences.getString(getString(R.string.pref_theme_key),getString(R.string.pref_theme_light_value)) == getString(R.string.pref_theme_dark_value)){
            theme = R.style.TheMovieDbDark;
            Log.d("theme","is dark");
        }
        return theme;
    }

    public void makeMovieSearchQuery(){
        String searchQuery = mSearchQuery.getText().toString();
        if(TextUtils.isEmpty(searchQuery)){
            return;
        }
        URL searchQueryUrl = NetworkUtils.buildSearchQueryUrl(searchQuery);

        Bundle queryBundle = new Bundle();
        queryBundle.putString(SEARCH_QUERY_URL_EXTRA,searchQueryUrl.toString());

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> movieSearchLoader = loaderManager.getLoader(MOVIE_SEARCH_LOADER);

        if(movieSearchLoader == null){
            loaderManager.initLoader(MOVIE_SEARCH_LOADER,queryBundle,  this);
        } else {
            loaderManager.restartLoader(MOVIE_SEARCH_LOADER,queryBundle,this);
        }
    }

    public void launchSettingsActivity(){
        Intent settingsIntent = new Intent(this,SettingsActivity.class);
        startActivity(settingsIntent);
    }

    public void launchFavouritesActivity(){
        Intent settingsIntent = new Intent(this,FavouritesActivity.class);
        startActivity(settingsIntent);
    }

    private void hideErrorMessage() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage() {
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<ContentValues[]> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ContentValues[]>(this) {
            ContentValues[] jsonResult;

            @Override
            protected void onStartLoading() {
                if(args == null){
                    return;
                }
                mLoadingIndicator.setVisibility(View.VISIBLE);
                mMoviesList.setVisibility(View.INVISIBLE);
                if(jsonResult != null){
                    deliverResult(jsonResult);
                } else {
                    forceLoad();
                }
            }

            @Override
            public ContentValues[] loadInBackground() {
                String searchQueryUrl = args.getString(SEARCH_QUERY_URL_EXTRA);
                Log.d("url",searchQueryUrl);

                if(searchQueryUrl == null || TextUtils.isEmpty(searchQueryUrl)) {
                    return null;
                }

                try {
                    URL url = new URL(searchQueryUrl);
                    Log.d("url",searchQueryUrl);
                    String searchQueryResults = NetworkUtils.getResponseFromHttpUrl(url);
                    Log.d("json",searchQueryResults);

                    try {
                        jsonResult = TheMovieDbJSONUtils.getMoviesContentValuesFromJson(searchQueryResults);
                    } catch (JSONException e){

                        e.printStackTrace();
                    }


                } catch (IOException e ){
                    e.printStackTrace();
                    return null;
                }
                return jsonResult;
            }

            @Override
            public void deliverResult(ContentValues[] data) {
                jsonResult = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ContentValues[]> loader, ContentValues[] data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mMoviesList.setVisibility(View.VISIBLE);
        if(data == null){
            showErrorMessage();
        } else {
            moviesList = data;
            Context context = this;
            mMoviesAdapter = new MoviesAdapter(this,moviesList,this);
//            mMoviesList.setHasFixedSize(true);
            mMoviesList.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));
            mMoviesList.setAdapter(mMoviesAdapter);
            hideErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<ContentValues[]> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_search :
                makeMovieSearchQuery();
                return true;
            case R.id.action_settings :
                launchSettingsActivity();
                return true;
            case R.id.action_favourites:
                launchFavouritesActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SEARCH_QUERY_URL_EXTRA,searchQueryUrl);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(R.string.pref_theme_key)){
            setupThemeSharedPreferences(sharedPreferences);
        }
        recreate();
    }

    @Override
    public void onClick(ContentValues contentValues) {
        Intent movieIntent = new Intent(MainActivity.this,MovieActivity.class);
        movieIntent.putExtras(TheMovieDbJSONUtils.getMovieFromContentValue(contentValues));
        startActivity(movieIntent);
    }

    @Override
    public void addToFavourites(ContentValues contentValues) {
        Uri uri = getContentResolver().insert(FavouriteMoviesContract.CONTENT_URI,contentValues);
        Log.d("checkadult",contentValues.getAsString(TheMovieDbJSONUtils.getMovieAdultKey()));
        if(uri != null){
        Toast toast = new Toast(this);
        toast.makeText(this, R.string.added_to_favourites_message, Toast.LENGTH_SHORT).show();
        Log.d("fav",uri.toString());
        }

    }
}

