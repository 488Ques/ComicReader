package com.group.comicreader.models;

public class ComicListItem {
    private String id;
    private String title;
    private String imageUrl;

    public ComicListItem(String id, String title, String imageUrl) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getId() {
        return id;
    }
}
