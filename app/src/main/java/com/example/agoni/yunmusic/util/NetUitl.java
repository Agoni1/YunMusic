package com.example.agoni.yunmusic.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Agoni on 2016/10/5.
 */
public class NetUitl {
    public static String getNetState(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        if (activeNetworkInfo==null){
            return "NO_NET";
        }
        boolean connected = activeNetworkInfo.isConnected();
        if (connected){
            int type = activeNetworkInfo.getType();
            String netType=null;
            switch (type){
                case ConnectivityManager.TYPE_WIFI:{
                    netType="WIFI";
                }break;
                case ConnectivityManager.TYPE_MOBILE:{
                    netType="MOBILE_DATA";
                }break;
            }
            return netType;
        }
        return null;
    }

    /**
     * 获取json
     * @param requestUrl
     * @return
     */
    public static String request(String requestUrl){
        try {
            URL url=new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer sb=new StringBuffer();
            String line;
            while ((line=reader.readLine())!=null){
                sb.append(line);
            }
            return sb.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过okhttp获取json
     * @param url
     * @return
     */
    public static String requestbyOkhttp(String url){
        String result = null;
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request=new Request.Builder().url(url).build();
            Call call = okHttpClient.newCall(request);
            Response response = call.execute();
            result=response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static byte[] requestforBytebyOkhttp(String url){
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request=new Request.Builder().url(url).build();
            Call call = okHttpClient.newCall(request);
            Response response = call.execute();
            byte[] bytes = response.body().bytes();
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
