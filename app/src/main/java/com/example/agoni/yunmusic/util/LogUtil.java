package com.example.agoni.yunmusic.util;

import android.util.Log;

/**
 * Created by Agoni on 2016/10/6.
 */
public class LogUtil {
    private static boolean isopen=true;
     public static void i(String tag,String text){
         if (isopen){
             Log.i(tag,text);
         }
     }
}
