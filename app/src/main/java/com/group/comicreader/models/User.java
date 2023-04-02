package com.group.comicreader.models;

import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public class User {
    private String email;
    private String username;
    private List<DocumentReference> favorites;
    private List<DocumentReference> reads;

    // Constructors
    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String username, List<DocumentReference> favorites, List<DocumentReference> reads) {
        this.email = email;
        this.username = username;
        this.favorites = favorites;
        this.reads = reads;
    }

    // Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
