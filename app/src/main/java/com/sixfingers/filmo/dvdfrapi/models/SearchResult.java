package com.sixfingers.filmo.dvdfrapi.models;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root(name = "dvds", strict = false)
public class SearchResult {
    @ElementList(entry = "dvd", inline = true, required = false, empty = true)
    private List<DVDResult> dvds = new ArrayList<>();

    public SearchResult() {}

    public List<DVDResult> getDVDs() {
        return dvds;
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