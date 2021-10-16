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
        Log.d(TAG, "RAR:: Internet connected:"+status);
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                status = "Wifi enabled";
                Log.d(TAG, "RAR:: Internet connected:"+status);
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                status = "Mobile data enabled";
                Log.d(TAG, "RAR:: Internet connected:"+status);
                return true;
            }
            else{
                return false;
            }
        } else {
            status = "No internet is available";
            Log.d(TAG, "RAR:: Internet connected:"+status);
            return false;
        }
    }

}
