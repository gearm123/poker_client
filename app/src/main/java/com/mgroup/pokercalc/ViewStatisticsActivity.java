package com.mgroup.pokercalc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewStatisticsActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences mPrefs;
    SharedPreferences.Editor editor;
    float tVal;
    float percentage;
    TextView balance;
    TextView precent;
    Button clearButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_statistics);
        balance = this.findViewById(R.id.tbalance);
        precent = this.findViewById(R.id.percen);
        clearButton = this.findViewById(R.id.clear);
        clearButton.setOnClickListener(this);
        mPrefs = getSharedPreferences("poker_stat", MODE_PRIVATE);
        editor = mPrefs.edit();
        getVals();
        updateVIews();
    }

    public void getVals(){

        tVal = mPrefs.getFloat("poker_total", 0);
        percentage = mPrefs.getFloat("poker_percentage",0);

    }

    public void updateVIews(){
        balance.setText("your total balance is: "+tVal);
        precent.setText("your percentage from last time is: "+percentage+"%");

    }

    @Override
    public void onClick(View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        editor.putFloat("poker_total", 0);
                        editor.commit();
                        editor.putFloat("poker_percentage", 0);
                        editor.commit();
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.are_you_sure).setPositiveButton(R.string.yes, dialogClickListener)
                .setNegativeButton(R.string.no, dialogClickListener).show();
    }
}
