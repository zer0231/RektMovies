package com.zero.rektmovies;

public class SearchFilter{
    String quality;
    String query;
    String min_rating;
    String genre;
    String sort_by;
    String pageNumber;

    public SearchFilter()
    {
        this.quality = "720p";
        this.query = "";
        this.min_rating="0";
        this.genre="all";
        this.sort_by="date_added";
        this.pageNumber = "1";
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setMin_rating(String min_rating) {
        this.min_rating = min_rating;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setSort_by(String sort_by)
    {
        this.sort_by = sort_by;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getURL()
    {
        return "https://yts.mx/api/v2/list_movies.json?minimum_rating="+min_rating+"&genre="+genre+"&page="+pageNumber+"&limit=30&quality="+quality+"&query_term="+query+"&sort_by="+sort_by;
    }
}
