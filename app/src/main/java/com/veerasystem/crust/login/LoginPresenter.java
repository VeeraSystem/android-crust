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

package com.veerasystem.crust.login;

import android.util.Log;

import com.google.gson.Gson;
import com.veerasystem.crust.data.ErrorModel;
import com.veerasystem.crust.data.source.remote.Remote;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.ResponseBody;
import retrofit2.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginPresenter implements LoginContract.Presenter {

    LoginContract.View view;
    Remote remote;

    private Subscription remoteSub;

    public LoginPresenter(Remote remote, LoginContract.View view) {
        this.remote = remote;
        this.view = view;

        view.setPresenter(this);
    }

    @Override
    public void checkAuth(String token) {
        if (!token.isEmpty())
            view.showMainView();
    }

    @Override
    public void getOTP(final String serverAddress, final String username, final String password) {

        view.showProgressIndicator(true);
        remote.setup(serverAddress);

        Observable<ResponseBody> loginResponse = remote.getOtp(username, password);
        remoteSub = loginResponse.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        view.showProgressIndicator(false);
                        view.showMessage("Wait for an SMS");
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showProgressIndicator(false);
                        e.printStackTrace();
                        String errorMessage = "Error on Login";

                        //under control! don't touch this code
                        if (e instanceof SocketTimeoutException) {
                            //move to OTP page, timeout
                        } else {
                            if (e instanceof HttpException) {
                                try {
                                    Gson gson = new Gson();
                                    ErrorModel error = gson.fromJson(((HttpException) e).response().errorBody().string(), ErrorModel.class);
                                    errorMessage = error.getError();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            }

                            view.showMessage(errorMessage);
                        }
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        //show OTP page
                        try {
                            Log.i("Response", "onNext: " + responseBody.string().toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        view.showOtpPage(serverAddress, username, password);
                    }
                });
    }

    @Override
    public void unsubscribe() {
        if (remoteSub != null)
            remoteSub.unsubscribe();
    }

    @Override
    public void start() {

    }
}
