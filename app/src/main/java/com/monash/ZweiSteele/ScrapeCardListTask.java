package com.monash.ZweiSteele;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;

import java.io.IOException;
import java.util.TreeMap;

/**
 * Created by Kin To Pang on 17/06/14.
 */
public class ScrapeCardListTask extends AsyncTask<String, Integer, TreeMap<String, String[]>> {//Params, Progress and Result

    private ProgressDialog dialog;
    private ListActivity activity;
    private ListView listView;
    private EditText editText;

    public ScrapeCardListTask(ListActivity activity, ListView listView, EditText editText) {
        this.activity = activity;
        this.listView = listView;
        this.editText = editText;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(activity);
        dialog.setMessage("Loading Card List.....");
        dialog.show();
    }

    @Override
    protected TreeMap<String, String[]> doInBackground(String... params) {
        TreeMap<String, String[]> cardlistMap = null;
        try {
            cardlistMap = ScraperHelper.getCardList(params[0]);
        } catch (IOException e) {
            this.publishProgress(-1);//something went wrong (most probably the internet dropped out)
        }
        return cardlistMap;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values[0]);

        if ((Integer) values[0] == -1) {
            this.cancel(true);
            Toast.makeText(activity, "Lost Internet Connection!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(activity, MainActivity.class);
            activity.startActivity(i);
        }
    }

    protected void onPostExecute(TreeMap<String, String[]> result) {

        super.onPostExecute(result);
        dialog.dismiss();

        final CardListAdapter adapter = new CardListAdapter(activity, R.layout.cardlist_item, result);

        activity.setListAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedCard = ((TextView) view.findViewById(R.id.cardList_cardID)).getText().toString();

                Intent i = new Intent(activity, SingleCardActivity.class);
                i.putExtra("URL", ((String[]) parent.getItemAtPosition(position))[1]);
                i.putExtra("serial", selectedCard);
                activity.startActivity(i);
            }
        });

        //http://www.androidhive.info/2012/09/android-adding-search-functionality-to-listview/
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {

                adapter.getFilter().filter(s);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

}
