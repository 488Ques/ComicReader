package com.group.comicreader.models;

import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public class User {
    private List<DocumentReference> favorites;
    private List<DocumentReference> reads;

    // Constructors
    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(List<DocumentReference> favorites, List<DocumentReference> reads) {
        this.favorites = favorites;
        this.reads = reads;
    }

    public List<DocumentReference> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<DocumentReference> favorites) {
        this.favorites = favorites;
    }

    public List<DocumentReference> getReads() {
        return reads;
    }

    public void setReads(List<DocumentReference> reads) {
        this.reads = reads;
    }
}
