package com.sixfingers.filmo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.sixfingers.filmo.adapter.MoviesGridItemAdapter;
import com.sixfingers.filmo.decoration.GridSpacingDecoration;
import com.sixfingers.filmo.dvdfrapi.DVDFrService;
import com.sixfingers.filmo.dvdfrapi.models.DVDResult;
import com.sixfingers.filmo.dvdfrapi.models.SearchResult;
import com.sixfingers.filmo.dvdfrapi.models.SupportType;
import com.sixfingers.filmo.helper.MovieDatabaseHelper;
import com.sixfingers.filmo.model.Movie;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class MainActivity extends AppCompatActivity {
    private MoviesGridItemAdapter gridListAdapter;
    private MovieDatabaseHelper dbHelper;

    private class SearchTask extends AsyncTask<String, Void, SearchResult> {
        @Override
        protected SearchResult doInBackground(String... params) {
            DVDFrService service = new Retrofit.Builder()
                    .baseUrl(DVDFrService.ENDPOINT)
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .build()
                    .create(DVDFrService.class);

            try {
                return service.searchByTitle(params[0], SupportType.ALL).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(SearchResult searchResult) {
            super.onPostExecute(searchResult);

            try {
                Dao<Movie, Long> movieDao = dbHelper.getDao();
                for (DVDResult dvd : searchResult.getDVDs()) {
                    movieDao.create(dvd.toMovie());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            afficherCards();
        }
    }

    public void afficherCards() {
        try {
            Dao<Movie, Long> movieDao = dbHelper.getDao();
            for (Movie movie : movieDao.queryForAll()) {
                gridListAdapter.addItem(movie, gridListAdapter.getItemCount());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(getResources().getString(R.string.menu_title));
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        RecyclerView gridList = (RecyclerView) findViewById(R.id.grid_recycler);
        gridList.setHasFixedSize(true);
        gridList.setLayoutManager(new GridLayoutManager(this, 2));
        gridList.addItemDecoration(
                new GridSpacingDecoration(
                        2,
                        getResources().getDimensionPixelSize(R.dimen.grid_spacing),
                        true,
                        0
                )
        );

        gridListAdapter = new MoviesGridItemAdapter(new ArrayList<Movie>());
        gridListAdapter.setHasStableIds(true);
        gridList.setAdapter(gridListAdapter);

        dbHelper = OpenHelperManager.getHelper(this, MovieDatabaseHelper.class);

        try {
            List<Movie> movies = dbHelper.getDao().queryForAll();

            if (movies.size() == 0) {
                new SearchTask().execute("Batman");
            } else {
                Log.d("TEST", "Get from DB");
                afficherCards();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);

        return true;
    }
}
