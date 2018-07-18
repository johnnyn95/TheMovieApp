package com.example.jnguyen.themovieapp;

import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jnguyen.themovieapp.Utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    int MOVIE_SEARCH_LOADER = 69;
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
    }
}

