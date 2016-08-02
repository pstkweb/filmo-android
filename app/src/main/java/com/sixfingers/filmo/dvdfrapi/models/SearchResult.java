package com.sixfingers.filmo.dvdfrapi.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import java.util.List;

@Root(name = "dvds")
public class SearchResult {
    @Attribute(name = "generator")
    private String generator;
    @ElementList(inline = true)
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
    private int id;
    @Element(name = "media")
    private String media;
    @Element(name = "cover")
    private String cover;
    @Path("titres")
    @Element(name = "fr")
    private String titre;
    @Path("titres")
    @Element(name = "vo")
    private String titreVO;
    @Path("titres")
    @Element(name = "alternatif", required = false)
    private String titreAlt;
    @Path("titres")
    @Element(name = "alternatif_vo", required = false)
    private String titreVOAlt;
    @Element(name = "annee")
    private int annee;
    @Element(name = "editeur")
    private String editeur;
    @Element(name = "edition", required = false)
    private String edition;
    @ElementList(name = "stars")
    private List<Person> stars;

    public DVDResult(
            @Element(name = "id") int id,
            @Element(name = "media") String media,
            @Element(name = "cover") String cover,
            @Element(name = "fr") String titre,
            @Element(name = "vo") String titreVO,
            @Element(name = "alternatif") String titreAlt,
            @Element(name = "alternatif_vo") String titreVOAlt,
            @Element(name = "annee") int annee,
            @Element(name = "editeur") String editeur,
            @Element(name = "edition") String edition,
            @ElementList(name = "stars") List<Person> stars
    ) {
        this.id = id;
        this.media = media;
        this.cover = cover;
        this.titre = titre;
        this.titreVO = titreVO;
        this.titreAlt = titreAlt;
        this.titreVOAlt = titreVOAlt;
        this.annee = annee;
        this.editeur = editeur;
        this.edition = edition;
        this.stars = stars;
    }

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

    @Override
    public String toString() {
        return titre;
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