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

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.veerasystem.crust.R;
import com.veerasystem.crust.data.ActiveConnectionModel.Result;
import com.veerasystem.crust.data.FailedConnectionModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConnectionFragment extends Fragment implements ConnectionContract.View {

    private ActiveAdapter activeAdapter;
    private FailedAdapter failedAdapter;

    @BindView(R.id.connectionActiveRecyclerView)
    RecyclerView activeCRecyclerView;
    @BindView(R.id.connectionFailedRecyclerView)
    RecyclerView failedCRecyclerView;

    @BindView(R.id.swipeRefreshConnectionActiveLayout)
    SwipeRefreshLayout activeCSwipeRefresh;
    @BindView(R.id.swipeRefreshConnectionFailedLayout)
    SwipeRefreshLayout failedCSwipeRefresh;

    @BindView(R.id.activeButton)
    Button activeButton;
    @BindView(R.id.failedButton)
    Button failedButton;

    private ConnectionContract.Presenter presenter;

    public static ConnectionFragment newInstance() {
        return new ConnectionFragment();
    }

    @Override
    public void setPresenter(ConnectionContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_connetion, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        activeCSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadActiveList();
                activeCSwipeRefresh.setRefreshing(false);
            }
        });

        failedCSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadFailedList();
                failedCSwipeRefresh.setRefreshing(false);
            }
        });

        activeCRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        failedCRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        activeAdapter = new ActiveAdapter();
        activeCRecyclerView.setAdapter(activeAdapter);

        failedAdapter = new FailedAdapter();
        failedCRecyclerView.setAdapter(failedAdapter);

        //Default
        gotoActiveTab();

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public String getToken() {
        SharedPreferences pref = getActivity().getSharedPreferences("CRUST", 1);
        return pref.getString("TOKEN", "");
    }

    @Override
    public void showActiveList(List<Result> models) {
        activeAdapter.updateModel(models);

        //Updating Title to show count
        activeButton.setText("Active (" + models.size() + ")");
        activeButton.setText(activeButton.getText().toString().replace("  ▼", "")); //Remove if exist
        activeButton.setText(activeButton.getText() + "  ▼");

        //Notifying View to Reload
        activeAdapter.notifyDataSetChanged();
    }

    @Override
    public void showFailedList(List<FailedConnectionModel.UsersFailCount> models) {
        failedAdapter.updateModel(models);

        //Updating Title to show count
        failedButton.setText("Failed (" + models.size() + ")");

        //Notifying View to Reload
        failedAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.activeButton)
    public void gotoActiveTab() {
        activeButton.setTextColor(Color.WHITE); //Selected
        activeButton.setText(activeButton.getText().toString().replace("  ▼", "")); //Remove if exist
        activeButton.setText(activeButton.getText() + "  ▼");
        failedButton.setTextColor(Color.GRAY);
        failedButton.setText(failedButton.getText().toString().replace("  ▼", ""));

        failedButton.setShadowLayer(1, 0, 1, Color.DKGRAY);

        activeCSwipeRefresh.setVisibility(View.VISIBLE);
        failedCSwipeRefresh.setVisibility(View.GONE);
    }

    @OnClick(R.id.failedButton)
    public void gotoFailedTab() {
        activeButton.setTextColor(Color.GRAY);
        failedButton.setText(failedButton.getText().toString().replace("  ▼", ""));  //Remove if exist
        failedButton.setText(failedButton.getText() + "  ▼");
        activeButton.setText(activeButton.getText().toString().replace("  ▼", ""));
        activeButton.setShadowLayer(1, 0, 1, Color.DKGRAY);
        failedButton.setTextColor(Color.WHITE); //Selected

        activeCSwipeRefresh.setVisibility(View.GONE);
        failedCSwipeRefresh.setVisibility(View.VISIBLE);
    }

}