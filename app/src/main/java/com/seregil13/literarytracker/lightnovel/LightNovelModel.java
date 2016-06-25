package com.seregil13.literarytracker.lightnovel;

import java.util.List;

/**
 * A class for holding a light novel's details
 */
public class LightNovelModel {

    private int id;
    private String title;
    private String author;
    private String description;
    private String completed;
    private String translatorSite;
    private List<String> genres;

    LightNovelModel(int id, String title, String author, String description, String completed, String translatorSite, List<String> genres) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.completed = completed;
        this.translatorSite = translatorSite;
        this.genres = genres;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getTranslatorSite() {
        return translatorSite;
    }

    public void setTranslatorSite(String translatorSite) {
        this.translatorSite = translatorSite;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
