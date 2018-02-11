package com.dikelito.schedule;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.InputStream;

public class Food extends AppCompatActivity {

    MenuItem item;
    ImageView iv;
    private MenuItem menuItem;
    boolean isRefreshing = false;
    boolean isConnected = false;
    ConnectivityManager cm;
    NetworkInfo ni;
    //Link to the menu
    final String uRl = "https://docs.google.com/drawings/d/1qbytuwAifEZo2oIABryKSRYr1BucZEHSlL-6rsUQmAQ/pub?w=1920&h=1080";
    final String OFFLINE_MESSAGE = "Трябва да имате връзка с интернет. След като веднъж сте се свързали, менюто ще бъде " +
            "запазено на устройството ви и ще можете да го виждате дори и когато сте офлайн.";

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
        setContentView(R.layout.food);
        File image = new File(getExternalFilesDir(null), "food_menu.png");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Седмично меню");
        iv = (ImageView) findViewById(R.id.iv);
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm.getActiveNetworkInfo() != null){
            ni = cm.getActiveNetworkInfo();
            isConnected = true; //There is internet connection
        }else {
            isConnected = false; //There isn't internet connection
        }
        //If the image is downloaded open it
        if(image.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
            if (theme == R.style.AppThemeDark) {
                myBitmap = changeBitmapContrastBrightness(myBitmap, 1, -63);
            }
            iv.setImageBitmap(myBitmap);
        }else{
            if(isConnected){
                new DownloadImageTask(iv, uRl, image).execute(uRl);
            }else{
                Toast.makeText(this, OFFLINE_MESSAGE, Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.food_activity_actions, menu);
        item = menu.findItem(R.id.refresh_food);
        return super.onCreateOptionsMenu(menu);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView iv;
        String uRl;
        File image;

        public DownloadImageTask(ImageView iv, String uRl, File image) {
            this.iv = iv;
            this.uRl = uRl;
            this.image = image;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            if(image.exists()){
                image.delete();
            }
            DownloadManager mgr = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

            Uri downloadUri = Uri.parse(uRl);
            DownloadManager.Request request = new DownloadManager.Request(
                    downloadUri);

            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI
                            | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false).setTitle("food_menu.png")
                    .setDescription("Food Menu")
                    .setDestinationInExternalFilesDir(getApplicationContext(), "/", "food_menu.png");

            mgr.enqueue(request);
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            iv.setImageBitmap(result);
            if(isRefreshing) {
                menuItem.collapseActionView();
                menuItem.setActionView(null);
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.refresh_food:
                menuItem = item;
                menuItem.setActionView(R.layout.progressbar);
                menuItem.expandActionView();
                isRefreshing = true;
                if(cm.getActiveNetworkInfo() != null){
                    ni = cm.getActiveNetworkInfo();
                    isConnected = true;
                }
                if(isConnected){
                    File image = new File(getExternalFilesDir(null), "food_menu.png");
                    new DownloadImageTask(iv, uRl, image).execute(uRl);
                }else{
                    Toast.makeText(this, OFFLINE_MESSAGE, Toast.LENGTH_LONG).show();
                    menuItem.collapseActionView();
                    menuItem.setActionView(null);
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Bitmap changeBitmapContrastBrightness(Bitmap bmp, float contrast, float brightness)
    {
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
