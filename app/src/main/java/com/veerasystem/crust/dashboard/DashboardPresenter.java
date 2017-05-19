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

package com.veerasystem.crust.dashboard;

import android.util.Log;

import com.google.gson.Gson;
import com.veerasystem.crust.data.RemoteUsersModel;
import com.veerasystem.crust.data.ServerAccountModel;
import com.veerasystem.crust.data.ServerCountModel;
import com.veerasystem.crust.data.ServerGroupCountModel;
import com.veerasystem.crust.data.source.remote.Remote;

import java.io.IOException;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DashboardPresenter implements DashboardContract.Presenter {

    private static final String TAG = "DashboardPresenter";

    private Remote remote;
    private DashboardContract.View view;

    private Gson gson;

    @Override
    public void start() {
        loadRemoteUserCount();
        loadServerAccountCount();
        loadServerCount();
        loadServerGroupCount();
    }

    public DashboardPresenter(Remote remote, DashboardContract.View view) {
        gson = new Gson();
        this.remote = remote;
        this.view = view;

        view.setPresenter(this);
    }

    @Override
    public void loadServerGroupCount() {
        Observable<ResponseBody> serverGroupCount = remote.getServerGroupCount(view.getToken());
        serverGroupCount.subscribeOn(Schedulers.newThread())
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
                            ServerGroupCountModel serverGroupCountModel = gson.fromJson(responseBody.string(), ServerGroupCountModel.class);

                            int count;
                            count = serverGroupCountModel.getCount();
                            Log.d("ServerGroupCount", String.valueOf(count));
                            view.showServerGroupsCount(String.valueOf(count));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void loadServerCount() {
        Observable<ResponseBody> serverCount = remote.getServerCount(view.getToken());
        serverCount.subscribeOn(Schedulers.newThread())
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
                            ServerCountModel serverCountModel = gson.fromJson(responseBody.string(), ServerCountModel.class);

                            int count;
                            count = serverCountModel.getCount();
                            view.showServerCount(String.valueOf(count));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void loadServerAccountCount() {
        Observable<ResponseBody> serverAccountCount = remote.getServerAccountCount(view.getToken());
        serverAccountCount.subscribeOn(Schedulers.newThread())
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
                            ServerAccountModel serverAccountModel = gson.fromJson(responseBody.string(), ServerAccountModel.class);

                            int count;
                            count = serverAccountModel.getCount();
                            view.showServerAccountsCount(String.valueOf(count));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void loadRemoteUserCount() {
        Observable<ResponseBody> remoteUserCount = remote.getRemoteUsersCount(view.getToken());
        remoteUserCount.subscribeOn(Schedulers.newThread())
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
                            RemoteUsersModel remoteUsersModel = gson.fromJson(responseBody.string(), RemoteUsersModel.class);

                            int count;
                            count = remoteUsersModel.getCount();
                            view.showRemoteUsersCount(String.valueOf(count));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
