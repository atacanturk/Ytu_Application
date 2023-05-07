package com.example.ytuapplication.models;

import android.net.Uri;

public class UserPreview {
    private String fullName;
    private String email;
    private Uri profilePhoto;

    public UserPreview() {
    }

    public UserPreview(String fullName, String email, Uri profilePhoto) {
        this.fullName = fullName;
        this.email = email;
        this.profilePhoto = profilePhoto;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Uri getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(Uri profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}

