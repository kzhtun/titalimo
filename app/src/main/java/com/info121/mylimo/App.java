package com.info121.mylimo;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;


/**
 * Created by KZHTUN on 7/25/2017.
 */

public class App extends Application {

    public static String CONST_BASE_URL = "http://alexisinfo121.noip.me:85/iops_portal/";
    public static String CONST_URL_JOB_LIST = CONST_BASE_URL + "iDriverJobsList.aspx?LogInUser=%s";
    public static String CONST_USER_NAME = "USER_NAME";
    public static String CONST_ALREADY_LOGIN = "ALREADY_LOGIN";

    public static String userName = "";
    public static String deviceID = "";

    public static long timerDelay = 0;

    public static Context targetContent ;

    public static Runnable mRunnable;
    public static final Handler mHandler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
       // Log.e("FCM Token : ", FirebaseInstanceId.getInstance().getToken());
    }


    @Override
    public void onTerminate() {
        super.onTerminate();

        mHandler.removeCallbacks(App.mRunnable);
        mRunnable = null;

    }
}
