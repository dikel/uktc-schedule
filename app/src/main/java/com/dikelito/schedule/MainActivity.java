package com.dikelito.schedule;

import android.Manifest;
import android.app.SearchManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private SearchView mSearchView;
    final String emailAddress[] = {"dikelito@tutamail.com"};
    SharedPreferences sharedPreferences;
    String version = "0000-00-00";
    int theme;
    public static JSONObject jsonObject = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        theme = sharedPreferences.getInt("Theme", R.style.AppTheme);
        setTheme(theme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String jsonString = sharedPreferences.getString("json", null);
        if (jsonString != null) {
            try {
                jsonObject = new JSONObject(sharedPreferences.getString("json", null));
                version = jsonObject.getString("version");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        File dir = new File(this.getExternalFilesDir(null), "students");
        if (!dir.exists() || dir.list().length == 0) {
            version = "0000-00-00";
        }

        new checkForUpdate(this).execute(getResources().getString(R.string.serverIP), version);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        String defaultSchedule = sharedPreferences.getString("defaultSchedule", null);

        if (defaultSchedule != null) {
            String defaultType = sharedPreferences.getString("type", null);
            Intent intent = new Intent(this, Schedule.class);
            intent.putExtra("name", defaultSchedule);
            intent.putExtra("type", defaultType);
            if(defaultType.equals("teachers")) {
                String teacherName =  sharedPreferences.getString("teacherName", null);
                intent.putExtra("teacherName", teacherName);
            }
            startActivity(intent);
        }
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            int hasReadPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasReadPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
            }
        }
    }

    private void setupSearchView(MenuItem searchItem) {
        if (isAlwaysExpanded()) {
            mSearchView.setIconifiedByDefault(false);
        } else {
            searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM
                    | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        }
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint("Търси Учител");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_actions, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchItem.getActionView();
        setupSearchView(searchItem);
        MenuItem item = menu.findItem(R.id.theme);
        if (theme == R.style.AppThemeDark){
            item.setTitle("Светла тема");
        }else{
            item.setTitle("Тъмна тема");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_search:
                mViewPager.setCurrentItem(1, true);
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
                startActivity(new Intent(MainActivity.this, Food.class));
                return true;
            case R.id.about:
                startActivity(new Intent(MainActivity.this, About.class));
                return true;
            case R.id.grades:
                startActivity(new Intent(MainActivity.this, Grades.class));
                return true;
            case R.id.theme:
                if (theme == R.style.AppThemeDark) {
                    theme = R.style.AppTheme;
                    item.setTitle("Тъмна тема");
                } else {
                    theme = R.style.AppThemeDark;
                    item.setTitle("Светла тема");
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("Theme", theme);
                editor.apply();
                setTheme(theme);
                recreate();
//                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.donate:
                AlertDialog.Builder builder;
                View dialog = LayoutInflater.from(this).inflate(R.layout.donate_dialog, null);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_NoActionBar);
                } else {
                    builder = new AlertDialog.Builder(this);
                }
                builder.setView(dialog).show();
                ImageView qrCode = dialog.findViewById(R.id.qrCodeImg);
                qrCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("qrCode", "DA1wN7UZHEiqxGXYrY6DZw76rMqyr9WJM4");
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(getApplicationContext(), "Адресът е копиран!", Toast.LENGTH_SHORT).show();
                    }
                });
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Teachers.adapter.getFilter().filter(newText);
        return false;
    }

    protected boolean isAlwaysExpanded() {
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return Students.newInstance(0, "Students");
                case 1:
                    return Teachers.newInstance(1, "Teachers");
                case 2:
                    return Rooms.newInstance(2, "Rooms");
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Курсове";
                case 1:
                    return "Учители";
                case 2:
                    return "Стаи";
            }
            return null;
        }
    }
}