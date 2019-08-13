package com.info121.titalimo.api;


import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.info121.titalimo.App;
import com.info121.titalimo.models.ConfirmJobRes;
import com.info121.titalimo.models.LoginRes;
import com.info121.titalimo.models.RemindLaterRes;
import com.info121.titalimo.models.SaveShowPicRes;
import com.info121.titalimo.models.SaveSignatureRes;
import com.info121.titalimo.models.UpdateDriverDetailRes;
import com.info121.titalimo.models.UpdateDriverLocationRes;
import com.info121.titalimo.models.VersionRes;
import com.info121.titalimo.models.product;

import java.util.List;

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

        Log.e("Firebase Token : ", App.FCM_TOKEN);

        Call<UpdateDriverDetailRes> call = RestClient.LIMO().getApiService().updateDriverDetail(userName, deviceId, DEVICE_TYPE, App.FCM_TOKEN );
        call.enqueue(new APICallback<UpdateDriverDetailRes>() {
        });
    }

    //("getdriverlocation/{user},{latitude},{longitude},{status}")
    public static void UpdateDriverLocation(String userName, String latitude, String longitude, int status, String address) {
        Call<UpdateDriverLocationRes> call = RestClient.LIMO().getApiService().updateDriverLocation(userName, latitude, longitude, status, address);
        call.enqueue(new APICallback<UpdateDriverLocationRes>() {
        });
    }

    public static void SaveShowPicture(String userName, String jobNo, String fileName) {
        Call<SaveShowPicRes> call = RestClient.LIMO().getApiService().saveShowPic(userName, jobNo, fileName);
        call.enqueue(new APICallback<SaveShowPicRes>() {
        });
    }

    public static void ConfirmJob(String jobNo) {
        Call<ConfirmJobRes> call = RestClient.LIMO().getApiService().confirmJob(jobNo);
        call.enqueue(new APICallback<ConfirmJobRes>() {
        });
    }

    public static void RemindLater(String jobNo) {
        Call<RemindLaterRes> call = RestClient.LIMO().getApiService().remindLater(jobNo);
        call.enqueue(new APICallback<RemindLaterRes>() {
        });
    }


    public static void GetProduct() {
        Call<List<product>> call = RestClient.LIMO().getApiService().getProduct();
        call.enqueue(new APICallback<List<product>>() {
        });
    }

    public static void CheckVersion(String versionCode) {
        Call<VersionRes> call = RestClient.LIMO().getApiService().checkVersion(versionCode);
        call.enqueue(new APICallback<VersionRes>() {
        });
    }

    public static void SaveSignature(String jobNo, String fileName) {
        Call<SaveSignatureRes> call = RestClient.LIMO().getApiService().savesignature(jobNo, fileName);
        call.enqueue(new APICallback<SaveSignatureRes>() {
        });
    }
}
