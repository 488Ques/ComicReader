package com.group.comicreader.models;

public class ComicListItem {
    private String title;
    private String imageUrl;

    public ComicListItem(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
