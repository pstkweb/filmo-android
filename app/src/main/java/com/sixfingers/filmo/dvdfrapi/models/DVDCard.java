package com.sixfingers.filmo.dvdfrapi.models;

import com.sixfingers.filmo.model.Movie;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

@Root(name = "dvd", strict = false)
public class DVDCard {
    @Element(name = "id")
    private int id;
    @Element(name = "cover")
    private String cover;
    @Element(name = "media")
    private String media;
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
    @Element(name = "editeur")
    private String editeur;
    @Element(name = "edition", required = false)
    private String edition;
    @Element(name = "distributeur", required = false)
    private String distributeur;
    @Element(name = "studio", required = false)
    private String studio;
    @Element(name = "annee", required = false)
    private int annee;
    @Element(name = "synopsis")
    private String synopsis;
    @Element(name = "duree")
    private int duree;

    public DVDCard() {}

    public int getId() {
        return id;
    }

    public String getCover() {
        return cover;
    }

    public String getMedia() {
        return media;
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

    public String getEditeur() {
        return editeur;
    }

    public String getEdition() {
        return edition;
    }

    public String getDistributeur() {
        return distributeur;
    }

    public String getStudio() {
        return studio;
    }

    public int getAnnee() {
        return annee;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public int getDuree() {
        return duree;
    }

    public Movie toMovie() {
        Movie movie = new Movie(
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

        movie.setDistributeur(distributeur);
        movie.setStudio(studio);
        movie.setDuree(duree);
        movie.setSynopsis(synopsis);

        return movie;
    }
}
