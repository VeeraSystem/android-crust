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

package com.veerasystem.crust.dashboard;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.veerasystem.crust.R;
import com.veerasystem.crust.dashboard.connectionFragment.ConnectionFragment;
import com.veerasystem.crust.dashboard.connectionFragment.ConnectionPresenter;
import com.veerasystem.crust.dashboard.sessionFragment.SessionFragment;
import com.veerasystem.crust.dashboard.sessionFragment.SessionPresenter;
import com.veerasystem.crust.data.source.remote.Remote;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment implements DashboardContract.View {

    private static final String TAG = "DashboardFragment";

    private DashboardContract.Presenter presenter;

    private TextView lblServerGroupsCount;
    private TextView lblServersCount;
    private TextView lblServerAccountsCount;
    private TextView lblRemoteUsersCount;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private String tokenID;
    private String serverAddress;

    private ConnectionFragment connectionFragment;
    private SessionFragment sessionFragment;


    private TextView headerUserInfoTextView;

    private int[] tabIcons = {
            R.drawable.connectionicon,
            R.drawable.sessionicon,
            R.drawable.groupicon
    };

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    public void setPresenter(DashboardContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lblServerGroupsCount = (TextView) view.findViewById(R.id.serverGroupsCountButton);
        lblServersCount = (TextView) view.findViewById(R.id.serversCountButton);
        lblServerAccountsCount = (TextView) view.findViewById(R.id.serverAccountsCountButton);
        lblRemoteUsersCount = (TextView) view.findViewById(R.id.remoteUsersCountButton);

        Typeface labelFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/DESIB.TTF");
        try {
            lblServerGroupsCount.setTypeface(labelFont);
            lblServersCount.setTypeface(labelFont);
            lblServerAccountsCount.setTypeface(labelFont);
            lblRemoteUsersCount.setTypeface(labelFont);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Load From DB
        SharedPreferences pref = getActivity().getSharedPreferences("CRUST", 0);
        tokenID = pref.getString("TOKEN", null);
        serverAddress = pref.getString("SERVERADDRESS", null);

        if (connectionFragment == null)
            connectionFragment = ConnectionFragment.newInstance();

        Remote remote = Remote.getINSTANCE();
        if (serverAddress != null)
            remote.setup(serverAddress);

        new ConnectionPresenter(remote, connectionFragment);

        if (sessionFragment == null)
            sessionFragment = SessionFragment.newInstance();

        new SessionPresenter(remote, sessionFragment);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    public void setTabColor(TabHost tabhost) {
        for (int i = 0; i < tabhost.getTabWidget().getChildCount(); i++)
            tabhost.getTabWidget().getChildAt(i).setBackgroundColor(1); //unselected

        if (tabhost.getCurrentTab() == 0)
            tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundColor(2); //1st tab selected
        else
            tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundColor(3); //2nd tab selected
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(connectionFragment, "Connections");
        viewPagerAdapter.addFragment(sessionFragment, "Sessions");
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public String getToken() {
        SharedPreferences pref = getActivity().getSharedPreferences("CRUST", 1);
        return pref.getString("TOKEN", "");
    }

    @Override
    public void showServerGroupsCount(String count) {
        lblServerGroupsCount.setText(count);
    }

    @Override
    public void showServerCount(String count) {
        lblServersCount.setText(count);
    }

    @Override
    public void showServerAccountsCount(String count) {
        lblServerAccountsCount.setText(count);
    }

    @Override
    public void showRemoteUsersCount(String count) {
        lblRemoteUsersCount.setText(count);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        List<Fragment> mFragmentList = new ArrayList<>();
        List<String> mFragmentTitles = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

}
