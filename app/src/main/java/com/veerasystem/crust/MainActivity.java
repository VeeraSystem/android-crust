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

package com.veerasystem.crust;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import rx.Observable;

import com.google.gson.Gson;
import com.veerasystem.crust.Fragment.ConnectionFragment;
//import com.veerasystem.crust.Fragment.ServerCountGroupFragment;
import com.veerasystem.crust.Fragment.SessionFragment;
import com.veerasystem.crust.Model.ActiveConnectionModel;
import com.veerasystem.crust.Model.ActiveConnectionModel.Result;
import com.veerasystem.crust.Model.ActiveSessionModel;
import com.veerasystem.crust.Model.FailedConnectionModel;
import com.veerasystem.crust.Model.FailedSessionModel;
import com.veerasystem.crust.Model.RemoteUsersModel;
import com.veerasystem.crust.Model.ServerAccountModel;
import com.veerasystem.crust.Model.ServerCountChartModel;
import com.veerasystem.crust.Model.ServerCountModel;
import com.veerasystem.crust.Model.ServerGroupCountModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Retrofit retrofit;
    private Gson gson;
    private CrustServiceAPI crustService;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public String tokenID;
    public String serverAddress;

    private ConnectionFragment connectionFragment;
    private SessionFragment sessionFragment;

    private Button serverGroupsCountButton;
    private Button serversCountButton;
    private Button serverAccountsCountButton;
    private Button remoteUsersCountButton;

    private TextView headerUserInfoTextView;

    private int[] tabIcons = {
            R.drawable.connectionicon,
            R.drawable.sessionicon,
            R.drawable.groupicon
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface labelFont = Typeface.createFromAsset(getAssets(),"fonts/DESIB.TTF");
        serverGroupsCountButton = (Button) findViewById(R.id.serverGroupsCountButton);
        serversCountButton = (Button) findViewById(R.id.serversCountButton);
        serverAccountsCountButton = (Button) findViewById(R.id.serverAccountsCountButton);
        remoteUsersCountButton = (Button) findViewById(R.id.remoteUsersCountButton);
        try {
            serverGroupsCountButton.setTypeface(labelFont);
            serversCountButton.setTypeface(labelFont);
            serverAccountsCountButton.setTypeface(labelFont);
            remoteUsersCountButton.setTypeface(labelFont);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
//        if (bundle != null) {
//            tokenID = "Token " + bundle.get("TOKEN").toString();
//            serverAddress = bundle.get("SERVERADDRESS").toString();
//        }

        //Load From Database
        SharedPreferences pref = getApplicationContext().getSharedPreferences("CRUST", 0);
        String username = pref.getString("Username", "");
        tokenID = pref.getString("TOKEN", null);
        serverAddress = pref.getString("SERVERADDRESS", null);

        connectionFragment = new ConnectionFragment();
        sessionFragment = new SessionFragment();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        initConnectionRx();
        getActiveConnectionList();
        reloadAllList();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View nvView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        headerUserInfoTextView = (TextView) nvView.findViewById(R.id.headerUserInfoTextView);
        //Loading Username from Database
        headerUserInfoTextView.setText(username);

        navigationView.setNavigationItemSelectedListener(this);

        TextView signout = (TextView) findViewById(R.id.signout_menu);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();

                SharedPreferences pref = getApplicationContext().getSharedPreferences("CRUST", 0);
                final SharedPreferences.Editor editor = pref.edit();
                editor.remove("TOKEN");
                editor.commit();



                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);


            }
        });
    }

    //Change The Backgournd Color of Tabs
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
        ///Commenting Server Group Chart Tab for now
//        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    public void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(connectionFragment, "Connections");
        viewPagerAdapter.addFragment(sessionFragment, "Sessions");
        ///Commenting Server Group Chart Tab for now
//        viewPagerAdapter.addFragment(new ServerCountGroupFragment(), "Server Count");
        viewPager.setAdapter(viewPagerAdapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        List<Fragment> mFragmentList = new ArrayList<>();
        List<String> mFragmentTitles = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
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

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent objEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return super.onKeyUp(keyCode, objEvent);
    }

    Boolean doubleExitPressed = false;

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
            }
            else {
                Toast.makeText(this,"Press back one more time to exit", Toast.LENGTH_LONG).show();
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
           reloadAllList();
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
            startActivity(this.getIntent());
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void initConnectionRx() {
        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("http://" + serverAddress + "/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        gson = new Gson();

        crustService = retrofit.create(CrustServiceAPI.class);
    }

    public void reloadAllList() {
        getActiveConnectionList();
        getActiveSessionList();
        getFailedConnectionList();
        getFailedSessionList();
        getRemoteUserCount();
        getServerAccountCount();
        getServerCount();
        getServerGroupCount();
        loadServerGroupChartData();
    }

     public void getFilteredActiveSessionList(String sourceIp, String remoteUser, String server) {
         Log.d("Crust",remoteUser);

         HashMap<String, String> parameters = new HashMap<String, String>();

         if (!sourceIp.isEmpty())
             parameters.put("client_ip",sourceIp);
         if (!remoteUser.isEmpty())
             parameters.put("remoteuser", remoteUser);
         if (!server.isEmpty())
             parameters.put("server", server);

         Observable<ResponseBody> activeSessionList = crustService.filterActiveSessions(tokenID, 1, parameters);
         activeSessionList.subscribeOn(Schedulers.newThread())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(new Subscriber<ResponseBody>() {
                     @Override
                     public void onCompleted() {

                     }

                     @Override
                     public void onError(Throwable e) {

                     }

                     @Override
                     public void onNext(ResponseBody responseBody) {
                         try {
                             ActiveSessionModel activeSessionModel = gson.fromJson(responseBody.string(), ActiveSessionModel.class);
                             List<ActiveSessionModel.Result> itemsData;
                             itemsData = activeSessionModel.getsResult();

                             sessionFragment.updateListActive(itemsData);
                         } catch (IOException e) {
                             e.printStackTrace();
                         }
                     }
                 });
    }

    public void getActiveConnectionList() {
        Observable<ResponseBody> activeConnectionList = crustService.activeConnections(tokenID, 1, 1, 10);
        activeConnectionList.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            ActiveConnectionModel activeConnectionModel = gson.fromJson(responseBody.string(), ActiveConnectionModel.class);
                            List<Result> itemsData;
                            itemsData = activeConnectionModel.getsResult();

                            connectionFragment.updateList(itemsData);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void getFailedConnectionList() {
        Observable<ResponseBody> failedConnectionList = crustService.failedConnections(tokenID);
        failedConnectionList.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            FailedConnectionModel failedConnectionModel = gson.fromJson(responseBody.string(), FailedConnectionModel.class);
                            List<FailedConnectionModel.UsersFailCount> itemsData;
                            itemsData = failedConnectionModel.getUsersFailCounts();

                            connectionFragment.updateListFailed(itemsData);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void getActiveSessionList() {
        Observable<ResponseBody> activeSessionList = crustService.activeSessions(tokenID, 1, 1, 10);
        activeSessionList.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            ActiveSessionModel activeSessionModel = gson.fromJson(responseBody.string(), ActiveSessionModel.class);
                            List<ActiveSessionModel.Result> itemsData;
                            itemsData = activeSessionModel.getsResult();

                            sessionFragment.updateListActive(itemsData);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void getFailedSessionList() {
        Observable<ResponseBody> failedSessionList = crustService.failedSessions(tokenID);
        failedSessionList.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            FailedSessionModel failedSessionModel = gson.fromJson(responseBody.string(), FailedSessionModel.class);
                            List<FailedSessionModel.SessionFailCount> itemsData;
                            itemsData = failedSessionModel.getSessionFailCounts();

                            sessionFragment.updateListFailed(itemsData);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void getServerGroupCount() {

        Observable<ResponseBody> serverGroupCount = crustService.getServerGroupCount(tokenID);
        serverGroupCount.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            ServerGroupCountModel serverGroupCountModel = gson.fromJson(responseBody.string(), ServerGroupCountModel.class);

                            int count;
                            count = serverGroupCountModel.getCount();
                            Log.d("ServerGroupCount", String.valueOf(count));
                            serverGroupsCountButton.setText(String.valueOf(count));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void getServerAccountCount() {
        Observable<ResponseBody> serverAccountCount = crustService.getServerAccountCount(tokenID);
        serverAccountCount.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            ServerAccountModel serverAccountModel = gson.fromJson(responseBody.string(), ServerAccountModel.class);

                            int count;
                            count = serverAccountModel.getCount();
                            serverAccountsCountButton.setText(String.valueOf(count));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void getRemoteUserCount() {
        Observable<ResponseBody> remoteUserCount = crustService.getRemoteUserCount(tokenID);
        remoteUserCount.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            RemoteUsersModel remoteUsersModel = gson.fromJson(responseBody.string(), RemoteUsersModel.class);

                            int count;
                            count = remoteUsersModel.getCount();
                            remoteUsersCountButton.setText(String.valueOf(count));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void getServerCount() {
        Observable<ResponseBody> serverCount = crustService.getServerCount(tokenID);
        serverCount.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            ServerCountModel serverCountModel = gson.fromJson(responseBody.string(), ServerCountModel.class);

                            int count;
                            count = serverCountModel.getCount();
                            serversCountButton.setText(String.valueOf(count));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void killSession(int sessionID) {
        Observable<ResponseBody> killSession = crustService.killSession(tokenID, sessionID);
        killSession.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        //TODO: just after true response we should update list
                        //Reloading Active session list after getting response
                        getActiveSessionList();
                        Log.d("Kill State", responseBody.toString());

                    }
                });
    }

    public void sendMessage(String message, int sessionID) {
        Observable<ResponseBody> sendMessageSession = crustService.sendSessionMessage(tokenID, message, sessionID);
        sendMessageSession.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        Log.d("Message State", responseBody.toString());

                    }
                });
    }

     public void loadServerGroupChartData() {
        Observable<ResponseBody> serverGroupChartData = crustService.loadServerGroupChartData(tokenID);
        serverGroupChartData.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            ServerCountChartModel serverCountChartModel = gson.fromJson(responseBody.string(),ServerCountChartModel.class);
                            Log.d("Chart Data",serverCountChartModel.getServerCounts().get(0).get(0).toString());
                            Log.d("Chart Data",serverCountChartModel.getServerCounts().get(0).get(1).toString());
                            Log.d("Chart Data",serverCountChartModel.getServerCounts().get(0).toString());
                            Log.d("Chart Data",serverCountChartModel.getServerCounts().toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void logoutUser() {
        Log.d("Crsut","Calling Logout");
        Observable<ResponseBody> logoutResponce = crustService.logout(tokenID);
        logoutResponce.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {

                        Log.d("Logout State", responseBody.toString());

                    }
                });
    }
}
