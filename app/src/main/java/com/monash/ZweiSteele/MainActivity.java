package com.monash.ZweiSteele;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;


public class MainActivity extends Activity {

    private boolean isConnected = false;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        //checks for a internet connection
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        //checks if the internet connection actally works
        new CheckConnectivityTask().execute();

        Button b4 = (Button) findViewById(R.id.button4);
        Button b5 = (Button) findViewById(R.id.button5);
        Button b6 = (Button) findViewById(R.id.button6);
        Button b7 = (Button) findViewById(R.id.main_goBtn);
        final EditText cardID = (EditText) findViewById(R.id.main_cardid);


        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isConnected) {
                    Toast.makeText(getApplicationContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(MainActivity.this, SeriesListActivity.class);
                    i.putExtra("type", 'b');
                    startActivity(i);
                }

            }
        });

        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isConnected) {
                    Toast.makeText(getApplicationContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(MainActivity.this, SeriesListActivity.class);
                    i.putExtra("type", 't');
                    startActivity(i);
                }

            }
        });

        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isConnected) {
                    Toast.makeText(getApplicationContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(MainActivity.this, SeriesListActivity.class);
                    i.putExtra("type", 'e');
                    startActivity(i);
                }

            }
        });

        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isConnected) {
                    Toast.makeText(getApplicationContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
                } else {
                    String host = "/code/cardlist.html?card=WS_";

                    String userInput = cardID.getText().toString();

                    Intent i = new Intent(getApplicationContext(), SingleCardActivity.class);
                    i.putExtra("URL", host + userInput);
                    i.putExtra("serial", userInput);
                    i.putExtra("search", true);
                    startActivity(i);
                }
            }
        });
    }

    //http://stackoverflow.com/questions/8919083/checking-host-reachability-availability-in-android
    private class CheckConnectivityTask extends AsyncTask<Void, Void, Void> {//Params, Progress and Result

        @Override
        protected Void doInBackground(Void... params) {
            boolean exists = false;

            try {
                SocketAddress sockaddr = new InetSocketAddress("www.heartofthecards.com", 80);
                // Create an unbound socket
                Socket sock = new Socket();

                // This method will block no more than timeoutMs.
                // If the timeout occurs, SocketTimeoutException is thrown.
                int timeoutMs = 2000;   // 2 seconds
                sock.connect(sockaddr, timeoutMs);
                isConnected = true;
            } catch (SocketTimeoutException e) {
                isConnected = false;
            } catch (IOException e) {
                isConnected = false;
            }
            return null;
        }
    }
}
