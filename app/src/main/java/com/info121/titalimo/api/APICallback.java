package com.info121.titalimo.api;

import android.util.Log;



import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by KZHTUN on 3/8/2016.
 */

public abstract class APICallback<T> implements Callback<T> {
    private static final String TAG = APIClient.class.getSimpleName();

    public void onResponse(Call<T> call, Response<T> response) {
        if (response.body() != null) {
            EventBus.getDefault().post(response.body());
            Log.e(TAG, "Retrofit call success response [ " + response.body().toString() + " ] ");
        } else {
            if (response.code() == 404 || response.code()== 500) {
                onFailure(call);
            } else if (response.errorBody() != null) {
                onFailure(response.errorBody());
            }
        }
    }


    public void onFailure(Call<T> call) {
        onFailure(call, null);
    }

    public void onFailure(ResponseBody responseBody) {
        try {
            JSONObject json = new JSONObject(responseBody.string());
            EventBus.getDefault().post(json);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            EventBus.getDefault().post(responseBody.toString());
        }
    }

    public void onFailure(Call<T> call, Throwable t) {
        EventBus.getDefault().post(t);
        Log.e(TAG, "Retrofit call fail : " + t.getMessage());
    }

}
