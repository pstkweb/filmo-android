package com.sixfingers.filmo.adapter;

import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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

    public static class MovieGridItemViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;
        public ImageButton action;

        public MovieGridItemViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.movie_title);
            image = (ImageView) itemView.findViewById(R.id.movie_image);
            action = (ImageButton) itemView.findViewById(R.id.grid_item_header_action);
        }
    }

    public MoviesGridItemAdapter(ArrayList<CollectionMovie> dataSet, MoviesDatabaseHelper dbHelper) {
        data = dataSet;
        helper = dbHelper;
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
            // TODO : Add default image
            e.printStackTrace();
        }

        holder.action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItem(holder.getAdapterPosition());
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
