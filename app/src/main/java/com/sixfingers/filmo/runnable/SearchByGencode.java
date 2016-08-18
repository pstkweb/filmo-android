package com.sixfingers.filmo.runnable;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.URLUtil;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.sixfingers.filmo.adapter.BarcodeListItemAdapter;
import com.sixfingers.filmo.dvdfrapi.APIHandler;
import com.sixfingers.filmo.dvdfrapi.models.DVDResult;
import com.sixfingers.filmo.dvdfrapi.models.SearchResult;
import com.sixfingers.filmo.dvdfrapi.models.SupportType;
import com.sixfingers.filmo.helper.MoviesDatabaseHelper;
import com.sixfingers.filmo.model.Movie;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

public class SearchByGencode extends AsyncTask<String, Void, ArrayList<Movie>> {
    private MoviesDatabaseHelper helper;
    private Activity context;
    private BarcodeListItemAdapter adapter;
    private String barcode;

    public SearchByGencode(Activity ctx, BarcodeListItemAdapter listAdapter) {
        context = ctx;
        adapter = listAdapter;
        helper = OpenHelperManager.getHelper(context, MoviesDatabaseHelper.class);
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {
        ArrayList<Movie> result = new ArrayList<>();
        try {
            barcode = params[0];

            APIHandler handler = new APIHandler();
            SearchResult searchResult = handler.searchByGencode(
                    barcode,
                    SupportType.valueOf(params[1])
            );

            if (searchResult != null) {
                if (searchResult.getDVDs() != null) {
                    for (DVDResult dvd : searchResult.getDVDs()) {
                        Movie movie = dvd.toMovie();
                        Movie existing = helper.getMovieDao().queryForId(movie.getId());
                        if (existing == null) {
                            helper.getMovieDao().create(movie);
                        } else {
                            movie = existing;
                        }

                        if (URLUtil.isValidUrl(movie.getCover())) {
                            // Download image
                            String file = movie.getId() + ".png";
                            File fileDest = new File(context.getFilesDir(), file);
                            try {
                                InputStream in = new URL(movie.getCover()).openStream();
                                Bitmap img = BitmapFactory.decodeStream(in);

                                FileOutputStream fOut = new FileOutputStream(fileDest);
                                img.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                fOut.flush();
                                fOut.close();

                                movie.setCover(file);
                                helper.getMovieDao().update(movie);

                                result.add(movie);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            result.add(movie);
                        }
                    }
                }
            }

            return result;
        } catch (SQLException e) {
            // TODO : Handle exception
            Log.d("TEST", "SQLException " + e.getMessage());
        } catch (IOException e) {
            // TODO : Handle exception
            Log.d("TEST", "IOException on API request " + e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        if (movies == null || movies.size() == 0) {
            adapter.updateItem(barcode, null);
        } else {
            adapter.updateItem(barcode, movies);
        }
    }
}
