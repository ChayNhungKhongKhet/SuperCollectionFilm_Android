package com.phamtantb24.superfilmcollection.model;

import androidx.annotation.NonNull;

import java.util.UUID;

public class Account {
    private String userName;
    private String password;
    private String email;
    private String location;
    private String facebook;
    private String github;
    private String phone;
    private Integer followers;
    private Integer following;

    public Account(String userName, String password, String email, String location, String phone) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.location = location;
        this.phone = phone;
    }

    @NonNull
    @Override
    public String toString() {
        return "Account{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", location='" + location + '\'' +
                ", facebook='" + facebook + '\'' +
                ", github='" + github + '\'' +
                ", phone='" + phone + '\'' +
                ", followers=" + followers +
                ", following=" + following +
                '}';
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public Integer getFollowing() {
        return following;
    }

    public void setFollowing(Integer following) {
        this.following = following;
    }
}
