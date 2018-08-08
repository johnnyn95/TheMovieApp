package com.example.jnguyen.themovieapp;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jnguyen.themovieapp.Utilities.NetworkUtils;
import com.example.jnguyen.themovieapp.Utilities.TheMovieDbJSONUtils;
import com.squareup.picasso.Picasso;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {
    private final Context mContext;
    private final MoviesAdapterOnClickHandler mMoviesAdapterOnClickHandler;
    private ContentValues[] mContentValues;

    public MoviesAdapter(Context context,ContentValues[] contentValues,MoviesAdapterOnClickHandler moviesAdapterOnClickHandler){
        mContext = context;
        mContentValues = contentValues;
        mMoviesAdapterOnClickHandler = moviesAdapterOnClickHandler;

    }
    @Override
    public MoviesAdapter.MoviesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_list_item,parent,false);
        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapter.MoviesAdapterViewHolder holder, int position) {
        holder.movieTitle.setText(mContentValues[position].get(TheMovieDbJSONUtils.getMovieTitleKey()).toString());
        holder.movieScore.setText("Score: " + mContentValues[position].get(TheMovieDbJSONUtils.getMovieScoreKey()).toString());
        String imagePath = mContentValues[position].get(TheMovieDbJSONUtils.getMovieImagePathKey()).toString();
        Uri imgUri = NetworkUtils.buildImageUri(imagePath);
        Picasso.get().load(imgUri).into(holder.movieImage);
    }

    @Override
    public int getItemCount() {
        return mContentValues.length;
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        final TextView movieTitle;
        final TextView movieScore;
        final ImageView movieImage;
        final ImageButton movieFavourite;

        MoviesAdapterViewHolder(View view){
            super(view);
            movieTitle = view.findViewById(R.id.tv_movieTitle);
            movieScore = view.findViewById(R.id.tv_movieScore);
            movieImage = view.findViewById(R.id.iv_movieImage);
            movieFavourite = view.findViewById(R.id.ib_favouriteMovie);
            view.setOnClickListener(this);
            movieFavourite.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            ContentValues contentValues = mContentValues[position];

            if(v == movieFavourite){
                Log.d("fav","fav");
                mMoviesAdapterOnClickHandler.addToFavourites(contentValues);
            }
            else {
                Log.d("click", Integer.toString(position));
                mMoviesAdapterOnClickHandler.onClick(contentValues);
            }
        }
    }

    public interface MoviesAdapterOnClickHandler {
        void onClick(ContentValues contentValues);
        void addToFavourites(ContentValues contentValues);
    }
}
