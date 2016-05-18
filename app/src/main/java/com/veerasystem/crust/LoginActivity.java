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

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.veerasystem.crust.Model.TokenModel;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
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
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("CRUST", 0);
        final SharedPreferences.Editor editor = pref.edit();

        if (!pref.getString("TOKEN", "").isEmpty()) {
            Intent main = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(main);
            //We need to finish this activity so pressing back on MainActivity wouldn't return to this activity
            finish();
        }

        serverAddress = (EditText) findViewById(R.id.serverAddress);
        usernameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Retrofit retrofit = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl("http://"+serverAddress.getText().toString()+"/api/v1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();


                final Gson gson = new Gson();

                CrustServiceAPI crustService = retrofit.create(CrustServiceAPI.class);

                Observable<ResponseBody> loginResponse = crustService.login(usernameEditText.getText().toString(), passwordEditText.getText().toString());
                loginResponse.subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<ResponseBody>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(ResponseBody responseBody) {
                                try {
                                    TokenModel model = gson.fromJson(responseBody.string(), TokenModel.class);

                                    Intent main = new Intent(LoginActivity.this, MainActivity.class);
//
//                                    //Sending Token and serverAddress to mainActivity
//                                    main.putExtra("TOKEN",model.getToken());
//                                    main.putExtra("SERVERADDRESS", serverAddress.getText().toString());

                                    editor.putString("Username", usernameEditText.getText().toString());
                                    editor.putString("TOKEN","Token "+model.getToken());
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
        });
    }
}
