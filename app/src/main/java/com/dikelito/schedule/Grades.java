package com.dikelito.schedule;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Grades extends Activity {

    WebView wv;
    AdView mAdView;
    AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grades);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Електронен дневник");
        wv = (WebView) findViewById(R.id.wvGrades);
        wv.loadUrl("http://my.uktc-bg.com/");
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new wvClient());
        mAdView = (AdView) findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdView.resume();
    }

    @Override
    public void onPause() {
        mAdView.pause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mAdView.destroy();
        super.onDestroy();
    }
}
