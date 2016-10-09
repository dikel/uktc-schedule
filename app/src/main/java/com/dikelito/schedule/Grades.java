package com.dikelito.schedule;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewDatabase;

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

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.grades_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.clearData:
                WebViewDatabase.getInstance(this).clearFormData();
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
