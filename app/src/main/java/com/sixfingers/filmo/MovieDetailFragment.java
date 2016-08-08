package com.sixfingers.filmo;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.sixfingers.filmo.helper.MoviesDatabaseHelper;
import com.sixfingers.filmo.model.Movie;

import java.sql.SQLException;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_MOVIE_ID = "movie_id";

    private MoviesDatabaseHelper helper;
    private Movie movie;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    protected MoviesDatabaseHelper getHelper() {
        if (helper == null) {
            helper = OpenHelperManager.getHelper(getActivity(), MoviesDatabaseHelper.class);
        }

        return helper;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_MOVIE_ID)) {
            // In a real-world scenario, use a Loader
            // to load content from a content provider.
            try {
                movie = getHelper().getMovieDao().queryForId(getArguments().getLong(ARG_MOVIE_ID));
                Log.d("TEST", movie.getTitre());
            } catch (SQLException e) {
                e.printStackTrace();
            }

            Activity activity = getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(movie != null ? movie.getTitre() : "");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);

        // Show the content as text in a TextView.
        if (movie != null) {
            ((TextView) rootView.findViewById(R.id.movie_detail)).setText(movie.getEditeur());
        }

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (helper != null) {
            OpenHelperManager.releaseHelper();
            helper = null;
        }
    }
}
