package com.monash.ZweiSteele;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;

/**
 * Created by Kin To Pang on 15/06/14.
 */
public class CardListActivity extends ListActivity {

    public void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        String link = null;
        if (extras != null) {
            link = extras.getString("URL");
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardlist);

        setTitle(extras.getString("series"));
        ListView listView = getListView();
        listView.setTextFilterEnabled(true);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        EditText editText = (EditText) findViewById(R.id.cardfilter);

        new ScrapeCardListTask(this, listView, editText).execute(link);
    }

}
