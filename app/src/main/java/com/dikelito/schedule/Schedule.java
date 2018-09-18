package com.dikelito.schedule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import java.io.File;
import java.util.Calendar;

public class Schedule extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    MenuItem item, itemTime;
    ImageView iv, ivTime;
    HorizontalScrollView scrollView;

    String name, type, defaultSchedule, teacherName;
    boolean showTime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int theme = sharedPreferences.getInt("Theme", R.style.AppTheme);
        if (theme == R.style.AppThemeDark) {
            setTheme(R.style.AppThemeDark_DarkActionBar);
        } else {
            setTheme(R.style.AppTheme_DarkActionBar);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);

        iv = (ImageView) findViewById(R.id.iv);
        ivTime = (ImageView) findViewById(R.id.time);

        Bundle bundle = getIntent().getExtras();
        type = bundle.getString("type");
        name = bundle.getString("name");

        String title = null;
        switch (type) {
            case "students":
                title = "Курс " + name.replace("s", "");
                break;
            case "teachers":
                teacherName = bundle.getString("teacherName");
                title = "Учител " + teacherName;
                break;
            case "rooms":
                if (name.startsWith("r")) {
                    title = "Стая " + name.replace("r", "");
                } else {
                    title = "Зала за асемблиране";
                }
                break;
        }
        getSupportActionBar().setTitle(title);

        File file = new File(this.getExternalFilesDir(null) + "/" + type, name + ".png");
        Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        Bitmap timeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.time);
        if (theme == R.style.AppThemeDark) {
            myBitmap = changeBitmapContrastBrightness(myBitmap, 1, -127);
            timeBitmap = changeBitmapContrastBrightness(timeBitmap, 1, -127);
        }
        iv.setImageBitmap(myBitmap);
        ivTime.setImageBitmap(timeBitmap);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        defaultSchedule = sharedPreferences.getString("defaultSchedule", null);
        showTime = sharedPreferences.getBoolean("showTime", false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        scrollView = (HorizontalScrollView) findViewById(R.id.sv);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                int scheduleSize = iv.getWidth();
                int timeSize = scheduleSize / 10;
                int offset = (day == 1 || day == 7) ? 0 : day - 2;
                scrollView.scrollTo(offset * ((scheduleSize - timeSize) / 5), 0);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.schedule_activity_actions, menu);
        item = menu.findItem(R.id.main);
        if (name.equals(defaultSchedule)) {
            item.setTitle("Не стартирай първоначално");
        } else {
            item.setTitle("Стартирай първоначално");
        }
        itemTime = menu.findItem(R.id.time);
        if (showTime) {
            ivTime.setVisibility(View.VISIBLE);
            itemTime.setTitle("Скрий лентата с часове");
        } else {
            ivTime.setVisibility(View.GONE);
            itemTime.setTitle("Покажи лентата с часове");
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.main:
                if (name.equals(defaultSchedule)) {
                    defaultSchedule = null;
                    item.setTitle("Стартирай първоначално");
                } else {
                    defaultSchedule = name;
                    item.setTitle("Не стартирай първоначално");
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("defaultSchedule", defaultSchedule);
                editor.putString("type", type);
                if (type.equals("teachers")) {
                    editor.putString("teacherName", teacherName);
                }
                editor.apply();
                return true;
            case R.id.time:
                if (showTime) {
                    ivTime.setVisibility(View.GONE);
                    item.setTitle("Покажи лентата с часове");
                } else {
                    ivTime.setVisibility(View.VISIBLE);
                    item.setTitle("Скрий лентата с часове");
                }
                showTime = !showTime;
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                editor = sharedPreferences.edit();
                editor.putBoolean("showTime", showTime);
                editor.apply();
                return true;
            case R.id.food:
                startActivity(new Intent(Schedule.this, Food.class));
                return true;
            case R.id.grades:
                startActivity(new Intent(Schedule.this, Grades.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Bitmap changeBitmapContrastBrightness(Bitmap bmp, float contrast, float brightness) {
        ColorMatrix cm = new ColorMatrix(new float[]
                {
                        contrast, 0, 0, 0, brightness,
                        0, contrast, 0, 0, brightness,
                        0, 0, contrast, 0, brightness,
                        0, 0, 0, 1, 0
                });

        Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        Canvas canvas = new Canvas(ret);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bmp, 0, 0, paint);

        return ret;
    }
}
