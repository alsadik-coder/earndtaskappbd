package com.sadik.earntask;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    private static final String PREF_NAME = "user_pref";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_IS_LOGIN = "is_login";

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public PrefManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    // Save user on SignUp
    public void saveUser(String name, String phone, String email, String password) {
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }

    // Login check
    public boolean checkLogin(String email, String password) {
        String savedEmail = pref.getString(KEY_EMAIL, "");
        String savedPassword = pref.getString(KEY_PASSWORD, "");
        return email.equals(savedEmail) && password.equals(savedPassword);
    }

    public void setLogin(boolean status) {
        editor.putBoolean(KEY_IS_LOGIN, status);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGIN, false);
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}
