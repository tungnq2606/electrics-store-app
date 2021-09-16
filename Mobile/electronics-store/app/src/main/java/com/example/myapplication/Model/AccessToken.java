package com.example.myapplication.Model;

public class AccessToken {
    private int role;
    private String access_token;

    public AccessToken() {
    }

    public AccessToken(int role, String access_token) {
        this.role = role;
        this.access_token = access_token;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
