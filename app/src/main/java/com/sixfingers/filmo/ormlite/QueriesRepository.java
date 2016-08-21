package com.sixfingers.filmo.ormlite;

import android.content.Context;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.sixfingers.filmo.helper.MoviesDatabaseHelper;
import com.sixfingers.filmo.model.Collection;
import com.sixfingers.filmo.model.CollectionMovie;
import com.sixfingers.filmo.model.Movie;

import java.sql.SQLException;
import java.util.List;

public class QueriesRepository {
    private PreparedQuery<CollectionMovie> moviesForCollectionQuery;
    private PreparedQuery<CollectionMovie> movieInCollectionQuery;
    private PreparedQuery<Collection> collectionByNameQuery;
    private MoviesDatabaseHelper helper;

    public QueriesRepository(Context context) {
        helper = new MoviesDatabaseHelper(context);

        try {
            moviesForCollectionQuery = moviesForCollectionQuery();
            collectionByNameQuery = collectionByNameQuery();
            movieInCollectionQuery = movieInCollectionQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Collection getCollectionByName(String name) throws SQLException {
        collectionByNameQuery.setArgumentHolderValue(0, name);

        return helper.getCollectionDao().queryForFirst(collectionByNameQuery);
    }

    public List<CollectionMovie> getMoviesCollection(Collection collection) throws SQLException {
        moviesForCollectionQuery.setArgumentHolderValue(0, collection);

        return helper.getCollectionMovieDao().query(moviesForCollectionQuery);
    }

    public int addMovieToCollection(Collection collection, Movie movie) throws SQLException {
        return helper.getCollectionMovieDao().create(new CollectionMovie(collection, movie));
    }

    public boolean isMovieInCollection(Collection collection, Movie movie) throws SQLException {
        movieInCollectionQuery.setArgumentHolderValue(0, collection.getId());
        movieInCollectionQuery.setArgumentHolderValue(1, movie.getId());

        return helper.getCollectionMovieDao().queryForFirst(movieInCollectionQuery) != null;
    }

    private PreparedQuery<Collection> collectionByNameQuery() throws SQLException {
        QueryBuilder<Collection, Long> collectQb = helper.getCollectionDao().queryBuilder();
        collectQb.where().eq(Collection.NAME, new SelectArg());

        return collectQb.prepare();
    }

    private PreparedQuery<CollectionMovie> moviesForCollectionQuery() throws SQLException {
        QueryBuilder<CollectionMovie, Long> collectQB = helper.getCollectionMovieDao()
                .queryBuilder();
        collectQB.selectColumns(CollectionMovie.MOVIE_ID);
        collectQB.where().eq(CollectionMovie.COLLECTION_ID, new SelectArg());

        return collectQB.prepare();
    }

    private PreparedQuery<CollectionMovie> movieInCollectionQuery() throws SQLException {
        QueryBuilder<CollectionMovie, Long> collectQB = helper.getCollectionMovieDao()
                .queryBuilder();
        collectQB.where().eq(CollectionMovie.COLLECTION_ID, new SelectArg())
                .and().eq(CollectionMovie.MOVIE_ID, new SelectArg());

        return collectQB.prepare();
    }
}
