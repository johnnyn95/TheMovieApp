package com.example.jnguyen.themovieapp;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jnguyen.themovieapp.Utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<String> {

    private static final int MOVIE_SEARCH_LOADER = 69;
    private static final String SEARCH_QUERY_URL_EXTRA = "query";

    EditText mSearchQuery;
    TextView mSearchQueryUrl;
    TextView mSearchQueryResult;

    TextView mErrorMessageDisplay;
    ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchQuery =  findViewById(R.id.et_search_box);

        mSearchQueryUrl = findViewById(R.id.tv_url_display);
        mSearchQueryResult =  findViewById(R.id.tv_movie_search_results_json);

        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);


        if(savedInstanceState != null){
            String queryUrl = savedInstanceState.getString(SEARCH_QUERY_URL_EXTRA);
            mSearchQueryUrl.setText(queryUrl);
        }

        getSupportLoaderManager().initLoader(MOVIE_SEARCH_LOADER,null, this);
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

    private void showJsonDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mSearchQueryResult.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the JSON
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        mSearchQueryResult.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            String jsonResult;
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
            public String loadInBackground() {
                String searchQueryUrl = args.getString(SEARCH_QUERY_URL_EXTRA);

                if(searchQueryUrl == null || TextUtils.isEmpty(searchQueryUrl)) {
                    return null;
                }

                try {
                    URL url = new URL(searchQueryUrl);
                    String searchQueryResults = NetworkUtils.getResponseFromHttpUrl(url);
                    Log.d("json",searchQueryResults);
                    return searchQueryResults;
                } catch (IOException e ){
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(String data) {
                jsonResult = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if(data == null){
            showErrorMessage();
        } else {
            mSearchQueryResult.setText(data);
            showJsonDataView();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

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

