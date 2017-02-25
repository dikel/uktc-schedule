package com.dikelito.schedule;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

public class Schedule extends AppCompatActivity {

    public static String thing;
    public static String cls;
    public static String scls;
    public static int num;
    SharedPreferences sharedPreferences;
    MenuItem item, itemTime;
    ImageView iv, ivTime;
    boolean showTime = false;
    int imageResource;
    String uri;
    HorizontalScrollView sv;
    final String teachers_filenames[] = {"valeri_kolev", "valeri_stefanov", "veneta_bojinova",
            "rumyana_lazarova", "nina_bayacheva", "emilia_donkova", "dimcho_danov", "ivanka_mancheva",
            "ivanka_nencheva", "ivo_tsuklev", "ina_georgieva", "kiril_tonchev", "margarita_gavrilova",
            "gergana_muhovska", "mariana_manoeva", "nedelcho_nedyalkov", "nikolai_sirakov", "ognyan_nakov",
            "radka_yordanova", "siika_tseneva", "stefan_stefanov", "stefan_tsenov", "trifon_trifonov",
            "tsetsa_tsolova", "maria_petkova", "paulina_tsvetkova", "petya_misheva", "boriana_dimitrova",
            "neli_sirakova", "valia_vladimirova", "tsvetomir_georgiev", "rumen_trifonov", "vladimir_dimitrov",
            "natalia_doncheva", "tatyana_ivanova", "darin_vasilev", "lidya_rashkova", "galya_bojinova",
            "penka_tomova", "pavlin_petkov", "vencislav_nachev", "yavor_tomov", "daniela_gotseva",
            "valentin_hristov", "krasimir_iliev", "petur_marinov", "tencho_gochev", "milena_lazarova",
            "momchil_petkov", "elena_purvanova", "kostadin_kostadinov", "vasil_tsanov", "none", "nikolai_boshkov"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        showTime = sharedPreferences.getBoolean("showTime", false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(thing + " " + cls);
        iv = (ImageView) findViewById(R.id.iv);
        ivTime = (ImageView) findViewById(R.id.time);
        sv = (HorizontalScrollView) findViewById(R.id.sv);
        switch (thing){
            case "Курс":
                uri = "@drawable/s" + cls;
                break;
            case "Стая":
                uri = "@drawable/r" + cls;
                break;
            case "Учител":
                uri = "@drawable/" + teachers_filenames[num];
                break;
        }
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
        }else{
            item.setTitle("Не стартирай първоначално");
        }
        itemTime = menu.findItem(R.id.time);
        if(showTime){
            ivTime.setVisibility(View.VISIBLE);
            itemTime.setTitle("Скрий лентата с часове");
        }else{
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
                if(scls == null || !scls.equals(cls)){
                    scls = cls;
                    item.setTitle("Не стартирай първоначално");
                }else{
                    scls = null;
                    item.setTitle("Стартирай първоначално");
                }
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.putString("thing", thing);
                editor.putString("scls", scls);
                editor.putInt("num", num);
                editor.putBoolean("showTime", showTime);
                editor.apply();
                return true;
            case R.id.time:
                if(showTime){
                    ivTime.setVisibility(View.GONE);
                    item.setTitle("Покажи лентата с часове");
                }else{
                    ivTime.setVisibility(View.VISIBLE);
                    item.setTitle("Скрий лентата с часове");
                }
                showTime = !showTime;
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                editor = sharedPreferences.edit();
                editor.clear();
                editor.putString("thing", thing);
                editor.putString("scls", scls);
                editor.putInt("num", num);
                editor.putBoolean("showTime", showTime);
                editor.apply();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
