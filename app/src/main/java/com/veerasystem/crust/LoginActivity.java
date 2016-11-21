/*************************************************************************
 * Veera CONFIDENTIAL
 * __________________
 * <p/>
 * [2016] Veera System Incorporated
 * All Rights Reserved.
 * <p/>
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

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.veerasystem.crust.Model.TokenModel;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {
    EditText serverAddress;
    EditText usernameEditText;
    EditText passwordEditText;
    EditText otpEditText;

    Button otpButton;
    Button loginButton;

    CrustServiceAPI crustService;
    Gson gson;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pref = getApplicationContext().getSharedPreferences("CRUST", 0);
        editor = pref.edit();

        if (!pref.getString("TOKEN", "").isEmpty()) {
            Intent main = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(main);
            //We need to finish this activity so pressing back on MainActivity wouldn't return to this activity
            finish();
        }

        serverAddress = (EditText) findViewById(R.id.serverAddress);
        usernameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);
        otpEditText = (EditText) findViewById(R.id.otp);
        otpButton = (Button) findViewById(R.id.otpButton);
        loginButton = (Button) findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder()
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .baseUrl("http://" + serverAddress.getText().toString() + "/api/v1/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                gson = new Gson();

                crustService = retrofit.create(CrustServiceAPI.class);
                getOTP();
            }
        });

        otpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (otpEditText.getVisibility() == View.VISIBLE) {
            serverAddress.setVisibility(View.VISIBLE);
            usernameEditText.setVisibility(View.VISIBLE);
            passwordEditText.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.VISIBLE);
            otpEditText.setVisibility(View.GONE);
            otpButton.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    private void getOTP() {
        Toast toast = Toast.makeText(LoginActivity.this, "   Please Wait ...    ", Toast.LENGTH_SHORT);
        toast.getView().setBackgroundColor(Color.parseColor("#607D8B"));
        toast.show();

        Observable<ResponseBody> loginResponse = crustService.loginOtpRequest(usernameEditText.getText().toString(), passwordEditText.getText().toString());
        loginResponse.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        String errorMessage = "Error on Login";

                        //under control! don't touch this code
                        if (e instanceof SocketTimeoutException) {
                            serverAddress.setVisibility(View.GONE);
                            usernameEditText.setVisibility(View.GONE);
                            passwordEditText.setVisibility(View.GONE);
                            loginButton.setVisibility(View.GONE);
                            otpEditText.setVisibility(View.VISIBLE);
                            otpButton.setVisibility(View.VISIBLE);
                        } else {
                            try {
                                Gson gson = new Gson();
                                ErrorModel error = gson.fromJson(((HttpException) e).response().errorBody().string(), ErrorModel.class);
                                errorMessage = error.getError();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }

                            Toast toast = Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT);
                            toast.getView().setBackgroundColor(Color.parseColor("#607D8B"));
                            toast.show();
                        }
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        serverAddress.setVisibility(View.GONE);
                        usernameEditText.setVisibility(View.GONE);
                        passwordEditText.setVisibility(View.GONE);
                        loginButton.setVisibility(View.GONE);
                        otpEditText.setVisibility(View.VISIBLE);
                        otpButton.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void login() {
        Observable<ResponseBody> loginResponse = crustService.login(usernameEditText.getText().toString(), passwordEditText.getText().toString(), otpEditText.getText().toString());
        loginResponse.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        String errorMessage = "Error on Login";
                        try {
                            Gson gson = new Gson();
                            ErrorModel error = gson.fromJson(((HttpException) e).response().errorBody().string(), ErrorModel.class);
                            errorMessage = error.getError();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                        Toast toast = Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT);
                        toast.getView().setBackgroundColor(Color.parseColor("#607D8B"));
                        toast.show();
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            TokenModel model = gson.fromJson(responseBody.string(), TokenModel.class);

                            Intent main = new Intent(LoginActivity.this, MainActivity.class);

                            editor.putString("Username", usernameEditText.getText().toString());
                            editor.putString("TOKEN", "Token " + model.getToken());
                            editor.putString("SERVERADDRESS", serverAddress.getText().toString());
                            editor.commit();

                            startActivity(main);

                            //We need to finish this activity so pressing back on MainActivity wouldn't return to this activity
                            finish();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
