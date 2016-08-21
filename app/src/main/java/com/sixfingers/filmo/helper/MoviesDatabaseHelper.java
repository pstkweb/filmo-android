package com.sixfingers.filmo.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.sixfingers.filmo.model.Collection;
import com.sixfingers.filmo.model.CollectionMovie;
import com.sixfingers.filmo.model.Movie;

import java.sql.SQLException;

public class MoviesDatabaseHelper extends OrmLiteSqliteOpenHelper {
    public static final String DATABASE_NAME = "movies";
    public static final int DATABASE_VERSION = 5;

    private Dao<Movie, Long> movieDao;
    private Dao<Collection, Long> collectionDao;
    private Dao<CollectionMovie, Long> collectionMovieDao;
    private final Class<?>[] classes = new Class[]{
            Movie.class, Collection.class, CollectionMovie.class
    };

    public MoviesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Dao<Movie, Long> getMovieDao() throws SQLException {
        if (movieDao == null)  {
            movieDao = getDao(Movie.class);
        }

        return movieDao;
    }

    public Dao<Collection, Long> getCollectionDao() throws SQLException {
        if (collectionDao == null) {
            collectionDao = getDao(Collection.class);
        }

        return collectionDao;
    }

    public Dao<CollectionMovie, Long> getCollectionMovieDao() throws SQLException {
        if (collectionMovieDao == null) {
            collectionMovieDao = getDao(CollectionMovie.class);
        }

        return collectionMovieDao;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            for (Class c : classes) {
                TableUtils.createTable(connectionSource, c);
            }

            for (String name : Collection.DEFAULT_COLLECTIONS) {
                Collection collected = new Collection(name);
                getCollectionDao().create(collected);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion,
            int newVersion
    ) {
        try {
            for (Class c : classes) {
                TableUtils.dropTable(connectionSource, c, false);
            }

            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
