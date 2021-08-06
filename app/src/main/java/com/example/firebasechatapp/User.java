package com.example.firebasechatapp;

public class User {

    private String name;
    private String email;
    private String userID;
    private int avatarMockUprecource;

    public User(){}

    public User(String name, String email, String userID, int avatarMockUprecource) {
        this.name = name;
        this.email = email;
        this.userID = userID;
        this.avatarMockUprecource = avatarMockUprecource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getAvatarMockUprecource() {
        return avatarMockUprecource;
    }

    public void setAvatarMockUprecource(int avatarMockUprecource) {
        this.avatarMockUprecource = avatarMockUprecource;
    }
}
