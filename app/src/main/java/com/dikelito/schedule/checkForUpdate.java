package com.dikelito.schedule;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class checkForUpdate extends AsyncTask<String, Void, JSONObject> {

    private Context context;
    private String currentVersion;

    public checkForUpdate(Context context) {
        this.context = context;
    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        try {
            String serverIP = strings[0];
            currentVersion = strings[1];
            URL url = new URL(serverIP + "/uktc/check-for-update?ver=" + currentVersion);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                InputStreamReader inputStreamReader = new InputStreamReader(in);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String chunk;

                while ((chunk = bufferedReader.readLine()) != null) {
                    stringBuilder.append(chunk);
                }

                JSONObject json = new JSONObject(stringBuilder.toString());
                return json;

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(final JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        final JSONObject json = jsonObject;
        if (jsonObject != null) {
            try {
                if (json.getString("version").equals(currentVersion)) {
                    //Toast.makeText(context, "Up to date!", Toast.LENGTH_LONG).show();
                } else {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_NoActionBar);
                    } else {
                        builder = new AlertDialog.Builder(context);
                    }
                    builder.setTitle("Налична актуализация")
                            .setMessage("Има промяна по програмите. Желаете ли да ги обновите? (Размер: " + json.getString("size") + ")")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    new downloadSchedules(context).execute(json);
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("По-късно", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "Няма връзка със сървъра!", Toast.LENGTH_LONG).show();
        }
    }
}
