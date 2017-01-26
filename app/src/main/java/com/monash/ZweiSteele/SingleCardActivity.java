package com.monash.ZweiSteele;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Kin To Pang on 16/06/14.
 */
public class SingleCardActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        String link = null;
        if (extras != null) {
            link = extras.getString("URL");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_card);

        setTitle(extras.getString("serial"));
        new ScrapeCardTask(this).execute(link);
    }
}