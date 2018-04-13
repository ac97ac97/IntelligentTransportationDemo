package com.lakalaka.intelligenttransportationdemo.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2018/3/23.
 * SharePerence的管理类
 */

public class PerferenceUtil {
    private static SharedPreferences mSharedPreferences=null;
    private static SharedPreferences.Editor mEditor = null;
    public static void init(Context context){
        if (null==mSharedPreferences){
            mSharedPreferences=android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        }
    }
    public static void commitString(String key,String value){
        mEditor=mSharedPreferences.edit();
        mEditor.putString(key,value);
        mEditor.commit();
    }
    private static String getString(String key,String failValue){
        return mSharedPreferences.getString(key,failValue);

    }
}
