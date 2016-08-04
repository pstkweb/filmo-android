package com.sixfingers.filmo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.sixfingers.filmo.adapter.MoviesCardAdapter;
import com.sixfingers.filmo.dvdfrapi.DVDFrService;
import com.sixfingers.filmo.dvdfrapi.models.DVDResult;
import com.sixfingers.filmo.dvdfrapi.models.SearchResult;
import com.sixfingers.filmo.dvdfrapi.models.SupportType;
import com.sixfingers.filmo.model.Movie;
import com.sixfingers.ui.floatingactionmenu.FloatingActionMenu;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class MainActivity extends AppCompatActivity {
    private MoviesCardAdapter cardsListAdapter;
    private FloatingActionMenu appFAM;

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

            afficherCards(searchResult);
        }
    }

    public void afficherCards(SearchResult result) {
        for (DVDResult dvd : result.getDVDs()) {
            cardsListAdapter.addItem(dvd.toMovie(), cardsListAdapter.getItemCount());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        appFAM = (FloatingActionMenu) findViewById(R.id.FAM);

        RecyclerView cardsList = (RecyclerView) findViewById(R.id.cards_recycler);
        cardsList.setHasFixedSize(true);
        cardsList.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );

        cardsListAdapter = new MoviesCardAdapter(new ArrayList<Movie>());
        cardsListAdapter.setHasStableIds(true);
        cardsList.setAdapter(cardsListAdapter);

        cardsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    // show FAM

                } else {
                    // hide FAM
                }
            }
        });

        //new SearchTask().execute("Batman");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);

        return true;
    }
}
