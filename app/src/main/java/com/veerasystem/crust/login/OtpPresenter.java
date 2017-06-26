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

import com.google.gson.Gson;
import com.veerasystem.crust.data.ErrorModel;
import com.veerasystem.crust.data.TokenModel;
import com.veerasystem.crust.data.source.remote.Remote;

import java.io.IOException;

import javax.inject.Inject;

import retrofit2.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class OtpPresenter implements OtpContract.Presenter {

    OtpContract.View view;
    Remote remote;

    @Inject
    OtpPresenter(Remote remote, OtpContract.View view) {
        this.remote = remote;
        this.view = view;

        view.setPresenter(this);
    }

    @Override
    public void login(final String username, String password, String otp) {

        view.showProgressIndicator(true);

        Observable<TokenModel> loginResponse = remote.login(username, password, otp);
        loginResponse.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TokenModel>() {
                    @Override
                    public void onCompleted() {
                        view.showProgressIndicator(false);
                        view.showMainView();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        String errorMessage = "Error on Login";
                        if (e instanceof HttpException) {
                            try {
                                Gson gson = new Gson();
                                ErrorModel error = gson.fromJson(((HttpException) e).response().errorBody().string(), ErrorModel.class);
                                errorMessage = error.getError();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }

//                        Toast toast = Toast.makeText(com.veerasystem.crust.Login.LoginActivity.this, errorMessage, Toast.LENGTH_SHORT);
//                        toast.getView().setBackgroundColor(Color.parseColor("#607D8B"));
//                        toast.show();
                    }

                    @Override
                    public void onNext(TokenModel model) {
                        view.storeUserDetail(username, model.getToken(), remote.currentServerAddress());
                    }
                });
    }


    @Override
    public void start() {

    }
}
