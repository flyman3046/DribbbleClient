package com.fly.flyman3046.dribbbleclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DribbbleLoginActivity extends AppCompatActivity {
    private final static String TAG = DribbbleLoginActivity.class.getSimpleName();
    private final static String REDIRECT_URL = "https://github.com/flyman3046?code=";
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dribbble_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.wtf(TAG, "load new url: " + url);
                if (url.startsWith(REDIRECT_URL)) {
                    String accessCode = url.replace(REDIRECT_URL, "");

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result", accessCode);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
                else {
                    view.loadUrl(url);
                }
                return true;
            }
        });

        mWebView.loadUrl("https://dribbble.com/oauth/authorize?client_id=786e7354b0acc25365f4639799c241b5f94952f367f120a16edc57e71ed0cc6b");

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
