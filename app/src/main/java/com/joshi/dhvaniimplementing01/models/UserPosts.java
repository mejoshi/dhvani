package com.joshi.dhvaniimplementing01.models;

public class UserPosts {
    private String user_song_id;
    private String user_song_name;
    private String user_song_genre;
    private String user_short_description;
    private String user_song_storage_url;
    private String user_song_time_stamp;

    public UserPosts() {

    }

    public UserPosts(String user_short_description,
                     String user_song_genre,
                     String user_song_id,
                     String user_song_name,
                     String user_song_storage_url,
                     String user_song_time_stamp) {
        this.user_song_id = user_song_id;
        this.user_song_name = user_song_name;
        this.user_song_genre = user_song_genre;
        this.user_short_description = user_short_description;
        this.user_song_storage_url = user_song_storage_url;
        this.user_song_time_stamp = user_song_time_stamp;

    }

    public String getUser_song_id() {
        return user_song_id;
    }

    public String getUser_song_name() {
        return user_song_name;
    }

    public String getUser_song_genre() {
        return user_song_genre;
    }

    public String getUser_short_description() {
        return user_short_description;
    }

    public String getUser_song_storage_url() {
        return user_song_storage_url;
    }

    public String getUser_song_time_stamp() {
        return user_song_time_stamp;
    }
}