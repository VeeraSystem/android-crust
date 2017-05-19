/*
 * Copyright (c) 2017 VEERA SYSTEM
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.veerasystem.crust.data.source.remote;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

interface CrustServiceAPI {

    @FormUrlEncoded
    @POST("auth/otpbysms/")
    Observable<ResponseBody> loginOtpRequest(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("auth/otptoken/")
    Observable<ResponseBody> login(
            @Field("username") String username,
            @Field("password") String password,
            @Field("otp") String otp
    );

    @POST("auth/logout/")
    Observable<ResponseBody> logout(
            @Header("Authorization") String token
    );

    @GET("remoteconnections/")
    Observable<ResponseBody> activeConnections(
            @Header("Authorization") String token,
            @Query("active") int active,
            @Query("page") int page,
            @Query("page_size") int page_size
    );

    @GET("remoteconnections/usersfailcount/")
    Observable<ResponseBody> failedConnections(
            @Header("Authorization") String token
    );

    @GET("crustsessions/")
    Observable<ResponseBody> activeSessions(
            @Header("Authorization") String token,
            @Query("active") int active,
            @Query("page") int page,
            @Query("page_size") int page_size
    );

    @GET("crustsessions/")
    Observable<ResponseBody> filterActiveSessions(
            @Header("Authorization") String token,
            @Query("active") int active,
            @QueryMap HashMap<String, String> parameters
    );

    @GET("crustsessions/usersfailcount/")
    Observable<ResponseBody> failedSessions(
            @Header("Authorization") String token
    );

    @GET("serveraccounts/count/")
    Observable<ResponseBody> getServerAccountCount(
            @Header("Authorization") String token
    );

    @GET("servers/count/")
    Observable<ResponseBody> getServerCount(
            @Header("Authorization") String token
    );

    @GET("servergroups/count/")
    Observable<ResponseBody> getServerGroupCount(
            @Header("Authorization") String token
    );

    @GET("remoteusers/count/")
    Observable<ResponseBody> getRemoteUserCount(
            @Header("Authorization") String token
    );

    @GET("crustsessions/kill/")
    Observable<ResponseBody> killSession(
            @Header("Authorization") String token,
            @Query("session_id") int sessionID
    );

    @GET("crustsessions/sendmsg/")
    Observable<ResponseBody> sendSessionMessage(
            @Header("Authorization") String token,
            @Query("message") String message,
            @Query("session_id") int sessionID
    );

//    @GET("servergroups/servercounts/")
//    Observable<ResponseBody> loadServerGroupChartData(
//            @Header("Authorization") String token
//    );
}
