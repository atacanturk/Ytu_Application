package com.example.ytuapplication.Entities;

import android.net.Uri;

public class UserDetail {
    int enteranceYear, graduateYear;
    Uri profilePhoto;
    String graduateLevel, job, phone;

    public UserDetail() {
    }

    public UserDetail(int enteranceYear, int graduateYear, Uri profilePhoto, String graduateLevel, String job, String phone) {
        this.enteranceYear = enteranceYear;
        this.graduateYear = graduateYear;
        this.profilePhoto = profilePhoto;
        this.graduateLevel = graduateLevel;
        this.job = job;
        this.phone = phone;
    }

    public int getEnteranceYear() {
        return enteranceYear;
    }

    public void setEnteranceYear(int enteranceYear) {
        this.enteranceYear = enteranceYear;
    }

    public int getGraduateYear() {
        return graduateYear;
    }

    public void setGraduateYear(int graduateYear) {
        this.graduateYear = graduateYear;
    }

    public Uri getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(Uri profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getGraduateLevel() {
        return graduateLevel;
    }

    public void setGraduateLevel(String graduateLevel) {
        this.graduateLevel = graduateLevel;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
