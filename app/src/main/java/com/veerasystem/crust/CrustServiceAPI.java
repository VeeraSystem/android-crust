/*************************************************************************
 *
 * Veera CONFIDENTIAL
 * __________________
 *
 *  [2016] Veera System Incorporated
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Veera System Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Veera System Incorporated
 * and its suppliers and may be covered by IR. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Veera System Incorporated.
 */

package com.veerasystem.crust;


import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface CrustServiceAPI {

    @FormUrlEncoded
    @POST("auth/obtain-token/")
    Observable<ResponseBody> login(
            @Field("username") String username,
            @Field("password") String password
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
    Observable<ResponseBody> killSession (
            @Header("Authorization") String token,
            @Query("session_id") int sessionID
    );

    @GET("crustsessions/sendmsg/")
    Observable<ResponseBody> sendSessionMessage (
            @Header("Authorization") String token,
            @Query("message") String message,
            @Query("session_id") int sessionID
    );


    @GET("servergroups/servercounts/")
    Observable<ResponseBody> loadServerGroupChartData(
            @Header("Authorization") String token
    );
}
