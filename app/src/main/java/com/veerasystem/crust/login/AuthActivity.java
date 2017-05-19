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

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.veerasystem.crust.MainActivity;
import com.veerasystem.crust.R;
import com.veerasystem.crust.data.source.remote.Remote;
import com.veerasystem.crust.utils.ActivityUtils;

public class AuthActivity extends AppCompatActivity {

    private static final String TAG = "AuthActivity";

    LoginFragment loginFragment;
    OtpFragment otpFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag("LOGIN");

        if (loginFragment == null) {
            loginFragment = LoginFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), loginFragment, R.id.contentFrame, "LOGIN");
        }

        if (otpFragment == null) {
            otpFragment = OtpFragment.newInstance();
        }

        loginFragment.setFragmentListener(new LoginFragment.FragmentListener() {
            @Override
            public void loadOtpPage(Bundle values) {
                replaceFragment(values);
            }

            @Override
            public void showMainView() {
                showMainActivity();
            }
        });

        otpFragment.setFragmentListener(new OtpFragment.OtpFragmentListener() {
            @Override
            public void showMainView() {
                showMainActivity();
            }
        });

        Remote remote = Remote.getINSTANCE();

        new LoginPresenter(remote, loginFragment);

        new OtpPresenter(remote, otpFragment);
    }

    private void showMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void replaceFragment(Bundle values) {
        otpFragment.setArguments(values);
        ActivityUtils.replaceFragment(getSupportFragmentManager(), otpFragment, R.id.contentFrame);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else
            finish();
    }
}
