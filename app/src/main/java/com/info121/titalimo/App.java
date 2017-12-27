package com.info121.titalimo;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.info121.titalimo.utils.Util;


/**
 * Created by KZHTUN on 7/25/2017.
 */

public class App extends Application {
// LIVE
    public static final String CONST_REST_API_URL = "http://alexisinfo121.noip.me:81/restapi/MyLimoService.svc/";
    public static final String CONST_WEBSITE_URL = "http://alexisinfo121.noip.me:81/iops_portal/";


   // DEV
//    public static final String CONST_REST_API_URL = "http://alexisinfo121.noip.me:82/restapi/MyLimoService.svc/";
//    public static final String CONST_WEBSITE_URL = "http://alexisinfo121.noip.me:82/iops_portal/";


    public static final String CONST_URL_JOB_LIST = CONST_WEBSITE_URL + "iDriverJobsList.aspx?LogInUser=%s";
    public static String CONST_USER_NAME = "USER_NAME";
    public static String CONST_ALREADY_LOGIN = "ALREADY_LOGIN";

    public static String userName = "";
    public static String deviceID = "";

    public static long timerDelay = 0;
    public static Location location;

    public static Context targetContent;

    public static Runnable mRunnable;
    public static final Handler mHandler = new Handler();

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();
     //   Log.e("FCM Token ", FirebaseInstanceId.getInstance().getToken());

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Util.getDeviceID(this));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, Util.getDeviceName());
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }


    @Override
    public void onTerminate() {
        super.onTerminate();

        mHandler.removeCallbacks(App.mRunnable);
        mRunnable = null;

    }
}
