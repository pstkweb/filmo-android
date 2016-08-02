package com.sixfingers.filmo.dvdfrapi.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import java.util.List;

@Root(name = "dvds")
public class SearchResult {
    @Attribute(name = "generator")
    String generator;
    @ElementList(inline = true)
    List<DVDResult> dvds;

    @Override
    public String toString() {
        String res = "DVDs:";
        for (DVDResult dvd : dvds) {
            res += " " + dvd;
        }

        return res;
    }
}

@Root(name = "dvd")
class DVDResult {
    @Element(name = "id")
    int id;
    @Element(name = "media")
    String media;
    @Element(name = "cover")
    String cover;
    @Path("titres")
    @Element(name = "fr")
    String titre;
    @Path("titres")
    @Element(name = "vo")
    String titreVO;
    @Path("titres")
    @Element(name = "alternatif", required = false)
    String titreAlt;
    @Path("titres")
    @Element(name = "alternatif_vo", required = false)
    String titreVOAlt;
    @Element(name = "annee")
    int annee;
    @Element(name = "editeur")
    String editeur;
    @Element(name = "edition", required = false)
    String edition;
    @ElementList(name = "stars")
    List<Person> stars;

    @Override
    public String toString() {
        return titre;
    }

    public SupportType getMedia() {
        return SupportType.valueOf(media);
    }
}

@Root(name = "star")
class Person {
    @Attribute(name = "type")
    String role;
    @Attribute(name = "id")
    int id;
    @Text
    String name;

    @Override
    public String toString() {
        return name + " <" + role + ">";
    }
}