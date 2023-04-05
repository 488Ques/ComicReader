package com.group.comicreader.models;

import java.util.List;

public class Comic {
    private String title;
    private String author;
    private String genre;
    private String imageUrl; // The URL of the cover image for the comic
    private int cover;
    private List<Chapter> chapters;

    public Comic() {
        // Default constructor required for calls to DataSnapshot.getValue(Comic.class)
    }

    public Comic(String title, int cover) {
        this.title = title;
        this.cover = cover;
    }

    public Comic(String title, String author, String genre, String imageUrl, List<Chapter> chapters) {
        this.title = title;
        this.author = author;
        this.genre = genre;
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
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

    public int getCover() {
        return cover;
    }

    public void setCover(int cover) {
        this.cover = cover;
    }
}
