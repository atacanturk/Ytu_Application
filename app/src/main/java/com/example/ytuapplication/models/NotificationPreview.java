package com.example.ytuapplication.models;

import android.net.Uri;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class NotificationPreview {
    String ownerName;
    Uri notificationPhoto;
    String title;
    String expirationDate;

    public NotificationPreview() {
    }

    public NotificationPreview(String ownerName, Uri notificationPhoto, String title, String expirationDate) {
        this.ownerName = ownerName;
        this.notificationPhoto = notificationPhoto;
        this.title = title;
        this.expirationDate = expirationDate;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Uri getNotificationPhoto() {
        return notificationPhoto;
    }

    public void setNotificationPhoto(Uri notificationPhoto) {
        this.notificationPhoto = notificationPhoto;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
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
