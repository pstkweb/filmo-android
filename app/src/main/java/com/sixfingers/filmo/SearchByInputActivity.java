package com.sixfingers.filmo;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.sixfingers.filmo.adapter.MoviesListItemAdapter;
import com.sixfingers.filmo.dvdfrapi.models.SupportType;
import com.sixfingers.filmo.model.Movie;
import com.sixfingers.filmo.runnable.SearchByTitle;

import java.util.ArrayList;

public class SearchByInputActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_input);

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.search_view);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(getApplicationContext(), SearchByInputActivity.class)
        ));
        searchView.setIconifiedByDefault(false);

        MoviesListItemAdapter listAdapter = new MoviesListItemAdapter(this, new ArrayList<Movie>());
        setListAdapter(listAdapter);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchView.setQuery(query, false);

            new SearchByTitle(this, (ProgressBar) findViewById(R.id.list_spinner), listAdapter)
                    .execute(query, String.valueOf(SupportType.ALL));
        }
    }
}
