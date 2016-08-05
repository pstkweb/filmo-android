package com.sixfingers.filmo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sixfingers.filmo.R;
import com.sixfingers.filmo.model.Movie;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class MoviesGridItemAdapter extends RecyclerView.Adapter<MoviesGridItemAdapter.MovieGridItemViewHolder> {
    private ArrayList<Movie> data;

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

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView view;
        Context context;

        public DownloadImageTask(ImageView v, Context c) {
            view = v;
            context = c;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            String filename = urls[1] + ".png";

            File fileDest = new File(context.getFilesDir(), filename);
            Bitmap img = null;
            try {
                InputStream in = new URL(url).openStream();
                img = BitmapFactory.decodeStream(in);

                FileOutputStream fOut = new FileOutputStream(fileDest);
                img.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush();
                fOut.close();
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

    public MoviesGridItemAdapter(ArrayList<Movie> dataSet) {
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
        holder.title.setText(data.get(position).getTitre());
        new DownloadImageTask(holder.image, holder.image.getContext()).execute(
                data.get(position).getCover(),
                data.get(position).getId().toString()
        );

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
