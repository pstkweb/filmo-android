package com.sixfingers.filmo.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.sixfingers.filmo.R;
import com.sixfingers.filmo.model.Movie;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class MoviesCardAdapter extends RecyclerView.Adapter<MoviesCardAdapter.MovieCardViewHolder> {
    private ArrayList<Movie> data;

    public static class MovieCardViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;

        public MovieCardViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.movie_title);
            image = (ImageView) itemView.findViewById(R.id.movie_image);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView view;

        public DownloadImageTask(ImageView v) {
            view = v;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap img = null;

            try {
                InputStream in = new URL(url).openStream();
                img = BitmapFactory.decodeStream(in);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return img;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            view.setImageBitmap(bitmap);
        }
    }

    public MoviesCardAdapter(ArrayList<Movie> dataSet) {
        data = dataSet;
    }

    public void addItem(Movie movie, int index) {
        data.add(index, movie);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        data.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public MovieCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.movie_card,
                parent,
                false
        );

        return new MovieCardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MovieCardViewHolder holder, int position) {
        holder.title.setText(data.get(position).getTitre());
        new DownloadImageTask(holder.image).execute(data.get(position).getCover());
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
