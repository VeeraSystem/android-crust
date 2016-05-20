/*************************************************************************
 *
 * Veera CONFIDENTIAL
 * __________________
 *
 *  [2016] Veera System Incorporated
 *  All Rights Reserved.
 *
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

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.veerasystem.crust.Fragment.SessionFragment;
import com.veerasystem.crust.Model.ActiveSessionModel;
import com.veerasystem.crust.R;

import org.w3c.dom.Text;

import java.util.List;

public class ActiveSessionAdapter extends RecyclerView.Adapter<ActiveSessionAdapter.MyViewHolder> {

    private SessionFragment mSessionFragment;
    private List<ActiveSessionModel.Result> sessionListModels;

    public ActiveSessionAdapter(SessionFragment mSessionFragment, List<ActiveSessionModel.Result> models) {
        this.mSessionFragment = mSessionFragment;
        this.sessionListModels = models;
    }

    public void updateModel(List<ActiveSessionModel.Result> models) {
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
        holder.startTextView.setText(sessionListModels.get(position).getmCreatedAt());
        holder.ipTextView.setText(String.valueOf(sessionListModels.get(position).getmClientIp()));
        holder.remoteUserTextView.setText(sessionListModels.get(position).getmRemoteUser());
        holder.pIdTextView.setText("PID: " + String.valueOf(sessionListModels.get(position).getmPid()));
        holder.stateTextView.setText(sessionListModels.get(position).getmFailReason());
        holder.sessionConnectionTextView.setText(sessionListModels.get(position).getmServerAccount()+"@"+sessionListModels.get(position).getmServer());

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

    public  class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView startTextView;
        public TextView ipTextView;
        public TextView remoteUserTextView;
        public TextView pIdTextView;
        public TextView stateTextView;
        public TextView sessionConnectionTextView;
        public Button killSessionButton;
        public Button messageSessionButton;
        public Button sessionSendMessageButton;
        public Button sessionCancelMessageButton;

        public EditText sessionMessageTextView;

        public LinearLayout llExpandArea;
        public LinearLayout llExpandAreaMessage;


        public MyViewHolder(View view) {
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
