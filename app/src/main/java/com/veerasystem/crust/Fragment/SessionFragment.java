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

import android.graphics.Color;
import android.os.Bundle;
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

import com.google.gson.Gson;
import com.veerasystem.crust.Adapter.ActiveSessionAdapter;
import com.veerasystem.crust.Adapter.FailedSessionAdapter;
import com.veerasystem.crust.CrustServiceAPI;
import com.veerasystem.crust.MainActivity;
import com.veerasystem.crust.Model.ActiveSessionModel;
import com.veerasystem.crust.Model.FailedSessionModel;
import com.veerasystem.crust.R;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SessionFragment extends Fragment implements FilterDialogFragment.OnFilterListener{

    FilterDialogFragment filterDialogFragment;

    private ActiveSessionAdapter activeSessionAdapter;
    private FailedSessionAdapter failedSessionAdapter;

    RecyclerView activeSRecyclerView;
    RecyclerView failedSRecyclerView;

    SwipeRefreshLayout activeSwipeRefresh;
    SwipeRefreshLayout failedSwipeRefresh;

    private List<ActiveSessionModel.Result> modelsActive;
    private List<FailedSessionModel.SessionFailCount> modelsFailed;

    private Button activeButton;
    private Button failedButton;

    private ImageButton filterActiveButton;

    public SessionFragment() {
        // Required empty public constructor
    }

    public void killSession(int id) {
        ((MainActivity)getActivity()).killSession(id);
    }

    public void sendMessage(int id, String message) {
        ((MainActivity)getActivity()).sendMessage(message, id);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void updateListActive(List<ActiveSessionModel.Result> models) {
        this.modelsActive = models;
        reloadActiveAdapter();
    }

    public void updateListFailed(List<FailedSessionModel.SessionFailCount> models) {
        this.modelsFailed = models;
        reloadFailedAdapter();
    }

    private void reloadActiveAdapter() {
        activeSessionAdapter.updateModel(this.modelsActive);

        //Updating Title to show count
        activeButton.setText("Active (" + this.modelsActive.size() + ")");
        activeButton.setText(activeButton.getText().toString().replace("  ▼", "")); //Remove if exist
        activeButton.setText(activeButton.getText() + "  ▼");

        activeSessionAdapter.notifyDataSetChanged();
    }

    private void reloadFailedAdapter() {
        failedSessionAdapter.updateModel(this.modelsFailed);

        //Updating Title to show count
        failedButton.setText("Failed (" + this.modelsFailed.size() + ")");

        failedSessionAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_session, container, false);

        activeSRecyclerView = (RecyclerView) rootView.findViewById(R.id.sessionActiveRecyclerView);
        failedSRecyclerView = (RecyclerView) rootView.findViewById(R.id.sessionFailedRecyclerView);

        activeSwipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshSessionActiveLayout);
        failedSwipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshSessionFailedLayout);

        activeSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((MainActivity) getActivity()).reloadAllList();
                activeSwipeRefresh.setRefreshing(false);
            }
        });

        failedSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((MainActivity) getActivity()).reloadAllList();
                failedSwipeRefresh.setRefreshing(false);
            }
        });

        //Don't show by default
        failedSRecyclerView.setVisibility(View.GONE);

        activeSRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        failedSRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        activeSessionAdapter = new ActiveSessionAdapter(this, this.modelsActive);
        activeSRecyclerView.setAdapter(activeSessionAdapter);

        failedSessionAdapter = new FailedSessionAdapter(this.modelsFailed);
        failedSRecyclerView.setAdapter(failedSessionAdapter);

        activeButton = (Button) rootView.findViewById(R.id.activeSessionButton);
        activeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeButton.setTextColor(Color.WHITE); //Selected
                activeButton.setText(activeButton.getText().toString().replace("  ▼", "")); //Remove if exist
                activeButton.setText(activeButton.getText() + "  ▼");
                failedButton.setTextColor(Color.GRAY);
                failedButton.setText(failedButton.getText().toString().replace("  ▼", ""));

                failedButton.setShadowLayer(1, 0, 1, Color.DKGRAY);

                activeSRecyclerView.setVisibility(View.VISIBLE);
                failedSRecyclerView.setVisibility(View.GONE);

            }
        });

        failedButton = (Button) rootView.findViewById(R.id.failedSessionButton);
        failedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeButton.setTextColor(Color.GRAY);
                failedButton.setText(failedButton.getText().toString().replace("  ▼", ""));  //Remove if exist
                failedButton.setText(failedButton.getText() + "  ▼");
                activeButton.setText(activeButton.getText().toString().replace("  ▼", ""));
                activeButton.setShadowLayer(1, 0, 1, Color.DKGRAY);
                failedButton.setTextColor(Color.WHITE); //Selected

                activeSRecyclerView.setVisibility(View.GONE);
                failedSRecyclerView.setVisibility(View.VISIBLE);
            }
        });

        //Default font theme for buttons to show selected one
        activeButton.setText(activeButton.getText() + "  ▼");
        activeButton.setTextColor(Color.WHITE);
        failedButton.setTextColor(Color.GRAY);
        failedButton.setShadowLayer(1, 0, 1, Color.DKGRAY);

        filterDialogFragment = new FilterDialogFragment();
        filterDialogFragment.setTargetFragment(this, 0);

        filterActiveButton = (ImageButton) rootView.findViewById(R.id.filterActiveSessionButton);
        filterActiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialogFragment.show(getActivity().getSupportFragmentManager(), "Filter");
            }
        });

        return rootView;
    }

    @Override
    public void onFilterAdded(String sourceIp, String remoteUser, String server) {
        ((MainActivity) getActivity()).getFilteredActiveSessionList(sourceIp, remoteUser, server);
    }


}
