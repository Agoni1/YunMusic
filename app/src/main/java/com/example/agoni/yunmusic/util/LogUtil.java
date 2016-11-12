package com.example.agoni.yunmusic.util;

import android.util.Log;

/**
 * Created by Agoni on 2016/10/6.
 */
public class LogUtil {
    private static boolean isopen=true;
    private static String TAG="tag";
    public static void setTag(String tag){
        LogUtil.TAG =tag;
    }
     public static void i(String text){
         if (isopen){
             Log.i(TAG,text);
         }
     }
    public static void i(String tag,String text){
        if (isopen){
            Log.i(tag,text);
        }
    }
}
