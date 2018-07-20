package com.example.jnguyen.themovieapp;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Movie;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jnguyen.themovieapp.Utilities.NetworkUtils;
import com.example.jnguyen.themovieapp.Utilities.TheMovieDbJSONUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {
    private final Context mContext;
    private ContentValues[] mContentValues;

    public MoviesAdapter(Context context,ContentValues[] contentValues){
        mContext = context;
        mContentValues = contentValues;
    }
    @Override
    public MoviesAdapter.MoviesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_list_item,parent,false);
        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapter.MoviesAdapterViewHolder holder, int position) {
        holder.movieTitle.setText(mContentValues[position].get(TheMovieDbJSONUtils.getMovieTitleKey()).toString());
        holder.movieScore.setText(mContentValues[position].get(TheMovieDbJSONUtils.getMovieScoreKey()).toString());
        String imagePath = mContentValues[position].get(TheMovieDbJSONUtils.getMovieImagePathKey()).toString();

        Uri imgUri = NetworkUtils.buildImageUri(imagePath);
        Picasso.get().load(imgUri).into(holder.movieImage);
        Log.d("imagePath",imagePath);
        Log.d("uri",imgUri.toString());
        //Picasso.get().load(R.drawable.ic_launcher_background).into(holder.movieImage);
    }

    @Override
    public int getItemCount() {
        return 5;//mContentValues.length;
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder {
        final TextView movieTitle;
        final TextView movieScore;
        final ImageView movieImage;

        MoviesAdapterViewHolder(View view){
            super(view);
            movieTitle = view.findViewById(R.id.tv_movieTitle);
            movieScore = view.findViewById(R.id.tv_movieScore);
            movieImage = view.findViewById(R.id.iv_movieImage);
        }
    }
    public void setNewData(ContentValues[] contentValues){
        mContentValues = contentValues;
    }
}
