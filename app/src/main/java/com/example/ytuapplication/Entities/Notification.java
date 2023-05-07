package com.example.ytuapplication.Entities;

import android.net.Uri;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Notification {
    User owner;
    String title, notificationContent;
    Uri notificationPhoto;
    String releaseDate, expirationDate;

    public Notification() {
    }

    public Notification(User owner, String title, String notificationContent, String releaseDate, String expirationDate, Uri notificationPhoto) {
        this.owner = owner;
        this.title = title;
        this.notificationContent = notificationContent;
        this.releaseDate = releaseDate;
        this.expirationDate = expirationDate;
        this.notificationPhoto=notificationPhoto;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotificationContent() {
        return notificationContent;
    }

    public void setNotificationContent(String notificationContent) {
        this.notificationContent = notificationContent;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Uri getNotificationPhoto() {
        return notificationPhoto;
    }

    public void setNotificationPhoto(Uri notificationPhoto) {
        this.notificationPhoto = notificationPhoto;
    }

    public LocalDate getFormattedDate(String expirationDateString) {
        LocalDate lDate = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            lDate = LocalDate.parse(expirationDateString, formatter);
        } catch (DateTimeParseException e) {
            System.err.println("Error: Invalid date format. Please use the format yyyy-MM-dd.");
        }
        return lDate;
    }
}
