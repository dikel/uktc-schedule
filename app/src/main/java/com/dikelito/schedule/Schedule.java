package com.dikelito.schedule;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

public class Schedule extends Activity {

    public static String cls;
    public static String scls;
    SharedPreferences sharedPreferences;
    MenuItem item, itemTime;
    ImageView iv, ivTime;
    String sdl;
    boolean showTime = false;
    int imageResource;
    String uri;
    HorizontalScrollView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        showTime = sharedPreferences.getBoolean("showTime", false);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Курс " + cls);
        iv = (ImageView) findViewById(R.id.iv);
        ivTime = (ImageView) findViewById(R.id.time);
        sv = (HorizontalScrollView) findViewById(R.id.sv);
        sdl = "s" + cls;
        uri = "@drawable/" + sdl;
        imageResource = getResources().getIdentifier(uri, null, getPackageName());
        iv.setImageResource(imageResource);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.schedule_activity_actions, menu);
        item = menu.findItem(R.id.main);
        if(scls == null || !scls.equals(cls)){
            item.setTitle("Стартирай първоначално");
        }else if(scls.equals(cls)){
            item.setTitle("Не стартирай първоначално");
        }
        itemTime = menu.findItem(R.id.time);
        if(showTime){
            ivTime.setVisibility(View.VISIBLE);
            itemTime.setTitle("Скрий лентата с часове");
        }else{
            ivTime.setVisibility(View.INVISIBLE);
            itemTime.setTitle("Покажи лентата с часове");
        }
        return super.onCreateOptionsMenu(menu);
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.main:
                if(scls == null){
                    scls = cls;
                    item.setTitle("Не стартирай първоначално");
                }else{
                    scls = null;
                    item.setTitle("Стартирай първоначално");
                }
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.putString("scls", scls);
                editor.putBoolean("showTime", showTime);
                editor.apply();
                return true;
            case R.id.time:
                if(showTime){
                    ivTime.setVisibility(View.INVISIBLE);
                    item.setTitle("Покажи лентата с часове");
                }else{
                    ivTime.setVisibility(View.VISIBLE);
                    item.setTitle("Скрий лентата с часове");
                }
                showTime = !showTime;
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                editor = sharedPreferences.edit();
                editor.clear();
                editor.putString("scls", scls);
                editor.putBoolean("showTime", showTime);
                editor.apply();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
