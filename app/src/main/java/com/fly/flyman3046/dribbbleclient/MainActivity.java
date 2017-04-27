package com.fly.flyman3046.dribbbleclient;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.fly.flyman3046.dribbbleclient.model.AccessTokenResponse;
import com.fly.flyman3046.dribbbleclient.model.ApiClient;
import com.fly.flyman3046.dribbbleclient.model.Shot;
import com.fly.flyman3046.dribbbleclient.model.User;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MainActivityRecylerViewAdapter rcAdapter;
    private List<Shot> mShotsList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView mAvatarImageView;
    private TextView mUserName;

    private ApiClient.ApiStores mApiStores = ApiClient.retrofit().create(ApiClient.ApiStores.class);
    private ApiClient.ApiStores mOauthApiStores = ApiClient.oauthRetrofit().create(ApiClient.ApiStores.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, DribbbleLoginActivity.class);
                    startActivityForResult(intent, 1);
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        if (drawer != null) {
            drawer.setDrawerListener(toggle);
        }
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
            View headerView = navigationView.getHeaderView(0);
            mAvatarImageView = (ImageView) headerView.findViewById(R.id.avatar_imageView);
            mUserName = (TextView) headerView.findViewById(R.id.user_name);
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    //Your refresh code here
                    loadShots();
                }
            });
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.shot_recycler_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mShotsList = new ArrayList<>();
        rcAdapter = new MainActivityRecylerViewAdapter(MainActivity.this, mShotsList);
        mRecyclerView.setAdapter(rcAdapter);
        loadShots();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                final String accessCode = data.getStringExtra("result");

                Log.wtf(TAG, "receveid accesscode in MainActivity: " + accessCode);
                Observable<AccessTokenResponse> observable = mOauthApiStores.getAccessToken(ApiClient.CLIENT_ID, ApiClient.CLIENT_SECRET, accessCode);
                observable.flatMap(new Func1<AccessTokenResponse, Observable<User>>() {
                            @Override
                            public Observable<User> call(AccessTokenResponse authResponse) {
                                return mApiStores.getAuthUser(authResponse.getAccessToken());
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<User>() {
                            @Override
                            public void onCompleted() {
                                Log.wtf(TAG, "onCompleted");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.wtf(TAG, e.getMessage());
                            }

                            @Override
                            public void onNext(User user) {
                                Log.wtf(TAG, "loading user information");
                                Picasso.with(MainActivity.this)
                                        .load(user.getAvatarUrl())
                                        .into(mAvatarImageView);

                                mUserName.setText(user.getName());
                            }
                        });
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    private void loadShots() {
        Log.wtf(TAG, "Start to get json");
        Observable<List<Shot>> observable = mApiStores.getShots(ApiClient.APIKEY);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<List<Shot>>() {
                    @Override
                    public void onCompleted() {
                        Log.wtf(TAG, "onCompleted");
                        mSwipeRefreshLayout.setRefreshing(false);
                        rcAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.wtf(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(List<Shot> result) {
                        Log.wtf(TAG, "search result size is: " + result.size());

                        mShotsList.clear();
                        mShotsList.addAll(result);
                    }
                });
    }
}
