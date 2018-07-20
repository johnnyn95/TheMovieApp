package com.example.jnguyen.themovieapp;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jnguyen.themovieapp.Utilities.NetworkUtils;
import com.example.jnguyen.themovieapp.Utilities.TheMovieDbJSONUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<ContentValues[]>  {

    private static final int MOVIE_SEARCH_LOADER = 69;
    private static final String SEARCH_QUERY_URL_EXTRA = "query";

    EditText mSearchQuery;
    TextView mSearchQueryUrl;
    TextView mSearchQueryResult;

    TextView mErrorMessageDisplay;
    ProgressBar mLoadingIndicator;
    RecyclerView mMoviesList;

    ContentValues[] moviesList;

    MoviesAdapter mMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchQuery =  findViewById(R.id.et_search_box);
        mSearchQueryUrl = findViewById(R.id.tv_url_display);

        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mMoviesList = findViewById(R.id.rv_movies_list);



        if(savedInstanceState != null){
            String queryUrl = savedInstanceState.getString(SEARCH_QUERY_URL_EXTRA);
            mSearchQueryUrl.setText(queryUrl);
        }

        getSupportLoaderManager().initLoader(MOVIE_SEARCH_LOADER,null, this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mMoviesList.setLayoutManager(linearLayoutManager);


    }

    public void makeMovieSearchQuery(){
        String searchQuery = mSearchQuery.getText().toString();
        if(TextUtils.isEmpty(searchQuery)){
            mSearchQueryUrl.setText("No query entered, nothing to search for.");
            return;
        }
        URL searchQueryUrl = NetworkUtils.buildSearchQueryUrl(searchQuery);

        mSearchQueryUrl.setText(searchQueryUrl.toString());

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
                    String searchQueryResults = NetworkUtils.getResponseFromHttpUrl(url);
                    Log.d("json",searchQueryResults);

                    try {
                        jsonResult = TheMovieDbJSONUtils.getMoviesContentValuesFromJson(searchQueryResults);
                        //for (ContentValues value : contentValues){
                        //    Log.d("movie", value.get("title").toString());
                        //}
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
        if(data == null){
            showErrorMessage();
        } else {
            //mSearchQueryResult.setText(data);
            moviesList = data;
            mMoviesAdapter = new MoviesAdapter(this,moviesList);
            mMoviesList.setHasFixedSize(true);
            mMoviesList.setAdapter(mMoviesAdapter);
            //mMoviesAdapter.setNewData(data);
            for(ContentValues value : data){
                Log.d("data",value.toString());
            }

            hideErrorMessage();
        }
    }

//        try {
//            ContentValues[] contentValues = TheMovieDbJSONUtils.getMoviesContentValuesFromJson(this, data);
//            for (ContentValues value : contentValues){
//                Log.d("movie", value.get("title").toString());
//            }
//
//        } catch (JSONException e){
//            e.printStackTrace();
//        }

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
        if(id == R.id.action_search) {
            makeMovieSearchQuery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        String queryUrl = mSearchQueryUrl.getText().toString();
        outState.putString(SEARCH_QUERY_URL_EXTRA,queryUrl);
    }

}

