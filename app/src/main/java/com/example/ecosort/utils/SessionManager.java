package com.example.ecosort.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences prefs;

    public SessionManager(Context ctx) {
        prefs = ctx.getSharedPreferences("ecosort_prefs", Context.MODE_PRIVATE);
    }

    public void saveSession(int userId, String username)
    {
        prefs.edit().putInt("user_id", userId).putString("username", username).apply();
    }

    public int getUserId() { return prefs.getInt("user_id", -1); }
    public String getUsername() { return prefs.getString("username", ""); }
    public boolean isLoggedIn() { return getUserId() != -1; }
    public void logout() { prefs.edit().clear().apply(); }
}
