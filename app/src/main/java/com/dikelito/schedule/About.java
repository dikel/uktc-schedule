package com.dikelito.schedule;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class About extends Activity {

    ImageButton ibT;
    ImageView ivLogo;
    MediaPlayer uktc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        ivLogo = (ImageView) findViewById(R.id.ivLogo);
        uktc = MediaPlayer.create(About.this, R.raw.uktc);
        //Start UKTC sound when UKTC logo is held
        ivLogo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                uktc.start();
                return false;
            }
        });
        ibT = (ImageButton) findViewById(R.id.ibT);
        //Open my Telegram profile when the Telegram logo is clicked
        ibT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://telegram.me/dikelito")));
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
