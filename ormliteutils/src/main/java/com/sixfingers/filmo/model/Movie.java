package com.sixfingers.filmo.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "movie")
public class Movie {
    public final static String ID = "id";
    public final static String MEDIA = "media";
    public final static String COVER = "cover";
    public final static String TITRE = "titre";
    public final static String ANNEE = "annee";
    public final static String EDITEUR = "editeur";
    public final static String EDITION = "edition";

    @DatabaseField(id = true, columnName = ID)
    private Long id;
    @DatabaseField(columnName = MEDIA)
    private String media;
    @DatabaseField(columnName = COVER)
    private String cover;
    @DatabaseField(columnName = TITRE)
    private String titre;
    @DatabaseField
    private String titreVO;
    @DatabaseField
    private String titreAlt;
    @DatabaseField
    private String titreVOAlt;
    @DatabaseField(columnName = ANNEE)
    private int annee;
    @DatabaseField(columnName = EDITEUR)
    private String editeur;
    @DatabaseField(columnName = EDITION)
    private String edition;

    public Movie() {}

    public Movie(
            Long id, String media, String cover, String titre, String titreVO, String titreAlt,
            String titreVOAlt, int annee, String editeur, String edition
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
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitre() {
        if (titre != null && titre.length() > 0) {
            return titre;
        } else if (titreAlt != null && titreAlt.length() > 0) {
            return titreAlt;
        } else if (titreVO != null && titreVO.length() > 0) {
            return titreVO;
        } else if (titreVOAlt != null && titreVOAlt.length() > 0) {
            return titreVOAlt;
        }

        return "";
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getTitreVO() {
        return titreVO;
    }

    public void setTitreVO(String titreVO) {
        this.titreVO = titreVO;
    }

    public String getTitreAlt() {
        return titreAlt;
    }

    public void setTitreAlt(String titreAlt) {
        this.titreAlt = titreAlt;
    }

    public String getTitreVOAlt() {
        return titreVOAlt;
    }

    public void setTitreVOAlt(String titreVOAlt) {
        this.titreVOAlt = titreVOAlt;
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public String getEditeur() {
        return editeur;
    }

    public void setEditeur(String editeur) {
        this.editeur = editeur;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }
}
