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
public class ScrapeSeriesTask extends AsyncTask<Character, Integer, TreeMap<String, String>> {//Params, Progress and Result

    private ProgressDialog dialog;
    private ListActivity activity;
    private ListView listView;
    private EditText editText;

    public ScrapeSeriesTask(ListActivity activity, ListView listView, EditText editText) {
        this.activity = activity;
        this.listView = listView;
        this.editText = editText;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(activity);
        dialog.setMessage("Loading Series List.....");
        dialog.show();
    }

    @Override
    protected TreeMap<String, String> doInBackground(Character... params) {
        TreeMap<String, String> seriesMap = null;
        try {
            seriesMap = ScraperHelper.getSeriesList(params[0]);
        } catch (IOException e) {
            this.publishProgress(-1);//something went wrong (most probably the internet dropped out)
        }

        return seriesMap;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        if (values[0] == -1) {
            this.cancel(true);
            Toast.makeText(activity, "Lost Internet Connection!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(activity, MainActivity.class);
            activity.startActivity(i);
        }
    }

    protected void onPostExecute(final TreeMap<String, String> result) {
        super.onPostExecute(result);
        dialog.dismiss();
        final SeriesAdapter adapter = new SeriesAdapter(activity, R.layout.series_list_item, result);

        activity.setListAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedSeries = ((TextView) view.findViewById(R.id.seriesList_seriesName)).getText().toString();

                Intent i = new Intent(activity, CardListActivity.class);
                i.putExtra("URL", parent.getItemAtPosition(position).toString());
                i.putExtra("series", selectedSeries);
                activity.startActivity(i);
            }
        });

        //http://www.androidhive.info/2012/09/android-adding-search-functionality-to-listview/
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                adapter.getFilter().filter(arg0);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {

            }
        });
    }

}
