package com.sixfingers.filmo.dvdfrapi.models;

import com.sixfingers.filmo.model.Movie;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root
public class SearchResult {
    @Attribute(name = "generator")
    private String generator;
    @ElementList(entry = "dvd", inline = true, required = false, empty = true)
    private List<DVDResult> dvds = new ArrayList<>();

    public SearchResult() {}

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