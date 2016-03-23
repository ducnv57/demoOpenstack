package com.samsunguet.sev_user.mycloud.log;

/**
 * Created by sev_user on 3/5/2016.
 */
public class MyLog {

    static String LOG_TAG = "S2UETCLOUD";

    public static void log(String str){
        android.util.Log.i(LOG_TAG, str);
    }
}
