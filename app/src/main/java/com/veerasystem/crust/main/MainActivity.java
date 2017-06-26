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

package com.veerasystem.crust.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.veerasystem.crust.CrustApplication;
import com.veerasystem.crust.R;
import com.veerasystem.crust.dashboard.DashboardFragment;
import com.veerasystem.crust.dashboard.DashboardPresenter;
import com.veerasystem.crust.dashboard.DashboardPresenterModule;
import com.veerasystem.crust.data.source.remote.Remote;
import com.veerasystem.crust.utils.ActivityUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements CrustContractor.View, NavigationView.OnNavigationItemSelectedListener {

    @Inject
    Remote remote;

    @Inject
    Presenter presenter;

    @Inject
    DashboardPresenter dashboardPresenter;

    private TextView headerUserInfoTextView;

    @BindView(R.id.signout_menu)
    TextView signout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    private DashboardFragment dashboardFragment;
    private FragmentManager fragmentManager;

    private Boolean doubleExitPressed = false;

    @Override
    public void setPresenter(CrustContractor.Presenter presenter) {
        //FIXME remove
        /*this.presenter = presenter;*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        fragmentManager = getSupportFragmentManager();

        dashboardFragment = (DashboardFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (dashboardFragment == null) {
            dashboardFragment = DashboardFragment.newInstance();
            ActivityUtils.addFragmentToActivity(fragmentManager, dashboardFragment, R.id.contentFrame);
        }

        DaggerMainComponent.builder()
                .remoteComponent(((CrustApplication)getApplication()).getComponent())
                .mainPresenterModule(new MainPresenterModule(this))
                .dashboardPresenterModule(new DashboardPresenterModule(dashboardFragment)).build()
                .inject(this);

        //Load From Database
        SharedPreferences pref = getSharedPreferences("CRUST", 0);
        String username = pref.getString("Username", "");
        String serverAddress = pref.getString("SERVERADDRESS", null);

        if (serverAddress != null)
            remote.setup(serverAddress);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        View nvView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        headerUserInfoTextView = (TextView) nvView.findViewById(R.id.headerUserInfoTextView);

        //Loading Username from DB
        headerUserInfoTextView.setText(username);

        navigationView.setNavigationItemSelectedListener(this);

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.signOut();
            }
        });
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent objEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return super.onKeyUp(keyCode, objEvent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleExitPressed) {
                Intent exitIntent = new Intent(Intent.ACTION_MAIN);
                exitIntent.addCategory(Intent.CATEGORY_HOME);
                exitIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(exitIntent);
            } else {
                Toast.makeText(this, "Press back one more time to exit", Toast.LENGTH_LONG).show();
                doubleExitPressed = true;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleExitPressed = false;
                    }
                }, 2000);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            //FIXME
//            reloadAllList();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            ActivityUtils.replaceFragment(fragmentManager, dashboardFragment, R.id.contentFrame);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void showLoginPage() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("CRUST", 0);
        final SharedPreferences.Editor editor = pref.edit();
        editor.remove("TOKEN");
        editor.apply();

        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    public String getToken() {
        SharedPreferences pref = getSharedPreferences("CRUST", 1);
        return pref.getString("TOKEN", "");
    }

}
