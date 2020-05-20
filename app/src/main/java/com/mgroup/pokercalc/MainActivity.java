package com.mgroup.pokercalc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private AlertDialog emailDialog;
    TextView myourMoney;
    TextView mbuyIn;
    TextView mtitle;
    TextView stat;
    Button msubmit;
    Button updateStat;
    Button endGameCalc;
    Button viewStats;
    private SharedPreferences mPrefs;
    public static float myMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        endGameCalc = this.findViewById(R.id.button2);
        updateStat = this.findViewById(R.id.button3);
        viewStats = this.findViewById(R.id.view_stat);
        endGameCalc.setOnClickListener(this);
        updateStat.setOnClickListener(this);
        viewStats.setOnClickListener(this);
    }

    private AlertDialog createEmailDialog() {
        AlertDialog.Builder builder;
        AlertDialog alertDialog;
        final EditText edittextEmail;
        final EditText edittextName;
        final EditText edittextYour;
        Log.v("poker_calc", "creating dialog");
        builder = new AlertDialog.Builder(this);

        edittextEmail = new EditText(this);
        edittextEmail.setHint(getString(R.string.table_string));
        edittextEmail.setInputType(EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        edittextName = new EditText(this);
        edittextName.setHint(getString(R.string.paybox_string));
        edittextName.setInputType(EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        edittextYour = new EditText(this);
        edittextYour.setHint(getString(R.string.your_string));
        edittextYour.setInputType(EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        LinearLayout lay = new LinearLayout(this);
        lay.setOrientation(LinearLayout.VERTICAL);
        lay.addView(edittextName);
        lay.addView(edittextEmail);
        lay.addView(edittextYour);
        builder.setTitle(getString(R.string.title));

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
                        String moneyOnTable;
                        String moneyInpaybox;
                        String yourMoney;
                        float moneyOnTableLong;
                        float moneyInpayboxLong;
                        float yourMoneyLong;
                        float realMoney;
                        float buyIn;
                        float tStat;
                        moneyOnTable = edittextEmail.getText().toString();
                        Log.v("poker_calc", "on table result edit" + moneyOnTable);
                        moneyInpaybox = edittextName.getText().toString();
                        Log.v("poker_calc", "on paybox result edit" + moneyInpaybox);
                        yourMoney = edittextYour.getText().toString();
                        Log.v("poker_calc", "your result edit" + yourMoney);
                        moneyOnTableLong = Float.valueOf(moneyOnTable);
                        Log.v("poker_calc", "on table long result edit" + moneyOnTable);
                        moneyInpayboxLong = Float.valueOf(moneyInpaybox);
                        Log.v("poker_calc", "on paybox long result edit" + moneyInpaybox);
                        yourMoneyLong = Float.valueOf(yourMoney);
                        myMoney = yourMoneyLong;
                        Log.v("poker_calc", "your long result edit" + yourMoneyLong);
                        realMoney = (moneyInpayboxLong / moneyOnTableLong) * yourMoneyLong;
                        Log.v("poker_calc", "real money" + realMoney);
                        buyIn = (moneyInpayboxLong / moneyOnTableLong) * 10000;
                        Log.v("poker_calc", "buy in money" + buyIn);
                        myourMoney.setText("you have " + String.valueOf(realMoney) + " shekels");
                        mbuyIn.setText("buy in is now " + String.valueOf(buyIn) + "shekels");
                        emailDialog.dismiss();
                        tStat = getTotalVal();
                        stat.setText("your total balance is " + String.valueOf(tStat));
                    }
                });
            }
        });

        return alertDialog;
    }

    public float getTotalVal() {

        return mPrefs.getFloat("poker_stat", 0);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button) {
            finish();
        } else if (view.getId() == R.id.button3) {
            Intent serviceIntent = new Intent(this, UpdateStatistics.class);
            serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(serviceIntent);
        } else if (view.getId() == R.id.button2) {
            Log.v("poker_calc", "starting end game calc");
            Intent serviceIntent = new Intent(this, EndGameCalcActivity.class);
            serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(serviceIntent);
        } else if (view.getId() == R.id.view_stat) {
            Intent serviceIntent = new Intent(this, ViewStatisticsActivity.class);
            serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(serviceIntent);

        }
    }
}
