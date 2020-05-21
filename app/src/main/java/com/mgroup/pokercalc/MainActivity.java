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
    Button getCurrent;
    private SharedPreferences mPrefs;
    public static float myMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        endGameCalc = this.findViewById(R.id.button2);
        updateStat = this.findViewById(R.id.button3);
        viewStats = this.findViewById(R.id.view_stat);
        getCurrent = this.findViewById(R.id.button);
        endGameCalc.setOnClickListener(this);
        updateStat.setOnClickListener(this);
        viewStats.setOnClickListener(this);
        getCurrent.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button) {
            Intent serviceIntent = new Intent(this, CalcCurrentValActivity.class);
            serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(serviceIntent);
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
