package com.info121.titalimo.api;


import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import package com.info121.titalimo.models.LoginRes;
import com.info121.titalimo.models.SaveShowPicRes;
import com.info121.titalimo.models.UpdateDriverDetailRes;
import com.info121.titalimo.models.UpdateDriverLocationRes;

import retrofit2.Call;


/**
 * Created by KZHTUN on 7/6/2017.
 */

public class APIClient {
    private static String DEVICE_TYPE = "Android";


    public static void GetAuthentication(String userName) {
        Call<LoginRes> call = RestClient.LIMO().getApiService().getAuthentication(userName);
        call.enqueue(new APICallback<LoginRes>() {
        });
    }

    public static void UpdateDriverDetail(String userName, String deviceId) {
        String fbToken = FirebaseInstanceId.getInstance().getToken();

        Log.e("Firebase Token : ", fbToken);

        Call<UpdateDriverDetailRes> call = RestClient.LIMO().getApiService().updateDriverDetail(userName, deviceId, DEVICE_TYPE, fbToken );
        call.enqueue(new APICallback<UpdateDriverDetailRes>() {
        });
    }

    //("getdriverlocation/{user},{latitude},{longitude},{status}")
    public static void UpdateDriverLocation(String userName, String latitude, String longitude, int status) {
        Call<UpdateDriverLocationRes> call = RestClient.LIMO().getApiService().updateDriverLocation(userName, latitude, longitude, status);
        call.enqueue(new APICallback<UpdateDriverLocationRes>() {
        });
    }

    public static void SaveShowPicture(String userName, String jobNo, String fileName) {
        Call<SaveShowPicRes> call = RestClient.LIMO().getApiService().saveShowPic(userName, jobNo, fileName);
        call.enqueue(new APICallback<SaveShowPicRes>() {
        });
    }
}
