package com.monash.ZweiSteele;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;


/**
 * Created by Kin To Pang on 15/06/14.
 */

public class ScrapeCardTask extends AsyncTask<String, Integer, HashMap<String, String>> {//Params, Progress and Result

    private ProgressDialog dialog;
    private Activity activity;

    public ScrapeCardTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(activity);
        dialog.setMessage("Loading Card.....");
        dialog.show();
    }


    @Override
    protected HashMap<String, String> doInBackground(String... params) {
        HashMap<String, String> result = new HashMap<String, String>();
        try {
            result = ScraperHelper.getSingleCard(params[0]);
        } catch (IOException e) {
            //something went wrong either the user typed in something bad on the home page or the internet dropped out
            this.publishProgress(-1);
        }
        return result;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values[0]);

        if (values[0] == -1) {
            this.cancel(true);
            boolean isSearch = activity.getIntent().getBooleanExtra("search", false);

            if (isSearch) {
                Toast.makeText(activity, "Card Not Found!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, "Lost Internet Connection!", Toast.LENGTH_SHORT).show();
            }
            Intent i = new Intent(activity, MainActivity.class);
            activity.startActivity(i);
        }
    }

    protected void onPostExecute(HashMap<String, String> result) {
        //double checks - you can never be too sure!
        if (result != null) {
            super.onPostExecute(result);
            dialog.dismiss();

            if (result.size() != 0) {
                TextView engNametv = (TextView) activity.findViewById(R.id.singleCard_cardnameEng);
                engNametv.setText(result.get("Eng Name"));
                TextView jpNametv = (TextView) activity.findViewById(R.id.singleCard_cardnameJP);
                jpNametv.setText(result.get("Jp Name"));
                String imageSrc = "http://www.heartofthecards.com/" + result.get("Image Source").split("/", 3)[2];
                ImageView iv = (ImageView) activity.findViewById(R.id.singleCard_cardPic);
                new DownloadImageTask(iv).execute(imageSrc);
                TextView cardNumtv = (TextView) activity.findViewById(R.id.singleCard_cardNo);
                cardNumtv.setText(result.get("Card No."));
                TextView colortv = (TextView) activity.findViewById(R.id.singleCard_color);
                colortv.setText(result.get("Color"));
                TextView typetv = (TextView) activity.findViewById(R.id.singleCard_type);
                typetv.setText(result.get("Type"));
                TextView powertv = (TextView) activity.findViewById(R.id.singleCard_power);
                powertv.setText(result.get("Power"));
                TextView soultv = (TextView) activity.findViewById(R.id.singleCard_soul);
                soultv.setText(result.get("Soul"));
                TextView triggerstv = (TextView) activity.findViewById(R.id.singleCard_tiggers);
                triggerstv.setText(result.get("Triggers"));
                TextView raritytv = (TextView) activity.findViewById(R.id.singleCard_rarity);
                raritytv.setText(result.get("Rarity"));
                TextView sidetv = (TextView) activity.findViewById(R.id.singleCard_side);
                sidetv.setText(result.get("Side"));
                TextView leveltv = (TextView) activity.findViewById(R.id.singleCard_level);
                leveltv.setText(result.get("Level"));
                TextView costtv = (TextView) activity.findViewById(R.id.singleCard_cost);
                costtv.setText(result.get("Cost"));
                TextView trait1tv = (TextView) activity.findViewById(R.id.singleCard_trait1);
                trait1tv.setText(result.get("Keyword 1"));
                TextView trait2tv = (TextView) activity.findViewById(R.id.singleCard_trait2);
                trait2tv.setText(result.get("Keyword 2"));
                TextView engTransEffecttv = (TextView) activity.findViewById(R.id.singleCard_effectEng);
                engTransEffecttv.setText(result.get("English Card Text"));
                TextView jpEffecttv = (TextView) activity.findViewById(R.id.singleCard_effectJp);
                jpEffecttv.setText(result.get("Original Card Text"));
            } else {
                Toast.makeText(activity, "Lost Internet Connection!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(activity, MainActivity.class);
                activity.startActivity(i);
            }
        }
    }

    //http://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android
    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
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
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}