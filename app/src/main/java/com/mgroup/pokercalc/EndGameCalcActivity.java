package com.mgroup.pokercalc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import javax.net.ssl.HttpsURLConnection;

import info.guardianproject.netcipher.NetCipher;

public class EndGameCalcActivity extends AppCompatActivity {
    private AlertDialog emailDialog;
    String realMoney = "";
    boolean isSent = false;
    TextView showMoney = null;
    TextView waiting = null;
     LocalBroadcastManager localBroadcastManager = null;
     BroadcastReceiver broadcastReceiver = null;
     Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game_calc);
        mContext = this;
        showMoney = this.findViewById(R.id.real_money);
        waiting = this.findViewById(R.id.waiting);
        configReceiver(this);
        emailDialog = createEmailDialog();
        emailDialog.show();
    }


    private AlertDialog createEmailDialog() {
        AlertDialog.Builder builder;
        AlertDialog alertDialog;
        final EditText numPlay;
        final EditText edittextName;
        final EditText edittextYour;
        final EditText monPaybox;
        Log.v("poker_calc", "creating dialog");
        builder = new AlertDialog.Builder(this);

        edittextName = new EditText(this);
        edittextName.setHint(getString(R.string.name));
        edittextName.setInputType(EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        numPlay = new EditText(this);
        numPlay.setHint(getString(R.string.num_play));
        numPlay.setInputType(EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);


        edittextYour = new EditText(this);
        edittextYour.setHint(getString(R.string.your_string));
        edittextYour.setInputType(EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        monPaybox = new EditText(this);
        monPaybox.setHint(getString(R.string.paybox_string));
        monPaybox.setInputType(EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        LinearLayout lay = new LinearLayout(this);
        lay.setOrientation(LinearLayout.VERTICAL);
        lay.addView(edittextName);
        lay.addView(numPlay);
        lay.addView(edittextYour);
        lay.addView(monPaybox);
        builder.setTitle(getString(R.string.stat_title));

        builder.setView(lay);

        builder.setCancelable(false);

        builder.setPositiveButton(getString(R.string.ok), null);

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                emailDialog.dismiss();
                finish();
            }
        });

        alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                Button b = emailDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String name = edittextName.getText().toString();
                        String numOfPlayers = numPlay.getText().toString();
                        String yourMoney = edittextYour.getText().toString();
                        String payBox = monPaybox.getText().toString();
                        waiting.setVisibility(View.VISIBLE);
                        sendPostReq(name, numOfPlayers, yourMoney, payBox);
                        emailDialog.dismiss();
                    }
                });
            }
        });

        return alertDialog;
    }


    public void sendPostReq(final String name, final String numPlay, final String money, final String payBox) {
        Thread t = new Thread() {
            public void run() {
                String tableMoney;
                Log.v("poker_calc", "sending post request");
                try {
                    while (!isSent) {
                        Log.v("poker_calc", "sending http request");
                        String url = "https://mysterious-savannah-21835.herokuapp.com/currency";
                        HttpsURLConnection client = NetCipher.getHttpsURLConnection(url);
                        client.setRequestMethod("POST");
                        client.setDoOutput(true);
                        Uri.Builder builder = new Uri.Builder()
                                .appendQueryParameter("name", name)
                                .appendQueryParameter("numplay", numPlay)
                                .appendQueryParameter("money", money);
                        String query = builder.build().getEncodedQuery();
                        OutputStream os = client.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                        writer.write(query);
                        writer.flush();
                        writer.close();
                        os.close();
                        int responseCode = client.getResponseCode();
                        if (responseCode != HttpURLConnection.HTTP_OK) {

                            Log.v("poker_calc", "http not ok");
                            return;
                        }

                        BufferedReader in = new BufferedReader(new InputStreamReader(
                                client.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        if (response.toString().equals("0")) {
                            Thread.sleep((1000));
                            Log.v("poker_calc", "waiting for other players - trying again");
                        } else {
                            tableMoney = response.toString();
                            realMoney = calcRealMoney(tableMoney, payBox, money);
                            isSent = true;
                            Intent intent = new Intent("got_result");
                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                        }
                        in.close();
                    }

                } catch (Exception e) {
                    Log.v("poker_calc", "exception in receiving response  " + e);

                }
                Log.v("poker_calc", "returning val of " + realMoney);
            }
        };
        t.start();
    }

    public String calcRealMoney(String tableMoney, String payBox, String myMoney) {
        float tableMoneyFloat = Float.parseFloat(tableMoney);
        float payBoxFloat = Float.parseFloat(payBox);
        float myMoneyFloat = Float.parseFloat(myMoney);
        float FinalFLoatVal;
        Log.v("poker_calc", "values before calc are - table: " + tableMoneyFloat + " paybox: " + payBoxFloat + " my money : " + myMoneyFloat);
        FinalFLoatVal = ((payBoxFloat / tableMoneyFloat) * myMoneyFloat);
        Log.v("poker_calc", "real money worth is " + FinalFLoatVal);
        return String.valueOf(FinalFLoatVal);

    }

    public void configReceiver(Context context) {
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.v("poker_calc", "value received - changing views");
                waiting.setVisibility(View.INVISIBLE);
                showMoney.setText("you have "+realMoney+" shekels");
                showMoney.setVisibility(View.VISIBLE);
            }


        };
        localBroadcastManager.registerReceiver(broadcastReceiver, new IntentFilter("got_result"));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }



}