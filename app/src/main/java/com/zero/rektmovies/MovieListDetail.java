package com.zero.rektmovies;

public class MovieListDetail {
    private String ImageUrl;
    private String Title;
    private String Rating;
    private String year;
    private String id;

    public MovieListDetail(String imageUrl, String title, String rating, String year, String id) {
        ImageUrl = imageUrl;
        Title = title;
        Rating = rating;
        this.year = year;
        this.id = id;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public String getTitle() {
        return Title;
    }

    public String getRating() {
        return Rating;
    }

    public String getYear() {
        return year;
    }

    public String getId() {
        return id;
    }
}
