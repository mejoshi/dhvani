/*
*  User song Upload details
*
*
* */
package com.joshi.dhvaniimplementing01.models;

import java.util.HashMap;
import java.util.Map;

public class UserSongUploadDetails {
    private String user_song_id;
    private String user_song_name;
    private String user_song_genre;
    private String user_short_description;
    private String user_song_time_stamp;
    private String user_song_storage_url;
    private UserRegistrationDetails created_by;

    public UserSongUploadDetails(){

    }

    public String getUser_song_time_stamp() {
        return user_song_time_stamp;
    }

    public UserSongUploadDetails(String user_song_id,
                                 String user_song_name,
                                 String user_song_genre,
                                 String user_song_description,
                                 String user_song_upload_time_stamp,
                                 String user_song_storage_url,
                                 UserRegistrationDetails created_by){
        this.user_song_id= user_song_id;
        this.user_song_name = user_song_name;
        this.user_song_genre = user_song_genre;
        this.user_short_description = user_song_description;
        this.user_song_time_stamp = user_song_upload_time_stamp;
        this.user_song_storage_url = user_song_storage_url;
        this.created_by = created_by;

    }

    public Map<String, String> userSongUploadInformation(){

        Map<String, String> user_song_information = new HashMap<>();
        user_song_information.put("user_song_id",user_song_id);
        user_song_information.put("user_song_name",user_song_name);
        user_song_information.put("user_song_genre",user_song_genre);
        user_song_information.put("user_short_description", user_short_description);
        user_song_information.put("user_song_time_stamp", user_song_time_stamp);
        user_song_information.put("user_song_song_url",user_song_storage_url);


        return user_song_information;
    }

    public UserRegistrationDetails getCreated_by(){
        return created_by;
    }

    public String getUser_id(){
        return user_song_id;
    }

    public String getUser_song_name(){
        return user_song_name;
    }

    public String getSong_genre(){
        return user_song_genre;
    }

    public String getUser_short_description(){
        return user_short_description;
    }

    public String getUser_song_storage_url() {
        return user_song_storage_url;
    }






}

