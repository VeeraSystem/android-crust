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

package com.veerasystem.crust.dashboard.connectionFragment;

import android.util.Log;

import com.google.gson.Gson;
import com.veerasystem.crust.data.ActiveConnectionModel;
import com.veerasystem.crust.data.FailedConnectionModel;
import com.veerasystem.crust.data.source.remote.Remote;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ConnectionPresenter implements ConnectionContract.Presenter {

    private static final String TAG = "ConnectionPresenter";

    private ConnectionContract.View view;
    private Remote remote;

    public ConnectionPresenter(Remote remote, ConnectionContract.View view) {
        this.remote = remote;
        this.view = view;

        view.setPresenter(this);
    }

    @Override
    public void start() {
        loadActiveList();
        loadFailedList();
    }

    @Override
    public void loadActiveList() {
        Observable<ResponseBody> activeConnectionList = remote.getActiveConnections(view.getToken(), 1, 1, 10);
        activeConnectionList.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String response = responseBody.string();
                            Log.i(TAG, "onNext: " + response);
                            Gson gson = new Gson();
                            ActiveConnectionModel activeConnectionModel = gson.fromJson(response, ActiveConnectionModel.class);
                            List<ActiveConnectionModel.Result> itemsData;
                            itemsData = activeConnectionModel.getsResult();

                            view.showActiveList(itemsData);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void loadFailedList() {
        Observable<ResponseBody> failedConnectionList = remote.getFailedConnections(view.getToken());
        failedConnectionList.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            Gson gson = new Gson();
                            FailedConnectionModel failedConnectionModel = gson.fromJson(responseBody.string(), FailedConnectionModel.class);
                            List<FailedConnectionModel.UsersFailCount> itemsData;
                            itemsData = failedConnectionModel.getUsersFailCounts();

                            view.showFailedList(itemsData);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
