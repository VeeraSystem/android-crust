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

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.veerasystem.crust.Model.ActiveConnectionModel.Result;
import com.veerasystem.crust.R;

import java.util.List;

public class ActiveConnectionAdapter extends RecyclerView.Adapter<ActiveConnectionAdapter.MyViewHolder> {

    private List<Result> connectionListModels;

    public ActiveConnectionAdapter(List<Result> models) {
        this.connectionListModels = models;
    }

    public void updateModel(List<Result> models) {
        this.connectionListModels = models;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.connectionlistrow, null);
        MyViewHolder myViewHolder = new MyViewHolder(itemLayoutView);
        return myViewHolder;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.startTextView.setText(connectionListModels.get(position).getmCreatedAt());
        holder.ipTextView.setText(String.valueOf(connectionListModels.get(position).getmSourceIp()));
        holder.remoteUserTextView.setText(connectionListModels.get(position).getmUsername());
        holder.pIdTextView.setText("PID: " + String.valueOf(connectionListModels.get(position).getmPid()));
        holder.stateTextView.setText(connectionListModels.get(position).getmState());
    }

    @Override
    public int getItemCount() {
        try {
            return connectionListModels.size();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView startTextView;
        TextView ipTextView;
        TextView remoteUserTextView;
        TextView pIdTextView;
        TextView stateTextView;

        MyViewHolder(View view) {
            super(view);

            Typeface ipFont = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/expressway_rg.ttf");

            startTextView = (TextView) view.findViewById(R.id.connectionStart);
            ipTextView = (TextView) view.findViewById(R.id.connectionIp);
            ipTextView.setTypeface(ipFont);
            remoteUserTextView = (TextView) view.findViewById(R.id.connectionRemoteUser);
            remoteUserTextView.setTypeface(ipFont);
            pIdTextView = (TextView) view.findViewById(R.id.connectionPid);
            stateTextView = (TextView) view.findViewById(R.id.connectionState);
        }
    }
}
