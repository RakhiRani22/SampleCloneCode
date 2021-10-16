package com.example.sample1.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Utility {
    private static final String TAG = "Utility";
    public static final String USERNAME = "username";
    public static final String REPOSITORY_NAME = "repositoryName";

    public static boolean isNetworkConnected(Context context) {
        String status = null;
        boolean isConnected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                status = "Wifi enabled";
                isConnected = true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                status = "Mobile data enabled";
                isConnected = true;
            }
        } else {
            status = "No internet is available";
            isConnected = false;
        }
        Log.d(TAG, "Network status:"+status+" isConnected:"+isConnected);
        return isConnected;
    }
}
