package com.sixfingers.filmo.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sixfingers.filmo.R;
import com.sixfingers.filmo.model.Movie;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MoviesListItemAdapter extends ArrayAdapter<Movie> {
    private ArrayList<Movie> movies;

    public static class MovieItemViewHolder extends RecyclerView.ViewHolder {
        public TextView movieTitle;
        public ImageView moviePoster;

        public MovieItemViewHolder(View itemView) {
            super(itemView);

            movieTitle = (TextView) itemView.findViewById(R.id.movie_title);
            moviePoster = (ImageView) itemView.findViewById(R.id.movie_poster);
        }
    }

    public MoviesListItemAdapter(Context context, ArrayList<Movie> movies) {
        super(context, R.layout.movie_list_item, movies);

        this.movies = movies;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieItemViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.movie_list_item,
                    parent,
                    false
            );

            viewHolder = new MovieItemViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MovieItemViewHolder) convertView.getTag();
        }

        viewHolder.movieTitle.setText(movies.get(position).getTitre());
        try {
            viewHolder.moviePoster.setImageBitmap(BitmapFactory.decodeStream(
                    new FileInputStream(
                            new File(
                                    getContext().getFilesDir(),
                                    movies.get(position).getCover()
                            )
                    )
            ));
        } catch (FileNotFoundException e) {
            // TODO : default image
            e.printStackTrace();
        }

        return convertView;
    }
}
