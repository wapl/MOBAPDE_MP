package com.example.mobapde_mp;

public class MovieModel {
    private int movieID;
    private String Title;
    private String imageURL;

    public MovieModel(int movieID, String title, String imageURL) {
        this.movieID = movieID;
        Title = title;
        this.imageURL = imageURL;
    }

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
