package com.com.student.Model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.com.student.SplashScreen.SplashScreen;

import java.util.HashMap;

public class ModelSaveData {
    SharedPreferences mShared;
    SharedPreferences.Editor mEditor;
    Context mContext;

    public static final String File_Name = "Student";
    public static final String Key_Name = "StudentName";
    public static final String Key_Emil = "phone";
    public static final String Key_ID = "phone";



    //Create File to save data
    public ModelSaveData(Context mContext) {
        this.mContext = mContext;
        mShared = mContext.getSharedPreferences(File_Name, Context.MODE_PRIVATE);
        mEditor = mShared.edit();
    }


    //Create method to save data
    public void SaveData(String Name, String phone ,String Key_id) {
        mEditor.putString(Key_Name, Name);
        mEditor.putString(Key_Emil, phone);
        mEditor.putString(Key_ID,Key_id);
        mEditor.commit();

    }

    //Create method load data hashMap

    public HashMap<String, String> loadData()
    {
        HashMap<String,String> user =new HashMap<>();
        user.put(Key_Name,mShared.getString(Key_Name ,null));
        user.put(Key_Emil,mShared.getString(Key_Emil ,null));
        user.put(Key_ID,mShared.getString(Key_ID ,null));
        return user ;
    }

    public String loadUserID()
    {
        return  mShared.getString(Key_ID ,null);

    }
    public String laod_Name()
    {
        return  mShared.getString(Key_Name ,null);

    }
    ///log out
    public void logOut()
    {
        mEditor.clear();
        mEditor.remove(Key_Emil);
        mEditor.remove(Key_Name);
        mEditor.remove(Key_ID);
        mEditor.apply();
        mContext.startActivity(new Intent(mContext, SplashScreen.class));

    }




}
