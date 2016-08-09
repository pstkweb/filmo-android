package com.sixfingers.filmo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.webkit.URLUtil;


import com.sixfingers.filmo.adapter.MoviesGridItemAdapter;
import com.sixfingers.filmo.decoration.GridSpacingDecoration;
import com.sixfingers.filmo.dvdfrapi.DVDFrService;
import com.sixfingers.filmo.dvdfrapi.models.DVDResult;
import com.sixfingers.filmo.dvdfrapi.models.SearchResult;
import com.sixfingers.filmo.dvdfrapi.models.SupportType;
import com.sixfingers.filmo.helper.MoviesDatabaseHelper;
import com.sixfingers.filmo.model.Collection;
import com.sixfingers.filmo.model.CollectionMovie;
import com.sixfingers.filmo.model.Movie;
import com.sixfingers.filmo.ormlite.OrmLiteAppCompatActivity;
import com.sixfingers.filmo.ormlite.QueriesRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MovieListActivity extends OrmLiteAppCompatActivity<MoviesDatabaseHelper> {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private QueriesRepository queriesRepository;
    private MoviesGridItemAdapter gridListAdapter;

    private class SearchTask extends AsyncTask<String, Void, SearchResult> {
        @Override
        protected SearchResult doInBackground(String... params) {
            DVDFrService service = new Retrofit.Builder()
                    .baseUrl(DVDFrService.ENDPOINT)
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .build()
                    .create(DVDFrService.class);

            try {
                SearchResult searchResult = service.searchByTitle(
                        params[0],
                        SupportType.ALL
                ).execute().body();

                if (searchResult != null) {
                    for (DVDResult dvd : searchResult.getDVDs()) {
                        Movie movie = dvd.toMovie();
                        Movie existing = getHelper().getMovieDao().queryForId(movie.getId());
                        if (existing == null) {
                            getHelper().getMovieDao().create(movie);
                        } else {
                            movie = existing;
                        }

                        if (URLUtil.isValidUrl(movie.getCover())) {
                            // Download image
                            String file = movie.getId() + ".png";
                            File fileDest = new File(getFilesDir(), file);
                            try {
                                InputStream in = new URL(movie.getCover()).openStream();
                                Bitmap img = BitmapFactory.decodeStream(in);

                                FileOutputStream fOut = new FileOutputStream(fileDest);
                                img.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                fOut.flush();
                                fOut.close();

                                movie.setCover(file);
                                getHelper().getMovieDao().update(movie);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        Collection collected = queriesRepository.getCollectionByName(Collection.COLLECTED);
                        CollectionMovie relation = new CollectionMovie(
                                collected,
                                movie
                        );
                        getHelper().getCollectionMovieDao().createIfNotExists(relation);
                    }
                }

                return searchResult;
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(SearchResult searchResult) {
            super.onPostExecute(searchResult);

            displayGridItems();
        }
    }

    public void displayGridItems() {
        try {
            displayGridItems(queriesRepository.getMoviesCollection(
                    queriesRepository.getCollectionByName(Collection.COLLECTED)
            ));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayGridItems(java.util.Collection<CollectionMovie> movies) {
        for (CollectionMovie movie : movies) {
            try {
                getHelper().getMovieDao().refresh(movie.getMovie());
            } catch (SQLException e) {
                e.printStackTrace();
            }

            gridListAdapter.addItem(movie, gridListAdapter.getItemCount());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        View recyclerView = findViewById(R.id.movie_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
        }

        //deleteDatabase(MoviesDatabaseHelper.DATABASE_NAME);
        queriesRepository = new QueriesRepository(this);
        try {
            List<CollectionMovie> movies = queriesRepository.getMoviesCollection(
                    queriesRepository.getCollectionByName(Collection.COLLECTED)
            );
            if (movies.size() == 0) {
                new SearchTask().execute("Batman");
            } else {
                this.displayGridItems(movies);
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

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(
                new GridSpacingDecoration(
                        2,
                        getResources().getDimensionPixelSize(R.dimen.grid_spacing),
                        true,
                        0
                )
        );

        gridListAdapter = new MoviesGridItemAdapter(
                new ArrayList<CollectionMovie>(),
                getHelper(),
                mTwoPane
        );
        gridListAdapter.setHasStableIds(true);

        recyclerView.setAdapter(gridListAdapter);
    }
}
