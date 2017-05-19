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
import android.widget.TextView;

import com.veerasystem.crust.data.FailedSessionModel;
import com.veerasystem.crust.R;

import java.util.List;

class FailedAdapter extends RecyclerView.Adapter<FailedAdapter.MyViewHolder> {

    private static final String TAG = "FailedSessionAdapter";

    private List<FailedSessionModel.SessionFailCount> sessionListModels;

    void updateModel(List<FailedSessionModel.SessionFailCount> models) {
        this.sessionListModels = models;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sessionfailedlistrow, null);
        MyViewHolder myViewHolder = new MyViewHolder(itemLayoutView);
        return myViewHolder;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder: IP " + String.valueOf(sessionListModels.get(position).getClientIp()));
        holder.remoteUserTextView.setText(sessionListModels.get(position).getRemoteUser());
        holder.ipTextView.setText(String.valueOf(sessionListModels.get(position).getClientIp()));
        holder.connectionTextView.setText(sessionListModels.get(position).getServerAccount());
        holder.reasonTextView.setText(String.valueOf(sessionListModels.get(position).getFailReason()));
        holder.countTextView.setText("Count: " + String.valueOf(sessionListModels.get(position).getTotal()));
    }

    @Override
    public int getItemCount() {
        try {
            Log.i(TAG, "getItemCount: " + sessionListModels.size());
            return sessionListModels.size();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView remoteUserTextView;
        TextView ipTextView;
        TextView connectionTextView;
        TextView reasonTextView;
        TextView countTextView;

        MyViewHolder(View view) {
            super(view);

            remoteUserTextView = (TextView) view.findViewById(R.id.sessionFailedRemoteUser);
            ipTextView = (TextView) view.findViewById(R.id.sessionFailedIp);
            connectionTextView = (TextView) view.findViewById(R.id.sessionFailedConnection);
            reasonTextView = (TextView) view.findViewById(R.id.sessionFailedReason);
            countTextView = (TextView) view.findViewById(R.id.sessionFailedCount);
        }
    }
}
