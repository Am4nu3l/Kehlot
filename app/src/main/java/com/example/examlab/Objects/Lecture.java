package com.example.examlab.Objects;

import android.net.Uri;

public class Lecture {
    String fullName;
    String faceBook;
    String instaGram;
    Uri image;
    String description;

    public Lecture(String fullName, String faceBook, String instaGram, Uri image, String description) {
        this.fullName = fullName;
        this.faceBook = faceBook;
        this.instaGram = instaGram;
        this.image = image;
        this.description = description;
    }

    public String getFullName() {
        return fullName;
    }

    public String getFaceBook() {
        return faceBook;
    }

    public String getInstaGram() {
        return instaGram;
    }

    public Uri getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }
}
