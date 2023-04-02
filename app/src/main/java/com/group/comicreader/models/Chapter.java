package com.group.comicreader.models;

import java.util.List;

public class Chapter {
    private int chapterNumber;
    private String title;
    private List<String> imageUrls;

    // Constructors
    public Chapter() {
        // Default constructor required for calls to DataSnapshot.getValue(Chapter.class)
    }

    public Chapter(int chapterNumber, String title, List<String> imageUrls) {
        this.chapterNumber = chapterNumber;
        this.title = title;
        this.imageUrls = imageUrls;
    }

    // Getters and setters
    public int getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(int chapterNumber) {
        this.chapterNumber = chapterNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
