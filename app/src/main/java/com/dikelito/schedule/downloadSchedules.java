package com.dikelito.schedule;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class downloadSchedules extends AsyncTask<JSONObject, Integer, Void> {

    private Context context;
    private NotificationManager notificationManager;
    private AlertDialog dialog;
    private NotificationCompat.Builder builder;
    private boolean firstNotificationShow = true;
    private JSONObject json;

    public downloadSchedules(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        AlertDialog.Builder alertBuilder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertBuilder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_NoActionBar);
        } else {
            alertBuilder = new AlertDialog.Builder(context);
        }
        alertBuilder.setTitle("Моля изчакайте!").setMessage("Няма да отнеме дълго!");
        dialog = alertBuilder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        builder = new NotificationCompat.Builder(context, "14210");
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        updateNotification("0/0", 0, 100);
    }

    @Override
    protected Void doInBackground(JSONObject... jsonObjects) {
        json = jsonObjects[0];
        try {
            JSONArray students = json.getJSONArray("studentsArray"),
                    teachers = json.getJSONArray("teachersArray"),
                    rooms = json.getJSONArray("roomsArray");
            int stundentsLength = students.length(),
                    teachersLength = teachers.length(),
                    roomsLength = rooms.length(),
                    doneSchedules = 0,
                    totalSchedules = stundentsLength + teachersLength + roomsLength;


            File dir = new File(context.getExternalFilesDir(null), "students");
            deleteDirectories();

            for(int i=0; i < stundentsLength; i++) {
                DownloadSchedule(context.getResources().getString(R.string.serverIP), "students", students.getString(i));
                doneSchedules++;
                publishProgress(doneSchedules, totalSchedules);
            }

            for(int i=0; i < teachersLength; i++) {
                DownloadSchedule(context.getResources().getString(R.string.serverIP), "teachers", teachers.getString(i));
                doneSchedules++;
                publishProgress(doneSchedules, totalSchedules);
            }

            for(int i=0; i < roomsLength; i++) {
                DownloadSchedule(context.getResources().getString(R.string.serverIP), "rooms", rooms.getString(i));
                doneSchedules++;
                publishProgress(doneSchedules, totalSchedules);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        updateNotification(values[0] + "/" + values[1], values[0], values[1]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        notificationManager.cancel(14210);
        dialog.dismiss();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("json", json.toString());
        editor.apply();
        Activity a = (Activity) context;
        a.recreate();
    }

    private void updateNotification(String message, int progress, int max) {
        if (firstNotificationShow) {
            Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder = new NotificationCompat.Builder(context, "14210");
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 14210, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentTitle("Downloading")
                    .setOnlyAlertOnce(true)
                    .setOngoing(true)
                    .setContentIntent(pendingIntent)
                    .setSound(uri)
                    .setSmallIcon(R.drawable.notificaton_logo)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo));
            firstNotificationShow = false;
        }
        builder.setContentText(message)
                .setProgress(max, progress, false);

        notificationManager.notify(14210, builder.build());
    }

    private void DownloadSchedule(String URL, String type, String name) {
        java.net.URL url;
        try {
            url = new URL(URL + "/uktc/img/" + type + "-" + name);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = new BufferedInputStream(connection.getInputStream());
            input.mark(input.available());
            input.reset();
            File dir = new File(context.getExternalFilesDir(null), type);
            File file = new File(dir, name + ".png");
            file.getParentFile().mkdirs();
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            byte data[] = new byte[1024*1024];
            int res;
            while ((res = input.read(data)) > 0) {
                outputStream.write(data, 0, res);
            }
            outputStream.flush();
            outputStream.close();
            input.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteDirectories() {
        String []directories = {"students", "teachers", "rooms"};
        for (String type : directories) {
            File dir = new File(context.getExternalFilesDir(null), type);
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    new File(dir, children[i]).delete();
                }
            }
        }
    }
}
