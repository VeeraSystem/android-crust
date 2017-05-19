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

package com.veerasystem.crust.dashboard.connectionFragment;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.veerasystem.crust.data.ActiveConnectionModel.Result;
import com.veerasystem.crust.R;

import java.util.List;

class ActiveAdapter extends RecyclerView.Adapter<ActiveAdapter.MyViewHolder> {

    private List<Result> connectionListModels;

    void updateModel(List<Result> models) {
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
