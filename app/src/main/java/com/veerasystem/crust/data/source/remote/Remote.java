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

import com.veerasystem.crust.data.source.ApiAccess;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class Remote implements ApiAccess {

    private static Remote INSTANCE = null;
    private static Retrofit retrofit;
    private CrustServiceAPI crustService;

    public static Remote getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new Remote();
        return INSTANCE;
    }

    private Remote() {
    }

    public void setup(String serverAddress) {
        if (retrofit != null)
            return;

        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("http://" + serverAddress + "/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        crustService = retrofit.create(CrustServiceAPI.class);
    }

    public String currentServerAddress() {
        return retrofit.baseUrl().url().getAuthority();
    }

    @Override
    public Observable<ResponseBody> getOtp(String username, String password) {
        return crustService.loginOtpRequest(username, password);
    }

    @Override
    public Observable<ResponseBody> login(String username, String password, String otp) {
        return crustService.login(username, password, otp);
    }

    @Override
    public Observable<ResponseBody> logout(String token) {
        return crustService.logout(token);
    }

    @Override
    public Observable<ResponseBody> getServerAccountCount(String tokenID) {
        return crustService.getServerAccountCount(tokenID);
    }

    @Override
    public Observable<ResponseBody> getServerGroupCount(String tokenID) {
        return crustService.getServerGroupCount(tokenID);
    }

    @Override
    public Observable<ResponseBody> getServerCount(String tokenID) {
        return crustService.getServerCount(tokenID);
    }

    @Override
    public Observable<ResponseBody> getRemoteUsersCount(String tokenID) {
        return crustService.getRemoteUserCount(tokenID);
    }

    @Override
    public Observable<ResponseBody> getActiveConnections(String tokenID, int active, int page, int pageSize) {
        return crustService.activeConnections(tokenID, active, page, pageSize);
    }

    @Override
    public Observable<ResponseBody> getFailedConnections(String tokenID) {
        return crustService.failedConnections(tokenID);
    }

    @Override
    public Observable<ResponseBody> getActiveSessions(String tokenID, int active, int page, int pageSize) {
        return crustService.activeSessions(tokenID, active, page, pageSize);
    }

    @Override
    public Observable<ResponseBody> getFailedSessions(String tokenID) {
        return crustService.failedSessions(tokenID);
    }

    @Override
    public Observable<ResponseBody> getFilterActiveSessions(String tokenID, int active, HashMap<String, String> parameters) {
        return crustService.filterActiveSessions(tokenID, active, parameters);
    }

    @Override
    public Observable<ResponseBody> killSession(String tokenID, int sessionID) {
        return crustService.killSession(tokenID, sessionID);
    }

    @Override
    public Observable<ResponseBody> sendSessionMessage(String tokenID, String message, int sessionID) {
        return crustService.sendSessionMessage(tokenID, message, sessionID);
    }

}
