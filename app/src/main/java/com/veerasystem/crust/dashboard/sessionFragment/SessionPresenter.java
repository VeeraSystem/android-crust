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

package com.veerasystem.crust.dashboard.sessionFragment;

import android.util.Log;

import com.google.gson.Gson;
import com.veerasystem.crust.data.ActiveSessionModel;
import com.veerasystem.crust.data.FailedSessionModel;
import com.veerasystem.crust.data.source.remote.Remote;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SessionPresenter implements SessionContract.Presenter {

    private static final String TAG = "SessionPresenter";

    private SessionContract.View view;
    private Remote remote;

    public SessionPresenter(Remote remote, SessionContract.View view) {
        this.remote = remote;
        this.view = view;

        view.setPresenter(this);
    }

    @Override
    public void loadActiveList() {
        Observable<ResponseBody> activeSessionList = remote.getActiveSessions(view.getToken(), 1, 1, 10);
        activeSessionList.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            Gson gson = new Gson();
                            ActiveSessionModel activeSessionModel = gson.fromJson(responseBody.string(), ActiveSessionModel.class);
                            List<ActiveSessionModel.Result> itemsData;
                            itemsData = activeSessionModel.getsResult();

                            view.showActiveList(itemsData);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void loadFailedList() {
        Observable<ResponseBody> failedSessionList = remote.getFailedSessions(view.getToken());
        failedSessionList.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String response = responseBody.string();
                            Log.i(TAG, "onNext:Failed Sessions: " + response);
                            Gson gson = new Gson();
                            FailedSessionModel failedSessionModel = gson.fromJson(response, FailedSessionModel.class);
                            List<FailedSessionModel.SessionFailCount> itemsData;
                            itemsData = failedSessionModel.getSessionFailCounts();

                            view.showFailedList(itemsData);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void loadFilteredActiveList(String sourceIp, String remoteUser, String server) {
        HashMap<String, String> parameters = new HashMap<>();

        if (!sourceIp.isEmpty())
            parameters.put("client_ip", sourceIp);
        if (!remoteUser.isEmpty())
            parameters.put("remoteuser", remoteUser);
        if (!server.isEmpty())
            parameters.put("server", server);

        Observable<ResponseBody> activeSessionList = remote.getFilterActiveSessions(view.getToken(), 1, parameters);
        activeSessionList.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            Gson gson = new Gson();
                            ActiveSessionModel activeSessionModel = gson.fromJson(responseBody.string(), ActiveSessionModel.class);
                            List<ActiveSessionModel.Result> itemsData;
                            itemsData = activeSessionModel.getsResult();

                            view.showActiveList(itemsData);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void killSession(int sessionID) {
        Observable<ResponseBody> killSession = remote.killSession(view.getToken(), sessionID);
        killSession.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        //TODO: just after true response we should update list
                        loadActiveList();
                    }
                });
    }

    @Override
    public void messageSession(String message, int sessionID) {
        Observable<ResponseBody> sendMessageSession = remote.sendSessionMessage(view.getToken(), message, sessionID);
        sendMessageSession.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        //sent ok response
                    }
                });
    }

    @Override
    public void start() {
        loadActiveList();
        loadFailedList();
    }
}
