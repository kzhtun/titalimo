package com.info121.titalimo.api;



import com.info121.titalimo.models.AuthenticationReq;
import com.info121.titalimo.models.AuthenticationRes;
import com.info121.titalimo.models.LoginRes;
import com.info121.titalimo.models.SaveShowPicRes;
import com.info121.titalimo.models.UpdateDriverDetailRes;
import com.info121.titalimo.models.UpdateDriverLocationRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by KZHTUN on 7/6/2017.
 */

public interface APIService {

    @GET("validatedriverDetail/{user}")
    Call<LoginRes> getAuthentication(@Path("user") String user);

    //amad,123,android,241jlksfljsaf
    @GET("updatedriverdetail/{user},{deviceId},{deviceType},{fcm_token}")
    Call<UpdateDriverDetailRes> updateDriverDetail(@Path("user") String user, @Path("deviceId") String deviceId,@Path("deviceType") String deviceType, @Path("fcm_token") String fcm_token);

    //amad,1.299654,103.855107,0
    @GET("getdriverlocation/{user},{latitude},{longitude},{datetime},{status}")
    Call<UpdateDriverLocationRes> updateDriverLocation(@Path("user") String user, @Path("latitude") String latitude,@Path("longitude") String longitude, @Path("status") int status);


    @GET("saveshowpic/{user},{job_no},{filename}")
    Call<SaveShowPicRes> saveShowPic(@Path("user") String user, @Path("job_no") String jobNo, @Path("filename") String fileName);

}
