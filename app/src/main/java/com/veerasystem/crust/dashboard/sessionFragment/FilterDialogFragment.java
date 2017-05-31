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

package com.veerasystem.crust.dashboard.sessionFragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.veerasystem.crust.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterDialogFragment extends android.support.v4.app.DialogFragment {

    @BindView(R.id.filterSourceIp)
    EditText sourceIPFilterEditText;
    @BindView(R.id.filterRemoteUser)
    EditText remoteUserFilterEditText;
    @BindView(R.id.filterServer)
    EditText serverFilterEditText;

    private OnFilterListener callback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            callback = (OnFilterListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement onFilterListner");
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder filterBuilder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.filter_dialog, null);

        ButterKnife.bind(this, view);

        filterBuilder.setView(view)
                .setPositiveButton("Filter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onFilterAdded(sourceIPFilterEditText.getText().toString(), remoteUserFilterEditText.getText().toString(),
                                serverFilterEditText.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Crust", "Cancel");
                    }
                });

        return filterBuilder.create();
    }

    public interface OnFilterListener {
        void onFilterAdded(String sourceIp, String remoteUser, String server);
    }
}
