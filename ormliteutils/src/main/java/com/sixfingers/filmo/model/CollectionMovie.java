package com.sixfingers.filmo.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "collection_movies")
public class CollectionMovie {
    public final static String ID = "id";
    public final static String COLLECTION_ID = "collection_id";
    public final  static String MOVIE_ID = "movie_id";

    @DatabaseField(generatedId = true, columnName = ID)
    private Long id;
    @DatabaseField(foreign = true, columnName = COLLECTION_ID, uniqueCombo = true)
    private Collection collection;
    @DatabaseField(foreign = true, columnName = MOVIE_ID, uniqueCombo = true)
    private Movie movie;

    public CollectionMovie() {}

    public CollectionMovie(Collection collection, Movie movie) {
        this.collection = collection;
        this.movie = movie;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
