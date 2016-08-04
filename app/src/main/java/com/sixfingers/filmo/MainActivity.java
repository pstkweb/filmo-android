package com.sixfingers.filmo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sixfingers.filmo.adapter.MoviesCardAdapter;
import com.sixfingers.filmo.dvdfrapi.DVDFrService;
import com.sixfingers.filmo.dvdfrapi.models.DVDResult;
import com.sixfingers.filmo.dvdfrapi.models.SearchResult;
import com.sixfingers.filmo.dvdfrapi.models.SupportType;
import com.sixfingers.filmo.model.Movie;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class MainActivity extends AppCompatActivity {
    private MoviesCardAdapter cardsListAdapter;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView cardsList = (RecyclerView) findViewById(R.id.cards_recycler);
        cardsList.setHasFixedSize(true);
        cardsList.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );

        cardsListAdapter = new MoviesCardAdapter(new ArrayList<Movie>());
        cardsListAdapter.setHasStableIds(true);
        cardsList.setAdapter(cardsListAdapter);

        //new SearchTask().execute("Batman");
    }

    public void afficherCards(SearchResult result) {
        for (DVDResult dvd : result.getDVDs()) {
            cardsListAdapter.addItem(dvd.toMovie(), cardsListAdapter.getItemCount());
        }
    }
}
