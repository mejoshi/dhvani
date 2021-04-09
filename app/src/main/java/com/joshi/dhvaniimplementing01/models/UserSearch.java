package com.joshi.dhvaniimplementing01.models;

import java.util.ArrayList;

public class UserSearch {
    private String user_full_name;
    private String user_profile_pic_url;
    private ArrayList<String> num_followers;
    private UserRegistrationDetails created_by;

    public UserSearch() {
        // To read the data
    }

    public UserSearch(UserRegistrationDetails created_by) {
        this.created_by = created_by;
    }


    public UserSearch(String user_full_name, String user_profile_pic_url, ArrayList<String> num_followers, UserRegistrationDetails created_by) {
        this.user_full_name = user_full_name;
        this.user_profile_pic_url = user_profile_pic_url;
        this.num_followers = num_followers;
        this.created_by = created_by;
    }

    public String getUser_full_name() {
        return user_full_name;
    }

    public String getUser_profile_pic_url() {
        return user_profile_pic_url;
    }

    public ArrayList<String> getNum_followers() {
        return num_followers;
    }

    public UserRegistrationDetails getCreated_by() { return created_by; }


}
