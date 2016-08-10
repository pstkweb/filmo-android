package com.sixfingers.filmo.runnable;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import com.sixfingers.filmo.dvdfrapi.DVDFrService;
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

import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class SearchByTitle extends AsyncTask<String, Void, ArrayList<Movie>> {
    private MoviesDatabaseHelper helper;
    private ListActivity context;
    private ProgressBar progress;
    private ArrayAdapter<Movie> adapter;

    public SearchByTitle(ListActivity ctx, ProgressBar progressBar, ArrayAdapter<Movie> moviesAdapter) {
        context = ctx;
        progress = progressBar;
        adapter = moviesAdapter;
        helper = OpenHelperManager.getHelper(context, MoviesDatabaseHelper.class);
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {
        ArrayList<Movie> result = new ArrayList<>();
        DVDFrService service = new Retrofit.Builder()
                .baseUrl(DVDFrService.ENDPOINT)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build()
                .create(DVDFrService.class);

        try {
            SearchResult searchResult = service.searchByTitle(
                    params[0],
                    SupportType.valueOf(params[1])
            ).execute().body();

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
                } else {
                    // TODO : no result
                }
            }
        } catch (SQLException e) {
            Log.d("TEST", "SQLException " + e.getMessage());
        } catch (IOException e) {
            Log.d("TEST", "IOException on API request " + e.getMessage());
        }

        return result;
    }

    @Override
    protected void onPreExecute() {
        //progress.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        adapter.addAll(movies);
        adapter.notifyDataSetChanged();

        //progress.setVisibility(View.GONE);
    }
}
