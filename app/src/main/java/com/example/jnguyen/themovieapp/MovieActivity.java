package com.example.jnguyen.themovieapp;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jnguyen.themovieapp.Utilities.NetworkUtils;
import com.example.jnguyen.themovieapp.Utilities.TheMovieDbJSONUtils;
import com.squareup.picasso.Picasso;

public class MovieActivity extends AppCompatActivity {
    TextView mMovieTitle;
    ImageView mMovieImage;
    TextView mMovieOverview;
    TextView mMovieScore;
    TextView mMoviePopularity;
    TextView mMovieReleaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        mMovieTitle = findViewById(R.id.tv_detailed_movieTitle);
        mMovieOverview = findViewById(R.id.tv_detailed_overview);
        mMovieImage = findViewById(R.id.iv_detailed_movieImage);
        mMovieScore = findViewById(R.id.tv_detailed_movieScore);
        mMoviePopularity = findViewById(R.id.tv_detailed_moviePopularity);
        mMovieReleaseDate =  findViewById(R.id.tv_detailed_movieReleaseDate);

        if(getIntent().getExtras() instanceof Bundle){
            Bundle bundle = getIntent().getExtras();
            mMovieTitle.setText(bundle.getString(TheMovieDbJSONUtils.getMovieTitleKey()));
            mMovieOverview.setText(bundle.getString(TheMovieDbJSONUtils.getMovieOverviewKey()));

            mMovieScore.setText(bundle.getString(TheMovieDbJSONUtils.getMovieScoreKey()));
            mMoviePopularity.setText(bundle.getString(TheMovieDbJSONUtils.getMoviePopularityKey()));
            mMovieReleaseDate.setText(bundle.getString(TheMovieDbJSONUtils.getMovieReleaseDateKey()));

            String imagePath = bundle.getString(TheMovieDbJSONUtils.getMovieImagePathKey());
            Uri imgUri = NetworkUtils.buildImageUri(imagePath);
            Picasso.get().load(imgUri).into(mMovieImage);
        }
    }
}
