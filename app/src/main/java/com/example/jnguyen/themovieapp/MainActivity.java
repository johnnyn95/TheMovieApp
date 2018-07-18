package com.example.jnguyen.themovieapp;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;

import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jnguyen.themovieapp.Utilities.NetworkUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

    int MOVIE_SEARCH_LOADER = 69;
    private static final String SEARCH_QUERY_URL_EXTRA = "query";
    TextView mSearchQueryUrl;
    EditText mSearchQuery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchQueryUrl = (TextView) findViewById(R.id.tv_search_query_url);
        mSearchQuery = (EditText) findViewById(R.id.et_query);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_search :
                makeMovieSearchQuery();
                return true;

                default: super.onOptionsItemSelected(item);
        }


        return super.onOptionsItemSelected(item);
    }


    public void makeMovieSearchQuery(){
            String searchQuery = mSearchQuery.getText().toString();
            String searchQueryResult = NetworkUtils.buildSearchQueryUrl(searchQuery).toString();
            mSearchQueryUrl.setText(searchQueryResult);
            Log.v("searchQuery",searchQuery);
            Log.v("searchQueryResult",searchQueryResult);
            Bundle queryBundle = new Bundle();
            queryBundle.putString(SEARCH_QUERY_URL_EXTRA,searchQueryResult);
    }

    @Override
    public Loader<String> onCreateLoader(int i, final Bundle bundle) {
        return new AsyncTaskLoader<String>(this) {
            @Override
            public String loadInBackground() {
                String searchQueryUrl = bundle.getString(SEARCH_QUERY_URL_EXTRA);

                if(searchQueryUrl == null || TextUtils.isEmpty(searchQueryUrl))
                {
                    return null;
                }

                try {
                    URL url = new URL(searchQueryUrl);
                    String searchQueryResults = NetworkUtils.getResponseFromHttpUrl(url);
                    return searchQueryResults;
                } catch (IOException e ){
                    e.printStackTrace();
                }

                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String s) {

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}

