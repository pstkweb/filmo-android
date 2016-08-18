package com.sixfingers.filmo.runnable;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.URLUtil;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.sixfingers.filmo.dvdfrapi.APIHandler;
import com.sixfingers.filmo.dvdfrapi.models.DVDCard;
import com.sixfingers.filmo.helper.MoviesDatabaseHelper;
import com.sixfingers.filmo.model.Movie;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;

public class GetCard extends AsyncTask<Integer, Void, DVDCard> {
    private MoviesDatabaseHelper helper;
    private Activity context;

    public GetCard(Activity ctx) {
        context = ctx;
        helper = OpenHelperManager.getHelper(context, MoviesDatabaseHelper.class);
    }

    @Override
    protected DVDCard doInBackground(Integer... params) {
        try {
            APIHandler handler = new APIHandler();
            DVDCard result = handler.getCard(params[0]);
            if (result != null) {
                Movie movie = result.toMovie();

                helper.getMovieDao().createOrUpdate(movie);

                String file = movie.getId() + ".png";
                File fileDest = new File(context.getFilesDir(), file);
                if (!fileDest.exists() && URLUtil.isValidUrl(movie.getCover())) {
                    try {
                        InputStream in = new URL(movie.getCover()).openStream();
                        Bitmap img = BitmapFactory.decodeStream(in);

                        FileOutputStream fOut = new FileOutputStream(fileDest);
                        img.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                        fOut.flush();
                        fOut.close();

                        movie.setCover(file);
                        helper.getMovieDao().update(movie);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return result;
        } catch (SQLException e) {
            // TODO : Handle exception
            Log.d("TEST", "SQLException " + e.getMessage());
        } catch (IOException e) {
            // TODO : Handle exception
            e.printStackTrace();
        }

        return null;
    }
}
