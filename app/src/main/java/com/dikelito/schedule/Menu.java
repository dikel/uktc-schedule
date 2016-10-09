package com.dikelito.schedule;

import android.Manifest;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Menu extends ListActivity {

    String classes[] = {"161", "162", "163", "164", "165", "151", "152", "153", "154", "155", "141",
            "142", "143", "144", "131", "132", "133", "134", "121", "122", "123"};
    String ncls;
    String email = "dikelito@tutamail.com";
    String emailAddress[] = {email};
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<>(this, R.layout.list, classes));
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Schedule.scls = sharedPreferences.getString("scls", null);
        if (Schedule.scls != null) {
            Schedule.cls = Schedule.scls;
            startActivity(new Intent(Menu.this, Schedule.class));
        }
        if (android.os.Build.VERSION.SDK_INT >= 23){
            int hasReadPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasReadPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        10);
            }
        }

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ncls = classes[position];
        Schedule.cls = ncls;
        startActivity(new Intent(Menu.this, Schedule.class));
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.rate:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.dikelito.schedule")));
                return true;
            case R.id.feedback:
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, emailAddress);
                startActivity(emailIntent);
                return true;
            case R.id.food:
                startActivity(new Intent(Menu.this, Food.class));
                return true;
            case R.id.about:
                startActivity(new Intent(Menu.this, About.class));
                return true;
            case R.id.grades:
                startActivity(new Intent(Menu.this, Grades.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
