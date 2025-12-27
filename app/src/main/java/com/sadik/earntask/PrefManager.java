package com.sadik.earntask;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    SharedPreferences sp;
    SharedPreferences.Editor ed;

    public PrefManager(Context c){
        sp = c.getSharedPreferences("USER",Context.MODE_PRIVATE);
        ed = sp.edit();
    }

    public void saveToken(String t){
        ed.putString("token",t).apply();
    }

    public String getToken(){
        return sp.getString("token",null);
    }

    public void logout(){
        ed.clear().apply();
    }

}
