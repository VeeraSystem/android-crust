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

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.veerasystem.crust.R;

import butterknife.BindView;

public class OtpFragment extends Fragment implements OtpContract.View {

    private static final String TAG = "OtpFragment";

    private OtpFragmentListener listener;
    private OtpContract.Presenter presenter;

    String username, password, ipAddress;

    @BindView(R.id.otp)
    EditText otpEditText;
    @BindView(R.id.otpButton)
    Button otpButton;

    public static OtpFragment newInstance() {
        return new OtpFragment();
    }

    @Override
    public void setPresenter(OtpContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public void setFragmentListener(OtpFragmentListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_otp, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            ipAddress = getArguments().getString("HOSTIP");
            username = getArguments().getString("USERNAME");
            password = getArguments().getString("PASSWORD");
        }

        otpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.login(username, password, otpEditText.getText().toString());
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach: ");
    }

    @Override
    public void showProgressIndicator(boolean active) {

    }

    @Override
    public void showMainView() {
        listener.showMainView();
    }

    @Override
    public void storeUserDetail(String username, String token, String host) {

        SharedPreferences pref = getActivity().getSharedPreferences("CRUST", 0);
        final SharedPreferences.Editor editor = pref.edit();

        editor.putString("Username", username);
        editor.putString("TOKEN", "Token " + token);
        editor.putString("SERVERADDRESS", host);

        editor.apply();
    }

    public interface OtpFragmentListener {
        void showMainView();
    }

}
