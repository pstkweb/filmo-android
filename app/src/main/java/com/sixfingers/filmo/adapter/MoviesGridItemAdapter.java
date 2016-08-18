package com.sixfingers.filmo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sixfingers.filmo.MovieDetailActivity;
import com.sixfingers.filmo.MovieDetailFragment;
import com.sixfingers.filmo.R;
import com.sixfingers.filmo.helper.MoviesDatabaseHelper;
import com.sixfingers.filmo.model.CollectionMovie;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;

public class MoviesGridItemAdapter extends RecyclerView.Adapter<MoviesGridItemAdapter.MovieGridItemViewHolder> {
    private ArrayList<CollectionMovie> data;
    private MoviesDatabaseHelper helper;
    private boolean isTwoPanes;

    public static class MovieGridItemViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;
        public ImageButton action;
        public View view;

        public MovieGridItemViewHolder(View itemView) {
            super(itemView);

            view = itemView;
            title = (TextView) itemView.findViewById(R.id.movie_title);
            image = (ImageView) itemView.findViewById(R.id.movie_image);
            action = (ImageButton) itemView.findViewById(R.id.grid_item_header_action);
        }
    }

    public MoviesGridItemAdapter(
            ArrayList<CollectionMovie> dataSet,
            MoviesDatabaseHelper dbHelper,
            boolean twoPanes
    ) {
        data = dataSet;
        helper = dbHelper;
        isTwoPanes = twoPanes;
    }

    public void addItem(CollectionMovie movie, int index) {
        data.add(index, movie);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {

        try {
            helper.getCollectionMovieDao().delete(data.get(index));

            data.remove(index);
            notifyItemRemoved(index);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public MovieGridItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.movie_grid_item,
                parent,
                false
        );

        return new MovieGridItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MovieGridItemViewHolder holder, int position) {
        holder.title.setText(data.get(position).getMovie().getTitre());
        try {
            holder.image.setImageBitmap(BitmapFactory.decodeStream(
                    new FileInputStream(
                            new File(
                                    holder.image.getContext().getFilesDir(),
                                    data.get(position).getMovie().getCover()
                            )
                    )
            ));
        } catch (FileNotFoundException e) {
            holder.image.setImageResource(R.drawable.no_image);

            e.printStackTrace();
        }

        holder.action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItem(holder.getAdapterPosition());
            }
        });

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTwoPanes) {
                    Bundle arguments = new Bundle();
                    arguments.putLong(
                            MovieDetailFragment.ARG_MOVIE_ID,
                            data.get(holder.getAdapterPosition()).getMovie().getId()
                    );
                    MovieDetailFragment fragment = new MovieDetailFragment();
                    fragment.setArguments(arguments);

                    (((FragmentActivity) holder.image.getContext()).getSupportFragmentManager())
                            .beginTransaction()
                            .replace(R.id.movie_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.putExtra(
                            MovieDetailFragment.ARG_MOVIE_ID,
                            data.get(holder.getAdapterPosition()).getMovie().getId()
                    );

                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }
}
