package com.example.myapplication.Model;

import android.content.SharedPreferences;

public class AccessTokenManager {
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private static AccessTokenManager instance = null;

    private AccessTokenManager(SharedPreferences _prefs) {
        this.prefs = _prefs;
        this.editor = prefs.edit();
    }

    public static synchronized AccessTokenManager getInstance(SharedPreferences _prefs) {
        if (instance == null) {
            instance = new AccessTokenManager(_prefs);
        }
        return instance;
    }

    public void saveToken(AccessToken token) {
        editor.putString("ACCESS_TOKEN", token.getAccess_token()).commit();
    }

    public void deleteToken() {
        editor.remove("ACCESS_TOKEN").commit();
    }

    public AccessToken getToken() {
        AccessToken token = new AccessToken();
        token.setAccess_token(prefs.getString("ACCESS_TOKEN", null));
        return token;
    }

    public void saveRole(AccessToken token) {
        editor.putInt("ROLE", token.getRole()).commit();
    }

    public void deleteRole() {
        editor.remove("ROLE").commit();
    }

    public AccessToken getRole() {
        AccessToken token = new AccessToken();
        token.setRole(prefs.getInt("ROLE", 1));
        return token;
    }
}
