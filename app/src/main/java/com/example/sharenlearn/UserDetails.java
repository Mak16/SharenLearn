package com.example.sharenlearn;

public class UserDetails {

    private String username;
    private String email;
    private int downloads;
    private int uploads;
    private String created_at;
    private String uid;

    public UserDetails(String username, String email, int downloads, int uploads, String created_at ,String uid) {
        this.username = username;
        this.email = email;
        this.downloads = downloads;
        this.uploads = uploads;
        this.created_at = created_at;
        this.uid = uid;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public int getDownloads() {
        return downloads;
    }

    public int getUploads() {
        return uploads;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public void setUploads(int uploads) {
        this.uploads = uploads;
    }
}
