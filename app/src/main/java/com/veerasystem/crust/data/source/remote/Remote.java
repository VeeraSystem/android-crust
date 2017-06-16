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

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.veerasystem.crust.data.ActiveConnectionModel;
import com.veerasystem.crust.data.ActiveSessionModel;
import com.veerasystem.crust.data.FailedConnectionModel;
import com.veerasystem.crust.data.FailedSessionModel;
import com.veerasystem.crust.data.RemoteUsersModel;
import com.veerasystem.crust.data.ServerAccountModel;
import com.veerasystem.crust.data.ServerCountModel;
import com.veerasystem.crust.data.ServerGroupCountModel;
import com.veerasystem.crust.data.TokenModel;
import com.veerasystem.crust.data.source.ApiAccess;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class Remote implements ApiAccess {

    private static Remote INSTANCE = null;
    private static Retrofit retrofit;
    private static OkHttpClient okHttpClient;
    private CrustServiceAPI crustService;

    public static Remote getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new Remote();
        return INSTANCE;
    }

    private Remote() {
        okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }

    public void setup(String serverAddress) {
        if (retrofit != null)
            return;

        retrofit = new Retrofit.Builder()
                .baseUrl("http://" + serverAddress + "/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
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
    public Observable<TokenModel> login(String username, String password, String otp) {
        return crustService.login(username, password, otp);
    }

    @Override
    public Observable<ResponseBody> logout(String token) {
        return crustService.logout(token);
    }

    @Override
    public Observable<ServerAccountModel> getServerAccountCount(String tokenID) {
        return crustService.getServerAccountCount(tokenID);
    }

    @Override
    public Observable<ServerGroupCountModel> getServerGroupCount(String tokenID) {
        return crustService.getServerGroupCount(tokenID);
    }

    @Override
    public Observable<ServerCountModel> getServerCount(String tokenID) {
        return crustService.getServerCount(tokenID);
    }

    @Override
    public Observable<RemoteUsersModel> getRemoteUsersCount(String tokenID) {
        return crustService.getRemoteUserCount(tokenID);
    }

    @Override
    public Observable<ActiveConnectionModel> getActiveConnections(String tokenID, int active, int page, int pageSize) {
        return crustService.activeConnections(tokenID, active, page, pageSize);
    }

    @Override
    public Observable<FailedConnectionModel> getFailedConnections(String tokenID) {
        return crustService.failedConnections(tokenID);
    }

    @Override
    public Observable<ActiveSessionModel> getActiveSessions(String tokenID, int active, int page, int pageSize) {
        return crustService.activeSessions(tokenID, active, page, pageSize);
    }

    @Override
    public Observable<FailedSessionModel> getFailedSessions(String tokenID) {
        return crustService.failedSessions(tokenID);
    }

    @Override
    public Observable<ActiveSessionModel> getFilterActiveSessions(String tokenID, int active, HashMap<String, String> parameters) {
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
