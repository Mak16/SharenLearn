package com.example.sharenlearn;

public class Notes {

    private String title;
    private String description;
    private String downloadLink;
    private String category;
    private int downloads;
    private String user_id;
    private String user_email;
    private int notes_id;
    private int rating;
    private  String image_link;

    public Notes(String title, String description, String downloadLink, String category, int downloads, String user_id, String user_email, int notes_id, int rating, String image_link) {
        this.title = title;
        this.description = description;
        this.downloadLink = downloadLink;
        this.category = category;
        this.downloads = downloads;
        this.user_id = user_id;
        this.user_email = user_email;
        this.notes_id = notes_id;
        this.rating = rating;
        this.image_link = image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public String getImage_link() {
        return image_link;
    }

//    public Notes(String title, String description, String downloadLink, String category, int downloads, String user_id, String user_email, int notes_id, int rating) {
//        this.title = title;
//        this.description = description;
//        this.downloadLink = downloadLink;
//        this.category = category;
//        this.downloads = downloads;
//        this.user_id = user_id;
//        this.user_email = user_email;
//        this.notes_id = notes_id;
//        this.rating = rating;
//    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public int getNotes_id() {
        return notes_id;
    }

    public void setNotes_id(int notes_id) {
        this.notes_id = notes_id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Notes(String title, String description, String downloadLink, String category, int downloads, String user_id, String user_email) {
        this.title = title;
        this.description = description;
        this.downloadLink = downloadLink;
        this.category = category;
        this.downloads = downloads;
        this.user_id = user_id;
        this.user_email = user_email;
    }

    public Notes(String title, String description, String downloadLink, String category) {
        this.title = title;
        this.description = description;
        this.downloadLink = downloadLink;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public String getCategory() {
        return category;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
