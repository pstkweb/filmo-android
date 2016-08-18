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
        public TextView movieTypeAndEdition;
        public ImageView moviePoster;

        public MovieItemViewHolder(View itemView) {
            super(itemView);

            movieTitle = (TextView) itemView.findViewById(R.id.movie_title);
            movieTypeAndEdition = (TextView) itemView.findViewById(R.id.movie_type_edition);
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

        Movie movie = movies.get(position);
        viewHolder.movieTitle.setText(movie.getTitre());

        String typeEdition = movie.getMedia();
        if (movie.getEdition() != null && !movie.getEdition().isEmpty()) {
            typeEdition += (typeEdition != null && !typeEdition.isEmpty() ? " - " : "") +
                    movie.getEdition();
        }
        viewHolder.movieTypeAndEdition.setText(typeEdition);
        try {
            viewHolder.moviePoster.setImageBitmap(BitmapFactory.decodeStream(
                    new FileInputStream(
                            new File(
                                    getContext().getFilesDir(),
                                    movie.getCover()
                            )
                    )
            ));
        } catch (FileNotFoundException e) {
            viewHolder.moviePoster.setImageResource(R.drawable.no_image);

            e.printStackTrace();
        }

        return convertView;
    }
}
