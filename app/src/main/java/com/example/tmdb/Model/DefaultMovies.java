package com.example.tmdb.Model;

public class DefaultMovies {
    private String poster_path, original_language ,title, backdrop_path, overview, release_date;
    private float vote_average;
    private int id;

    public DefaultMovies(int id, String poster_path, String original_language, String title, String backdrop_path, String overview, String release_date, float vote_average) {
        this.poster_path = poster_path;
        this.original_language = original_language;
        this.title = title;
        this.backdrop_path = backdrop_path;
        this.overview = overview;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public float getVote_average() {
        return vote_average;
    }
}
