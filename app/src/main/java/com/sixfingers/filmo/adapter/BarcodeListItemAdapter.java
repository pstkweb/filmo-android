package com.sixfingers.filmo.adapter;

import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
    private HashMap<String, List<Movie>> map;
    private String[] keys;

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
            errorMessage.setBackgroundColor(
                    ContextCompat.getColor(itemView.getContext(), android.R.color.holo_red_dark)
            );
        }
    }

    public static class BarcodeFoundViewHolder extends RecyclerView.ViewHolder {
        public BarcodeFoundViewHolder(View itemView) {
            super(itemView);
        }
    }

    public BarcodeListItemAdapter(HashMap<String, List<Movie>> barcodes) {
        map = barcodes;
        keys = barcodes.keySet().toArray(new String[barcodes.size()]);
    }

    public boolean add(String text) {
        if (!map.containsKey(text)) {
            map.put(text, new ArrayList<Movie>());
            keys = map.keySet().toArray(new String[map.size()]);

            notifyDataSetChanged();

            return true;
        }

        return false;
    }

    public void updateItem(String barcode, List<Movie> movies) {
        map.put(barcode, movies);

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return map.size();
    }

    @Override
    public List<Movie> getItem(int i) {
        return map.get(keys[i]);
    }

    @Override
    public long getItemId(int i) {
        return keys[i].hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String barcode = keys[position];
        List<Movie> movies = getItem(position);

        if (movies == null) {
            BarcodeErrorViewHolder viewHolder;
            if (convertView == null || convertView.getId() != R.id.movie_list_item) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.movie_list_item,
                        parent,
                        false
                );

                viewHolder = new BarcodeErrorViewHolder(convertView);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (BarcodeErrorViewHolder) convertView.getTag();
            }

            viewHolder.errorMessage.setText(
                    convertView.getResources().getString(R.string.no_result_search_barcode)
            );
        } else if (movies.size() == 0) {
            BarcodeSearchViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.barcode_searching_list_item,
                        parent,
                        false
                );
            }

            viewHolder = new BarcodeSearchViewHolder(convertView);
            viewHolder.barcodeText.setText(barcode);
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
                // TODO : default image
                e.printStackTrace();
            }
        }

        return convertView;
    }
}
