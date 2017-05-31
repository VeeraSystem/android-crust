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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.veerasystem.crust.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LoginFragment extends Fragment implements LoginContract.View {

    LoginContract.Presenter presenter;

    FragmentListener listener;

    SharedPreferences pref;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.serverAddress)
    EditText serverAddress;
    @BindView(R.id.username)
    EditText usernameEditText;
    @BindView(R.id.password)
    EditText passwordEditText;
    @BindView(R.id.loginButton)
    Button loginButton;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    public void setFragmentListener(FragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);

        ButterKnife.bind(this, root);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getOTP(serverAddress.getText().toString(), usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        checkTokenAvailability();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unsubscribe();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private void checkTokenAvailability() {
        pref = getActivity().getApplicationContext().getSharedPreferences("CRUST", 0);
        presenter.checkAuth(pref.getString("TOKEN", ""));
    }

    @Override
    public void showProgressIndicator(boolean active) {
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(active ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showOtpPage(String hostIP, String username, String password) {
        Bundle values = new Bundle();
        values.putString("HOSTIP", hostIP);
        values.putString("USERNAME", username);
        values.putString("PASSWORD", password);
        listener.loadOtpPage(values);
    }

    @Override
    public void showMainView() {
        listener.showMainView();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    interface FragmentListener {
        void loadOtpPage(Bundle values);

        void showMainView();
    }
}
