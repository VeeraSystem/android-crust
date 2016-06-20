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

import com.veerasystem.crust.Model.FailedConnectionModel;
import com.veerasystem.crust.R;

import java.util.List;

public class FailedConnectionAdapter extends RecyclerView.Adapter<FailedConnectionAdapter.MyViewHolder> {

    private List<FailedConnectionModel.UsersFailCount> connectionListModels;

    public FailedConnectionAdapter(List<FailedConnectionModel.UsersFailCount> models) {
        this.connectionListModels = models;
    }

    public void updateModel(List<FailedConnectionModel.UsersFailCount> models) {
        this.connectionListModels = models;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.connectionfailedlistrow, null);
        MyViewHolder myViewHolder = new MyViewHolder(itemLayoutView);
        return myViewHolder;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.usernameTextView.setText(connectionListModels.get(position).getUsername());
        holder.ipTextView.setText(String.valueOf(connectionListModels.get(position).getSourceIp()));
        holder.failReasonTextView.setText(connectionListModels.get(position).getFailReason());
        holder.countTextView.setText("Count: " + String.valueOf(connectionListModels.get(position).getTotal()));
        holder.stateTextView.setText(connectionListModels.get(position).getState());
    }

    @Override
    public int getItemCount() {
        try {
            return connectionListModels.size();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView usernameTextView;
        public TextView ipTextView;
        public TextView failReasonTextView;
        public TextView countTextView;
        public TextView stateTextView;

        public MyViewHolder(View view) {
            super(view);

            usernameTextView = (TextView) view.findViewById(R.id.connectionFailedUsername);
            ipTextView = (TextView) view.findViewById(R.id.connectionFailedIp);
            failReasonTextView = (TextView) view.findViewById(R.id.connectionFailedFailReason);
            countTextView = (TextView) view.findViewById(R.id.connectionFailedCount);
            stateTextView = (TextView) view.findViewById(R.id.connectionFailedState);
        }
    }
}
