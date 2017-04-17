package com.fly.flyman3046.dribbbleclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fly.flyman3046.dribbbleclient.model.ApiClient;
import com.fly.flyman3046.dribbbleclient.model.Comment;
import com.fly.flyman3046.dribbbleclient.model.Shot;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 */
public class ShotActivity extends AppCompatActivity {
    private final static String TAG = ShotActivity.class.getSimpleName();
    private CommentRecylerViewAdapter rcAdapter;
    private List<Comment> commentsList;
    private ApiClient.ApiStores mApiStores = ApiClient.retrofit().create(ApiClient.ApiStores.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shot_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ImageView shotImageView = (ImageView) findViewById(R.id.appbar_backdrop);
//        ImageView

        String imageUrl = getIntent().getStringExtra(ShotRecylerViewAdapter.IMAGE_URL);
        Integer shotId = getIntent().getIntExtra(ShotRecylerViewAdapter.SHOT_ID, 0);

        if (imageUrl.endsWith(".gif")) {
            Glide.with(this)
                    .load(imageUrl)
                    .asGif()
                    .placeholder(R.drawable.progress_animation)
                    .into(shotImageView);
        }
        else {
            Glide.with(this)
                    .load(imageUrl)
                    .into(shotImageView);
        }

        RecyclerView commentRecylerView = (RecyclerView) findViewById(R.id.comment_recycler_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        commentRecylerView.setLayoutManager(mLayoutManager);

        commentsList = new ArrayList<>();
        rcAdapter = new CommentRecylerViewAdapter(ShotActivity.this, commentsList);
        commentRecylerView.setAdapter(rcAdapter);
        loadComments(shotId);
    }

    private void loadComments(Integer shotId) {
        Observable<List<Comment>> observable = mApiStores.getCommentsByShotId(shotId, ApiClient.APIKEY);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<List<Comment>>() {
                    @Override
                    public void onCompleted() {
                        Log.wtf(TAG, "onCompleted");
                        rcAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.wtf(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(List<Comment> result) {
                        Log.wtf(TAG, "search result size is: " + result.size());

                        commentsList.clear();
                        commentsList.addAll(result);
                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }








}
