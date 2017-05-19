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

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.veerasystem.crust.R;
import com.veerasystem.crust.data.ActiveSessionModel;

import java.util.List;

class ActiveAdapter extends RecyclerView.Adapter<ActiveAdapter.MyViewHolder> {

    private static final String TAG = "ActiveSessionAdapter";

    private SessionFragment mSessionFragment;
    private List<ActiveSessionModel.Result> sessionListModels;

    ActiveAdapter(SessionFragment mSessionFragment) {
        this.mSessionFragment = mSessionFragment;
    }

    void updateModel(List<ActiveSessionModel.Result> models) {
        this.sessionListModels = models;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sessionlistrow, null);
        MyViewHolder myViewHolder = new MyViewHolder(itemLayoutView);
        return myViewHolder;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Log.i(TAG, "onBindViewHolder: " + String.valueOf(sessionListModels.get(position).getmClientIp()));
        holder.startTextView.setText(sessionListModels.get(position).getmCreatedAt());
        holder.ipTextView.setText(String.valueOf(sessionListModels.get(position).getmClientIp()));
        holder.remoteUserTextView.setText(sessionListModels.get(position).getmRemoteUser());
        holder.pIdTextView.setText("PID: " + String.valueOf(sessionListModels.get(position).getmPid()));
        holder.stateTextView.setText(sessionListModels.get(position).getmFailReason());
        holder.sessionConnectionTextView.setText(sessionListModels.get(position).getmServerAccount() + "@" + sessionListModels.get(position).getmServer());

        holder.killSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSessionFragment.killSession(sessionListModels.get(position).getmId());
            }
        });

        holder.messageSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.llExpandArea.setVisibility(View.GONE);
                holder.llExpandAreaMessage.setVisibility(View.VISIBLE);
            }
        });

        holder.sessionCancelMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.llExpandArea.setVisibility(View.VISIBLE);
                holder.llExpandAreaMessage.setVisibility(View.GONE);
            }
        });

        holder.sessionSendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = sessionListModels.get(position).getmId();
                String message = holder.sessionMessageTextView.getText().toString();

                mSessionFragment.sendMessage(id, message);

                //Clear Message
                holder.sessionMessageTextView.setText("");
            }
        });
    }

    @Override
    public int getItemCount() {
        try {
            return sessionListModels.size();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView startTextView;
        TextView ipTextView;
        TextView remoteUserTextView;
        TextView pIdTextView;
        TextView stateTextView;
        TextView sessionConnectionTextView;
        Button killSessionButton;
        Button messageSessionButton;
        Button sessionSendMessageButton;
        Button sessionCancelMessageButton;

        EditText sessionMessageTextView;

        LinearLayout llExpandArea;
        LinearLayout llExpandAreaMessage;


        MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            startTextView = (TextView) view.findViewById(R.id.sessionStart);
            ipTextView = (TextView) view.findViewById(R.id.sessionIp);
            remoteUserTextView = (TextView) view.findViewById(R.id.sessionRemoteUser);
            pIdTextView = (TextView) view.findViewById(R.id.sessionPid);
            stateTextView = (TextView) view.findViewById(R.id.sessionState);
            sessionConnectionTextView = (TextView) view.findViewById(R.id.sessionConnection);

            killSessionButton = (Button) view.findViewById(R.id.killSessionButton);
            messageSessionButton = (Button) view.findViewById(R.id.messageSessionButton);

            sessionCancelMessageButton = (Button) view.findViewById(R.id.sessionCancelMessageButton);
            sessionSendMessageButton = (Button) view.findViewById(R.id.sessionSendMessageButton);

            sessionMessageTextView = (EditText) view.findViewById(R.id.sessionMessageTextView);

            llExpandArea = (LinearLayout) view.findViewById(R.id.llExpandArea);
            llExpandAreaMessage = (LinearLayout) view.findViewById(R.id.llExpandAreaMessage);
        }

        @Override
        public void onClick(View v) {
            if (llExpandAreaMessage.getVisibility() == View.VISIBLE)
                llExpandAreaMessage.setVisibility(View.GONE);

            if (llExpandArea.getVisibility() == View.VISIBLE)
                llExpandArea.setVisibility(View.GONE);
            else
                llExpandArea.setVisibility(View.VISIBLE);
        }
    }
}
