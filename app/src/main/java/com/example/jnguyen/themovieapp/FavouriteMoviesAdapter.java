package com.example.jnguyen.themovieapp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
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

import com.example.jnguyen.themovieapp.TheMovieDb.FavouriteMoviesContract;
import com.example.jnguyen.themovieapp.Utilities.NetworkUtils;
import com.example.jnguyen.themovieapp.Utilities.TheMovieDbJSONUtils;
import com.squareup.picasso.Picasso;

public class FavouriteMoviesAdapter extends
        RecyclerView.Adapter<FavouriteMoviesAdapter.FavouriteMoviesAdapterViewHolder> {

    private final Context mContext;
    private Cursor mCursor;
    FavouriteMoviesAdapterOnClickHandler mFavouriteMoviesAdapterOnClickHandler;

    public FavouriteMoviesAdapter(Context context, Cursor cursor, FavouriteMoviesAdapterOnClickHandler handler){
        mContext = context;
        mCursor = cursor;
        mFavouriteMoviesAdapterOnClickHandler = handler;
    }

    @NonNull
    @Override
    public FavouriteMoviesAdapter.FavouriteMoviesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.favourite_movie_list_item,parent,false);
        return  new FavouriteMoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteMoviesAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.movieTitle.setText(mCursor.getString(mCursor.getColumnIndex(TheMovieDbJSONUtils.getMovieTitleKey())));
        holder.movieScore.setText("Score: " + mCursor.getString(mCursor.getColumnIndex(TheMovieDbJSONUtils.getMovieScoreKey())));
        String imagePath = mCursor.getString(mCursor.getColumnIndex(TheMovieDbJSONUtils.getMovieImagePathKey()));
        Uri imgUri = NetworkUtils.buildImageUri(imagePath);
        Picasso.get().load(imgUri).into(holder.movieImage);

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public class FavouriteMoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView movieTitle;
        final TextView movieScore;
        final ImageView movieImage;
        final ImageButton movieFavourite;

        FavouriteMoviesAdapterViewHolder(View view){
            super(view);
            movieTitle = view.findViewById(R.id.fm_tv_movieTitle);
            movieScore = view.findViewById(R.id.fm_tv_movieScore);
            movieImage = view.findViewById(R.id.fv_iv_movieImage);
            movieFavourite = view.findViewById(R.id.fm_ib_removeMovie);
            view.setOnClickListener(this);
            movieFavourite.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            ContentValues contentValues = new ContentValues();
            DatabaseUtils.cursorRowToContentValues(mCursor,contentValues);

            if(v == movieFavourite){
                mCursor.moveToPosition(position);
                String title = mCursor.getString(mCursor.getColumnIndex(FavouriteMoviesContract.COLUMN_TITLE));

                Log.d("delete",title);
                mFavouriteMoviesAdapterOnClickHandler.removeFromFavourites(title);
            }
            else {
                Log.d("click", Integer.toString(position));
                mFavouriteMoviesAdapterOnClickHandler.onClick(contentValues);
            }
        }
    }

    public Cursor swapCursor(Cursor c){
        if(mCursor == c){
            return null;
        }
        Cursor temp = mCursor;
        this.mCursor = c;
        if(c != null){
            this.notifyDataSetChanged();
        }
        return temp;
    }

    public interface FavouriteMoviesAdapterOnClickHandler {
        void onClick(ContentValues contentValues);
        void removeFromFavourites(String title);
    }
}
