package com.sixfingers.filmo.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "collection")
public class Collection {
    public final static String ID = "id";
    public final static String NAME = "name";

    @DatabaseField(generatedId = true, columnName = ID)
    private Long id;
    @DatabaseField(unique = true, columnName = NAME)
    private String name;

    public static final String COLLECTED = "Collected";

    public Collection() {}

    public Collection(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Collec[Id=" + getId() + ",Name=" + getName() + "]";
    }
}
