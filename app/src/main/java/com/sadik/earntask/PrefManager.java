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

    // TOKEN
    public void saveToken(String t){
        ed.putString("token",t).apply();
    }
    public String getToken(){
        return sp.getString("token",null);
    }

    // USER BASIC INFO
    public void saveUser(String id,String name,String email,String code){
        ed.putString("uid",id);
        ed.putString("name",name);
        ed.putString("email",email);
        ed.putString("my_code",code);
        ed.apply();
    }

    public String getUserId(){ return sp.getString("uid",""); }
    public String getName(){ return sp.getString("name","User"); }
    public String getEmail(){ return sp.getString("email",""); }
    public String getMyCode(){ return sp.getString("my_code",""); }

    // LOGOUT
    public void logout(){
        ed.clear().apply();
    }
}
