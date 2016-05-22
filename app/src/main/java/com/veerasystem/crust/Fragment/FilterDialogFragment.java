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

package com.veerasystem.crust.Fragment;

import android.app.Dialog;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.veerasystem.crust.R;

public class FilterDialogFragment extends android.support.v4.app.DialogFragment {

    private EditText sourceIPFilterEditText;
    private EditText remoteUserFilterEditText;
    private EditText serverFilterEditText;

    private OnFilterListener callback;

    public interface OnFilterListener {
        void onFilterAdded(String sourceIp, String remoteUser, String server);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        try {
            callback = (OnFilterListener) getTargetFragment();
        }
        catch (ClassCastException e) {
            throw  new ClassCastException("Calling fragment must implement onFilterListner");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder filterBuilder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();


        View view = inflater.inflate(R.layout.filter_dialog, null);

        sourceIPFilterEditText = (EditText) view.findViewById(R.id.filterSourceIp);
        remoteUserFilterEditText = (EditText) view.findViewById(R.id.filterRemoteUser);
        serverFilterEditText = (EditText) view.findViewById(R.id.filterServer);

        filterBuilder.setView(view)
                .setPositiveButton("Filter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onFilterAdded(sourceIPFilterEditText.getText().toString(),remoteUserFilterEditText.getText().toString(),
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
}
