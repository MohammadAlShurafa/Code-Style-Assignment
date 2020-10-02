package com.example.realtimechatting;

public class User {
    String userId;
    String username;
    String imageURL;

    public User() {
    }

    public User(String userId, String username, String imageURL) {
        this.userId = userId;
        this.username = username;
        this.imageURL = imageURL;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
