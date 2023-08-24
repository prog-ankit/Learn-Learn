package com.example.project_learn;


import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class SessionManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session_user";
    String username;
    public SessionManager(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(String username){
        //save session of user whenever user is logged in
        editor.putString(SESSION_KEY,username).commit();
    }

    public String getSession(MainActivity mainActivity){
        //return user id whose session is saved
       // Toast.makeText(, "", Toast.LENGTH_SHORT).show();
          return sharedPreferences.getString(SESSION_KEY, "cancel");
    }

    public void removeSession(){
        editor.putString(SESSION_KEY,"cancel").commit();
    }
}