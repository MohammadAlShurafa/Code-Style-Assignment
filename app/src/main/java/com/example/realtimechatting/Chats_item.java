package com.example.realtimechatting;

public class Chats_item {
    int profileImage;
    String username;

    public Chats_item(int profileImage, String username) {
        this.profileImage = profileImage;
        this.username = username;
    }

    public int getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(int profileImage) {
        this.profileImage = profileImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
