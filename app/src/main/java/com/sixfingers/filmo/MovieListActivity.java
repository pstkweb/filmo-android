package com.sixfingers.filmo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.sixfingers.filmo.adapter.MoviesGridItemAdapter;
import com.sixfingers.filmo.decoration.GridSpacingDecoration;
import com.sixfingers.filmo.helper.MoviesDatabaseHelper;
import com.sixfingers.filmo.model.Collection;
import com.sixfingers.filmo.model.CollectionMovie;
import com.sixfingers.filmo.ormlite.OrmLiteAppCompatActivity;
import com.sixfingers.filmo.ormlite.QueriesRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    private MoviesGridItemAdapter gridListAdapter;

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
        QueriesRepository queriesRepository = new QueriesRepository(this);
        try {
            List<CollectionMovie> movies = queriesRepository.getMoviesCollection(
                    queriesRepository.getCollectionByName(Collection.COLLECTED)
            );

            displayGridItems(movies);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);

        return true;
    }

    public void displayGridItems(java.util.Collection<CollectionMovie> movies) {
        if (movies.size() > 0) {
            findViewById(android.R.id.empty).setVisibility(View.GONE);
        }

        for (CollectionMovie movie : movies) {
            try {
                getHelper().getMovieDao().refresh(movie.getMovie());
            } catch (SQLException e) {
                e.printStackTrace();
            }

            gridListAdapter.addItem(movie, gridListAdapter.getItemCount());
        }
    }

    public void startSearchByTitle(View view) {
        Intent intent = new Intent(this, SearchByInputActivity.class);
        startActivity(intent);
    }

    public void startSearchByScan(View view) {
        Intent intent = new Intent(this, SearchByScanActivity.class);
        startActivity(intent);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
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
