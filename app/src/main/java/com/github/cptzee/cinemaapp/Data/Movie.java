package com.github.cptzee.cinemaapp.Data;

public class Movie{
    private int id;
    private String title;
    private String description;
    private int ratingID; //MTRCB rating
    private int categoryID;
    private int cinemaID;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRating() {
        return ratingID;
    }

    public void setRating(int rating) {
        this.ratingID = rating;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getCinemaID() {
        return cinemaID;
    }

    public void setCinemaID(int cinemaID) {
        this.cinemaID = cinemaID;
    }
}
