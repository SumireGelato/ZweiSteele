package com.monash.ZweiSteele;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;


/**
 * Created by Kin To Pang on 15/06/14.
 */
public class SeriesListActivity extends ListActivity {

    public void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        char type = 0;
        if (extras != null) {
            type = extras.getChar("type");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.series_list);

        ListView listView = getListView();
        listView.setTextFilterEnabled(true);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        EditText editText = (EditText) findViewById(R.id.seriesfilter);
        //old way (hack)
        //new ScrapeSeriesTask(this).execute(this, listView, type, editText);
        //new way
        new ScrapeSeriesTask(this, listView, editText).execute(type);
    }

}
