package com.zero.rektmovies;

import java.util.List;

public class FullDetail {
    String summary;
    double rating;
    String image;
    String title;
    List<Torrent> torrents;
    List<String> genres;

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<String> getGenres() {
        return genres;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTorrents(List<Torrent> torrents) {
        this.torrents = torrents;
    }

    public List<Torrent> getTorrents() {
        return torrents;

    }

    public String getSummary() {
        return summary;
    }

    public double getRating() {
        return rating;
    }

    public String getTitle() {
        return title;
    }
}
