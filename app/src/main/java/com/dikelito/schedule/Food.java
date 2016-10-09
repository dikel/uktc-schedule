package com.dikelito.schedule;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.InputStream;

public class Food extends Activity {

    MenuItem item;
    ImageView iv;
    private MenuItem menuItem;
    boolean isRefreshing = false;
    boolean isConnected = false;
    ConnectivityManager cm;
    NetworkInfo ni;
    //Link to the menu
    String uRl = "https://docs.google.com/drawings/d/1qbytuwAifEZo2oIABryKSRYr1BucZEHSlL-6rsUQmAQ/pub?w=1920&h=1080";
    File image = new File(Environment.getExternalStorageDirectory() + "/Download", "food_menu.png");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Седмично меню");
        iv = (ImageView) findViewById(R.id.iv);
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm.getActiveNetworkInfo() != null){
            ni = cm.getActiveNetworkInfo();
            isConnected = true; //There is internet connection
        }
        else isConnected = false; //There isn't internet connection
        //If the image is downloaded open it
        if(image.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
            iv.setImageBitmap(myBitmap);
        }else{
            if(isConnected){
                new DownloadImageTask(iv, uRl, image).execute("https://docs.google.com/drawings/d/1qbytuwAifEZo2oIABryKSRYr1BucZEHSlL-6rsUQmAQ/pub?w=1920&h=1080");
            }else{
                Toast.makeText(this, "Трябва да имате връзка с интернет. След като веднъж сте се свързали," +
                        " менюто ще бъде запазено на устройството ви и ще можете да го виждате дори" +
                        " и когато сте офлайн.", Toast.LENGTH_LONG).show();
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
                    .setDestinationInExternalPublicDir("/Download", "food_menu.png");

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
                    new DownloadImageTask(iv, uRl, image).execute("https://docs.google.com/drawings/d/1qbytuwAifEZo2oIABryKSRYr1BucZEHSlL-6rsUQmAQ/pub?w=1920&h=1080");
                }else{
                    Toast.makeText(this, "Трябва да имате връзка с интернет. След като веднъж сте се свързали," +
                            " менюто ще бъде запазено на устройството ви и ще можете да го виждате дори" +
                            " и когато сте офлайн.", Toast.LENGTH_LONG).show();
                    menuItem.collapseActionView();
                    menuItem.setActionView(null);
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
