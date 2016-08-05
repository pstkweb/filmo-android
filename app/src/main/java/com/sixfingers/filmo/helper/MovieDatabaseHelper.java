package com.sixfingers.filmo.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.sixfingers.filmo.model.Movie;

import java.sql.SQLException;

public class MovieDatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "movies";
    private static final int DATABASE_VERSION = 1;

    private Dao<Movie, Long> movieDao;

    public MovieDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Dao<Movie, Long> getDao() throws SQLException {
        if (movieDao == null)  {
            movieDao = getDao(Movie.class);
        }

        return movieDao;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Movie.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Movie.class, false);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
