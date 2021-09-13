package com.example.tmdb.Model;

public class TV {
    private String backdrop_path, name, original_language, overview, poster_path, release_date;
    private int id;
    private float vote_average;

    public TV(String backdrop_path, String name, String original_language, String overview, String poster_path, String release_date, int id, float vote_average) {
        this.backdrop_path = backdrop_path;
        this.name = name;
        this.original_language = original_language;
        this.overview = overview;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.id = id;
        this.vote_average = vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getName() {
        return name;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getOverview() {
        return overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public int getId() {
        return id;
    }

    public float getVote_average() {
        return vote_average;
    }
}

