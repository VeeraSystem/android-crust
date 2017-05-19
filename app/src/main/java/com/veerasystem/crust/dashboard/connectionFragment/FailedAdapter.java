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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.veerasystem.crust.data.FailedConnectionModel;
import com.veerasystem.crust.R;

import java.util.List;

class FailedAdapter extends RecyclerView.Adapter<FailedAdapter.MyViewHolder> {

    private List<FailedConnectionModel.UsersFailCount> connectionListModels;

    void updateModel(List<FailedConnectionModel.UsersFailCount> models) {
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

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView ipTextView;
        TextView failReasonTextView;
        TextView countTextView;
        TextView stateTextView;

        MyViewHolder(View view) {
            super(view);

            usernameTextView = (TextView) view.findViewById(R.id.connectionFailedUsername);
            ipTextView = (TextView) view.findViewById(R.id.connectionFailedIp);
            failReasonTextView = (TextView) view.findViewById(R.id.connectionFailedFailReason);
            countTextView = (TextView) view.findViewById(R.id.connectionFailedCount);
            stateTextView = (TextView) view.findViewById(R.id.connectionFailedState);
        }
    }
}
