/*************************************************************************
 * Veera CONFIDENTIAL
 * __________________
 * <p>
 * [2016] Veera System Incorporated
 * All Rights Reserved.
 * <p>
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

package com.veerasystem.crust.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.veerasystem.crust.Model.FailedSessionModel;
import com.veerasystem.crust.R;

import java.util.List;

public class FailedSessionAdapter extends RecyclerView.Adapter<FailedSessionAdapter.MyViewHolder> {

    private List<FailedSessionModel.SessionFailCount> sessionListModels;

    public FailedSessionAdapter(List<FailedSessionModel.SessionFailCount> models) {
        this.sessionListModels = models;
    }

    public void updateModel(List<FailedSessionModel.SessionFailCount> models) {
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

        holder.remoteUserTextView.setText(sessionListModels.get(position).getRemoteUser());
        holder.ipTextView.setText(String.valueOf(sessionListModels.get(position).getClientIp()));
        holder.connectionTextView.setText(sessionListModels.get(position).getServerAccount());
        holder.reasonTextView.setText(String.valueOf(sessionListModels.get(position).getFailReason()));
        holder.countTextView.setText("Count: " + String.valueOf(sessionListModels.get(position).getTotal()));
    }

    @Override
    public int getItemCount() {
        try {
            return sessionListModels.size();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView remoteUserTextView;
        public TextView ipTextView;
        public TextView connectionTextView;
        public TextView reasonTextView;
        public TextView countTextView;

        public MyViewHolder(View view) {
            super(view);

            remoteUserTextView = (TextView) view.findViewById(R.id.sessionFailedRemoteUser);
            ipTextView = (TextView) view.findViewById(R.id.sessionFailedIp);
            connectionTextView = (TextView) view.findViewById(R.id.sessionFailedConnection);
            reasonTextView = (TextView) view.findViewById(R.id.sessionFailedReason);
            countTextView = (TextView) view.findViewById(R.id.sessionFailedCount);
        }
    }
}
