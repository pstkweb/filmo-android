package com.sixfingers.filmo.adapter;

import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sixfingers.filmo.R;
import com.sixfingers.filmo.model.Movie;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BarcodeListItemAdapter extends BaseAdapter {
    private ArrayList<String> keys;
    private ArrayList<List<Movie>> values;

    public static class BarcodeSearchViewHolder extends RecyclerView.ViewHolder {
        public TextView barcodeText;
        public ProgressBar progress;

        public BarcodeSearchViewHolder(View itemView) {
            super(itemView);

            barcodeText = (TextView) itemView.findViewById(R.id.barcode_text);
            progress = (ProgressBar) itemView.findViewById(R.id.action_progress);
        }
    }

    public static class BarcodeErrorViewHolder extends RecyclerView.ViewHolder {
        public TextView errorMessage;

        public BarcodeErrorViewHolder(View itemView) {
            super(itemView);

            errorMessage = (TextView) itemView.findViewById(android.R.id.text1);
            errorMessage.setTextColor(
                    ContextCompat.getColor(itemView.getContext(), android.R.color.holo_red_dark)
            );
        }
    }

    public BarcodeListItemAdapter(HashMap<String, List<Movie>> barcodes) {
        keys = new ArrayList<>();
        keys.addAll(barcodes.keySet());

        values = new ArrayList<>();
        values.addAll(barcodes.values());
    }

    public boolean add(String text) {
        if (!keys.contains(text)) {
            keys.add(text);
            values.add(new ArrayList<Movie>());

            notifyDataSetChanged();

            return true;
        }

        return false;
    }

    public void updateItem(String barcode, List<Movie> movies) {
        values.add(keys.indexOf(barcode), movies);

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return keys.size();
    }

    @Override
    public List<Movie> getItem(int i) {
        return values.get(i);
    }

    @Override
    public long getItemId(int i) {
        return keys.get(i).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String barcode = keys.get(position);
        List<Movie> movies = getItem(position);

        Log.d("TEST", "B: " + barcode);
        // Error or no result
        if (movies == null) {
            BarcodeErrorViewHolder viewHolder;
            if (convertView == null || convertView.getId() != android.R.id.text1) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(
                        android.R.layout.simple_list_item_1,
                        parent,
                        false
                );

                viewHolder = new BarcodeErrorViewHolder(convertView);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (BarcodeErrorViewHolder) convertView.getTag();
            }

            viewHolder.errorMessage.setText(
                    String.format(
                            convertView.getResources().getString(R.string.no_result_search_barcode),
                            barcode
                    )
            );
        // Initial state after barcode scan
        } else if (movies.size() == 0) {
            BarcodeSearchViewHolder viewHolder;
            if (convertView == null || convertView.getId() != R.id.barcode_searching) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.barcode_searching_list_item,
                        parent,
                        false
                );

                viewHolder = new BarcodeSearchViewHolder(convertView);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (BarcodeSearchViewHolder) convertView.getTag();
            }

            viewHolder.barcodeText.setText(barcode);

            Log.d("TEST", "view " + convertView.getClass() + " " + convertView.getId());
        // When single result
        } else if (movies.size() == 1) {
            MoviesListItemAdapter.MovieItemViewHolder viewHolder;
            if (convertView == null || convertView.getId() != R.id.movie_list_item) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.movie_list_item,
                        parent,
                        false
                );

                viewHolder = new MoviesListItemAdapter.MovieItemViewHolder(convertView);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (MoviesListItemAdapter.MovieItemViewHolder) convertView.getTag();
            }

            Movie movie = movies.get(0);
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
                                        convertView.getContext().getFilesDir(),
                                        movie.getCover()
                                )
                        )
                ));
            } catch (FileNotFoundException e) {
                viewHolder.moviePoster.setImageResource(R.drawable.no_image);

                e.printStackTrace();
            }
        }

        return convertView;
    }
}
