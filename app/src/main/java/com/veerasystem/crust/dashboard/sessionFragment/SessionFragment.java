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

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.veerasystem.crust.R;
import com.veerasystem.crust.data.ActiveSessionModel;
import com.veerasystem.crust.data.FailedSessionModel;

import java.util.List;

public class SessionFragment extends Fragment implements SessionContract.View, FilterDialogFragment.OnFilterListener {

    private static final String TAG = "SessionFragment";

    private SessionContract.Presenter presenter;

    private FilterDialogFragment filterDialogFragment;

    private ActiveAdapter activeAdapter;
    private FailedAdapter failedSessionAdapter;

    private RecyclerView activeSRecyclerView;
    private RecyclerView failedSRecyclerView;

    private SwipeRefreshLayout activeSwipeRefresh;
    private SwipeRefreshLayout failedSwipeRefresh;

    private LinearLayout filterSessionLayout;
    private Button activeButton;
    private Button failedButton;

    public static SessionFragment newInstance() {
        return new SessionFragment();
    }

    @Override
    public void setPresenter(SessionContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_session, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activeSRecyclerView = (RecyclerView) view.findViewById(R.id.sessionActiveRecyclerView);
        failedSRecyclerView = (RecyclerView) view.findViewById(R.id.sessionFailedRecyclerView);

        activeSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshSessionActiveLayout);
        failedSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshSessionFailedLayout);

        activeSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadActiveList();
                activeSwipeRefresh.setRefreshing(false);
            }
        });

        failedSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadFailedList();
                failedSwipeRefresh.setRefreshing(false);
            }
        });

        activeSRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        failedSRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        activeAdapter = new ActiveAdapter(this);
        activeSRecyclerView.setAdapter(activeAdapter);

        failedSessionAdapter = new FailedAdapter();
        failedSRecyclerView.setAdapter(failedSessionAdapter);

        activeButton = (Button) view.findViewById(R.id.activeSessionButton);
        activeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeButton.setTextColor(Color.WHITE); //Selected
                activeButton.setText(activeButton.getText().toString().replace("  ▼", "")); //Remove if exist
                activeButton.setText(activeButton.getText() + "  ▼");
                failedButton.setTextColor(Color.GRAY);
                failedButton.setText(failedButton.getText().toString().replace("  ▼", ""));

                failedButton.setShadowLayer(1, 0, 1, Color.DKGRAY);

                filterSessionLayout.setVisibility(View.VISIBLE);
                activeSwipeRefresh.setVisibility(View.VISIBLE);
                failedSwipeRefresh.setVisibility(View.GONE);
            }
        });

        failedButton = (Button) view.findViewById(R.id.failedSessionButton);
        failedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeButton.setTextColor(Color.GRAY);
                failedButton.setText(failedButton.getText().toString().replace("  ▼", ""));  //Remove if exist
                failedButton.setText(failedButton.getText() + "  ▼");
                activeButton.setText(activeButton.getText().toString().replace("  ▼", ""));
                activeButton.setShadowLayer(1, 0, 1, Color.DKGRAY);
                failedButton.setTextColor(Color.WHITE); //Selected

                filterSessionLayout.setVisibility(View.GONE);
                activeSwipeRefresh.setVisibility(View.GONE);
                failedSwipeRefresh.setVisibility(View.VISIBLE);
            }
        });

        filterSessionLayout = (LinearLayout) view.findViewById(R.id.filterSessionLayout);

        //Default font theme for buttons to show selected one
        activeButton.setText(activeButton.getText() + "  ▼");
        activeButton.setTextColor(Color.WHITE);
        failedButton.setTextColor(Color.GRAY);
        failedButton.setShadowLayer(1, 0, 1, Color.DKGRAY);

        filterDialogFragment = new FilterDialogFragment();
        filterDialogFragment.setTargetFragment(this, 0);

        ImageButton filterActiveButton;
        filterActiveButton = (ImageButton) view.findViewById(R.id.filterActiveSessionButton);
        filterActiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialogFragment.show(getActivity().getSupportFragmentManager(), "Filter");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    public void killSession(int id) {
        presenter.killSession(id);
    }

    public void sendMessage(int id, String message) {
        presenter.messageSession(message, id);
    }

    @Override
    public String getToken() {
        SharedPreferences pref = getActivity().getSharedPreferences("CRUST", 1);
        return pref.getString("TOKEN", "");
    }

    @Override
    public void showActiveList(List<ActiveSessionModel.Result> models) {
        activeAdapter.updateModel(models);

        //Updating Title to show count
        activeButton.setText("Active (" + models.size() + ")");
        activeButton.setText(activeButton.getText().toString().replace("  ▼", "")); //Remove if exist
        activeButton.setText(activeButton.getText() + "  ▼");

        activeAdapter.notifyDataSetChanged();
    }

    @Override
    public void showFailedList(List<FailedSessionModel.SessionFailCount> models) {
        failedSessionAdapter.updateModel(models);

        //Updating Title to show count
        failedButton.setText("Failed (" + models.size() + ")");

        failedSessionAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFilterAdded(String sourceIp, String remoteUser, String server) {
        presenter.loadFilteredActiveList(sourceIp, remoteUser, server);
    }
}
