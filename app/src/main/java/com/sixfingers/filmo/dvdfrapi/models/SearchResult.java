package com.sixfingers.filmo.dvdfrapi.models;

import com.sixfingers.filmo.model.Movie;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Root(name = "dvds")
public class SearchResult {
    @Attribute(name = "generator")
    private String generator;
    @ElementList(inline = true, required = false)
    private List<DVDResult> dvds;

    public SearchResult(
            @Attribute(name = "generator") String generator,
            @ElementList(inline = true) List<DVDResult> dvds
    ) {
        this.generator = generator;
        this.dvds = dvds;
    }

    public List<DVDResult> getDVDs() {
        return dvds;
    }

    public String getGenerator() {
        return generator;
    }

    public ArrayList<Movie> toList() {
        ArrayList<Movie> movies = new ArrayList<>();
        for (DVDResult result : dvds) {
            movies.add(result.toMovie());
        }

        return movies;
    }

    @Override
    public String toString() {
        String res = "DVDs:";
        for (DVDResult dvd : dvds) {
            res += " " + dvd;
        }

        return res;
    }
}

@Root(name = "star")
class Person {
    @Attribute(name = "type")
    private String role;
    @Attribute(name = "id")
    private int id;
    @Text
    private String name;

    public Person(
            @Attribute(name = "type") String role,
            @Attribute(name = "id") int id,
            @Text String name
    ) {
        this.role = role;
        this.id = id;
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " <" + role + ">";
    }
}