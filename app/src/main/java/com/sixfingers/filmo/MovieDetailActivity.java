package com.sixfingers.filmo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;

import com.sixfingers.filmo.helper.MoviesDatabaseHelper;
import com.sixfingers.filmo.model.Collection;
import com.sixfingers.filmo.model.Movie;
import com.sixfingers.filmo.ormlite.OrmLiteAppCompatActivity;
import com.sixfingers.filmo.ormlite.QueriesRepository;

import java.sql.SQLException;

/**
 * An activity representing a single Movie detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link MovieListActivity}.
 */
public class MovieDetailActivity extends OrmLiteAppCompatActivity<MoviesDatabaseHelper> {
    private QueriesRepository queriesRepository;

    public void addMovieToWishes(View view) {
        try {
            addMovieTo(Collection.WISHES);

            FloatingActionButton button = (FloatingActionButton) findViewById(
                    R.id.fab_menu_wish
            );

            button.setEnabled(false);
            button.setClickable(false);
            button.setImageResource(R.drawable.ic_favorite_white_48dp);
        } catch (Exception e) {
            Snackbar.make(
                    findViewById(android.R.id.content),
                    e.getMessage(),
                    Snackbar.LENGTH_SHORT
            ).show();
        }
    }

    public void addMovieToCollected(View view) {
        try {
            addMovieTo(Collection.COLLECTED);

            FloatingActionButton button = (FloatingActionButton) findViewById(
                    R.id.fab_menu_collected
            );

            button.setEnabled(false);
            button.setClickable(false);
        } catch (Exception e) {
            Snackbar.make(
                    findViewById(android.R.id.content),
                    e.getMessage(),
                    Snackbar.LENGTH_SHORT
            ).show();
        }
    }

    private int addMovieTo(String collectionName) throws SQLException {
        return queriesRepository.addMovieToCollection(
                queriesRepository.getCollectionByName(collectionName),
                getHelper().getMovieDao().queryForId(
                        getIntent().getLongExtra(MovieDetailFragment.ARG_MOVIE_ID, 0)
                )
        );
    }

    private Intent getParentActivity() {
        String className = getIntent().getExtras().getString(
                MovieDetailFragment.ARG_BACK_ACTIVITY,
                MovieListActivity.class.getCanonicalName()
        );

        Intent i;
        try {
            i = new Intent(this, Class.forName(className));
        } catch (ClassNotFoundException e) {
            i = new Intent(this, MovieListActivity.class);
        }
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        try {
            Movie movie = getHelper().getMovieDao().queryForId(
                    getIntent().getLongExtra(MovieDetailFragment.ARG_MOVIE_ID, 0)
            );

            queriesRepository = new QueriesRepository(this);
            if (queriesRepository.isMovieInCollection(
                    queriesRepository.getCollectionByName(Collection.COLLECTED),
                    movie
            )) {
                FloatingActionButton button = (FloatingActionButton) findViewById(
                        R.id.fab_menu_collected
                );

                button.setEnabled(false);
                button.setClickable(false);
            }

            if (queriesRepository.isMovieInCollection(
                    queriesRepository.getCollectionByName(Collection.WISHES),
                    movie
            )) {
                FloatingActionButton button = (FloatingActionButton) findViewById(
                        R.id.fab_menu_wish
                );

                button.setEnabled(false);
                button.setClickable(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putLong(
                    MovieDetailFragment.ARG_MOVIE_ID,
                    getIntent().getLongExtra(MovieDetailFragment.ARG_MOVIE_ID, 0)
            );
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, getParentActivity());

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
