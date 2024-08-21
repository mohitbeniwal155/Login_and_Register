package com.example.assessment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference {
    private static final String USER_PREF = "user";
    private SharedPreferences appShared;
    private SharedPreferences.Editor prefsEditor;

    public SharedPreference(Context context){
        appShared=context.getSharedPreferences(USER_PREF, Activity.MODE_PRIVATE);
        this.prefsEditor=appShared.edit();
    }


    public int getValue_int(String key) {
        return appShared.getInt(key,0);
    }
    public void setValue_int(String key, int value) {
        prefsEditor.putInt(key, value).commit();
    }



    public String getValue_string(String key) {
        return appShared.getString(key,"");
    }
    public void setValue_string(String key, String value) {
        prefsEditor.putString(key, value).commit();
    }


    public boolean getValue_bool(String key) {
        return appShared.getBoolean(key, false);
    }
    public void setValue_bool(String key, boolean value) {
        prefsEditor.putBoolean(key, value).commit();
    }

    public  void clear(){
        prefsEditor.clear().commit();
    }
}
