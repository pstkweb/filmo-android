package com.sixfingers.filmo.dvdfrapi.models;

import com.sixfingers.filmo.model.Movie;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import java.util.List;

@Root
public class DVDResult {
    @Element(name = "id")
    private int id;
    @Element(name = "media")
    private String media;
    @Element(name = "cover")
    private String cover;
    @Path("titres")
    @Element(name = "fr")
    private String titre;
    @Path("titres")
    @Element(name = "vo", required = false)
    private String titreVO;
    @Path("titres")
    @Element(name = "alternatif", required = false)
    private String titreAlt;
    @Path("titres")
    @Element(name = "alternatif_vo", required = false)
    private String titreVOAlt;
    @Element(name = "annee", required = false)
    private int annee;
    @Element(name = "editeur")
    private String editeur;
    @Element(name = "edition", required = false)
    private String edition;
    @ElementList(name = "stars")
    private List<Person> stars;

    public DVDResult() {}

    public int getId() {
        return id;
    }

    public SupportType getMedia() {
        return SupportType.valueOf(media);
    }

    public String getCover() {
        return cover;
    }

    public String getTitre() {
        return titre;
    }

    public String getTitreVO() {
        return titreVO;
    }

    public String getTitreAlt() {
        return titreAlt;
    }

    public String getTitreVOAlt() {
        return titreVOAlt;
    }

    public int getAnnee() {
        return annee;
    }

    public String getEditeur() {
        return editeur;
    }

    public String getEdition() {
        return edition;
    }

    public List<Person> getStars() {
        return stars;
    }

    public Movie toMovie() {
        return new Movie(
                (long) id,
                media,
                cover,
                titre,
                titreVO,
                titreAlt,
                titreVOAlt,
                annee,
                editeur,
                edition
        );
    }

    @Override
    public String toString() {
        return titre;
    }
}

@Root
class Person {
    @Attribute(name = "type")
    private String role;
    @Attribute(name = "id")
    private int id;
    @Text
    private String name;

    public Person() {}

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
