/*
*
* User Registration Details
*
* */



package com.joshi.dhvaniimplementing01.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserRegistrationDetails {
    private String user_uid;
    private String user_full_name;
    private String user_email_id;
    private String user_short_description;
    private String user_profile_pic_url;
    private ArrayList<String> followers;

    public UserRegistrationDetails(){
        this.followers = followers;
        this.user_email_id = user_email_id;
        this.user_full_name = user_full_name;
        this.user_uid = user_uid;
        this.user_short_description = user_short_description;
        this.user_profile_pic_url = user_profile_pic_url;
    }

    public UserRegistrationDetails(String user_uid, String user_full_name, String user_email_id, String user_short_description, String user_profile_pic_url, ArrayList<String> followers){
        this.user_email_id = user_email_id;
        this.user_full_name = user_full_name;
        this.user_uid = user_uid;
        this.user_short_description = user_short_description;
        this.user_profile_pic_url = user_profile_pic_url;
        this.followers = followers;
    }

    public Map<String,String> userInformation(){
        Map<String, String> user_information = new HashMap<>();
        user_information.put("user-uid",user_uid);
        user_information.put("user-name",user_full_name);
        user_information.put("user-email-id",user_email_id);
        user_information.put("user-short-description",user_short_description);
        user_information.put("user-profile-image",user_profile_pic_url);
        return  user_information;
    }

    public String getUser_uid() { return user_uid; }

    public String getUser_full_name() {
        return user_full_name;
    }

    public String getUser_email_id() {
        return user_email_id;
    }

    public String getUser_short_description(){ return user_short_description; }

    public String getUser_profile_pic_url(){ return user_profile_pic_url; }

    public ArrayList<String> getFollowers(){ return  followers;}


}

