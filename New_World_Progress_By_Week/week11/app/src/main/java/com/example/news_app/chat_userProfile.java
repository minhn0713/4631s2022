package com.example.news_app;

public class chat_userProfile {


    //create 2 variables since we only want to pass two variables
    public String username, userUID;

    public chat_userProfile() {
    }

    public chat_userProfile(String username, String userUID) {
        this.username = username;
        this.userUID = userUID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

}
