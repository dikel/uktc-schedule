package com.dikelito.schedule;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Grades extends Activity {

    WebView wv;

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
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
