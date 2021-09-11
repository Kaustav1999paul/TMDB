package com.example.tmdb.Model;

public class Cast {
    private String name, character, profile_path;
    private int popularity;

    public Cast(String name, String character, String profile_path, int popularity) {
        this.name = name;
        this.character = character;
        this.profile_path = profile_path;
        this.popularity = popularity;
    }

    public String getName() {
        return name;
    }

    public String getCharacter() {
        return character;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public int getPopularity() {
        return popularity;
    }
}
