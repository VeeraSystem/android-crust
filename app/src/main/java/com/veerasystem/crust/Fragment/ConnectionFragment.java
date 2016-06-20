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

package com.veerasystem.crust.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.veerasystem.crust.Adapter.ActiveConnectionAdapter;
import com.veerasystem.crust.Adapter.FailedConnectionAdapter;
import com.veerasystem.crust.MainActivity;
import com.veerasystem.crust.Model.ActiveConnectionModel.Result;
import com.veerasystem.crust.Model.FailedConnectionModel;
import com.veerasystem.crust.R;

import java.util.List;

public class ConnectionFragment extends Fragment {

    private ActiveConnectionAdapter activeConnectionAdapter;
    private FailedConnectionAdapter failedConnectionAdapter;

    RecyclerView activeCRecyclerView;
    RecyclerView failedCRecyclerView;

    SwipeRefreshLayout activeCSwipeRefresh;
    SwipeRefreshLayout failedCSwipeRefresh;

    private List<Result> modelsActive;
    private List<FailedConnectionModel.UsersFailCount> modelsFailed;

    private Button activeButton;
    private Button failedButton;

    public ConnectionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void updateList(List<Result> models) {
        this.modelsActive = models;
        reloadActiveAdapter();
    }

    public void updateListFailed(List<FailedConnectionModel.UsersFailCount> models) {
        this.modelsFailed = models;
        reloadFailedAdapter();
    }

    private void reloadActiveAdapter() {
        activeConnectionAdapter.updateModel(this.modelsActive);

        //Updating Title to show count
        activeButton.setText("Active (" + this.modelsActive.size() + ")");
        activeButton.setText(activeButton.getText().toString().replace("  ▼", "")); //Remove if exist
        activeButton.setText(activeButton.getText() + "  ▼");

        //Notifying View to Reload
        activeConnectionAdapter.notifyDataSetChanged();
    }

    private void reloadFailedAdapter() {
        failedConnectionAdapter.updateModel(this.modelsFailed);

        //Updating Title to show count
        failedButton.setText("Failed (" + this.modelsFailed.size() + ")");

        //Notifying View to Reload
        failedConnectionAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_connetion, container, false);

        activeCRecyclerView = (RecyclerView) rootView.findViewById(R.id.connectionActiveRecyclerView);
        failedCRecyclerView = (RecyclerView) rootView.findViewById(R.id.connectionFailedRecyclerView);

        activeCSwipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshConnectionActiveLayout);
        failedCSwipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshConnectionFailedLayout);

        activeCSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((MainActivity) getActivity()).reloadAllList();
                activeCSwipeRefresh.setRefreshing(false);
            }
        });

        failedCSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((MainActivity) getActivity()).reloadAllList();
                failedCSwipeRefresh.setRefreshing(false);
            }
        });

        //Don't show by default
        failedCRecyclerView.setVisibility(View.GONE);

        activeCRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        failedCRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        activeConnectionAdapter = new ActiveConnectionAdapter(this.modelsActive);
        activeCRecyclerView.setAdapter(activeConnectionAdapter);

        failedConnectionAdapter = new FailedConnectionAdapter(this.modelsFailed);
        failedCRecyclerView.setAdapter(failedConnectionAdapter);

        activeButton = (Button) rootView.findViewById(R.id.activeButton);
        activeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeButton.setTextColor(Color.WHITE); //Selected
                activeButton.setText(activeButton.getText().toString().replace("  ▼", "")); //Remove if exist
                activeButton.setText(activeButton.getText() + "  ▼");
                failedButton.setTextColor(Color.GRAY);
                failedButton.setText(failedButton.getText().toString().replace("  ▼", ""));

                failedButton.setShadowLayer(1, 0, 1, Color.DKGRAY);

                activeCRecyclerView.setVisibility(View.VISIBLE);
                failedCRecyclerView.setVisibility(View.GONE);

            }
        });

        failedButton = (Button) rootView.findViewById(R.id.failedButton);
        failedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeButton.setTextColor(Color.GRAY);
                failedButton.setText(failedButton.getText().toString().replace("  ▼", ""));  //Remove if exist
                failedButton.setText(failedButton.getText() + "  ▼");
                activeButton.setText(activeButton.getText().toString().replace("  ▼", ""));
                activeButton.setShadowLayer(1, 0, 1, Color.DKGRAY);
                failedButton.setTextColor(Color.WHITE); //Selected

                activeCRecyclerView.setVisibility(View.GONE);
                failedCRecyclerView.setVisibility(View.VISIBLE);
            }
        });

        //Default font theme for buttons to show selected one
        activeButton.setText(activeButton.getText() + "  ▼");
        activeButton.setTextColor(Color.WHITE);
        failedButton.setTextColor(Color.GRAY);
        failedButton.setShadowLayer(1, 0, 1, Color.DKGRAY);

        return rootView;
    }

}