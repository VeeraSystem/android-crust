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

package com.veerasystem.crust.main;

import com.veerasystem.crust.data.source.remote.Remote;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class Presenter implements CrustContractor.Presenter {

    private Remote remote;
    private CrustContractor.View view;

    @Inject
    Presenter(Remote remote, CrustContractor.View view) {
        this.remote = remote;
        this.view = view;

        view.setPresenter(this);
    }

    @Override
    public void start() {
    }

    @Override
    public void signOut() {
        Observable<ResponseBody> logoutResponce = remote.logout(view.getToken());
        logoutResponce.subscribeOn(Schedulers.newThread())
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
                        view.showLoginPage();
                    }
                });
    }
}
