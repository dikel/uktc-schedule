package com.dikelito.schedule;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewDatabase;

public class Grades extends AppCompatActivity {

    WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int theme = sharedPreferences.getInt("Theme", R.style.AppTheme);
        if(theme == R.style.AppThemeDark) {
            setTheme(R.style.AppThemeDark_DarkActionBar);
        } else {
            setTheme(R.style.AppTheme_DarkActionBar);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grades);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Електронен дневник");
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
}
