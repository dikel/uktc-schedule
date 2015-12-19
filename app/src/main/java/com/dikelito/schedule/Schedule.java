package com.dikelito.schedule;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Schedule extends Activity {

    public static String cls;
    public static String scls;
    SharedPreferences sharedPreferences;
    MenuItem item;
    ImageView iv;
    String sdl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Курс " + cls);
        iv = (ImageView) findViewById(R.id.iv);
        sdl = "s" + cls;
        String uri = "drawable/" + sdl;
        int imageResource = this.getResources().getIdentifier(uri, null, this.getPackageName());
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
                editor.apply();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
