package com.group.comicreader.models;

import java.util.ArrayList;
import java.util.List;

public class Comic {
    private String title;
    private String author;
    private String description;
    private ArrayList<String> genres;
    private String imageUrl; // The URL of the cover image for the comic
    private List<Chapter> chapters;

    public Comic() {
        // Default constructor required for calls to DataSnapshot.getValue(Comic.class)
    }

    public Comic(String title, String author, String description, ArrayList<String> genres, String imageUrl, List<Chapter> chapters) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.genres = genres;
        this.imageUrl = imageUrl;
        this.chapters = chapters;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
