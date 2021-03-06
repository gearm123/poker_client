package com.mgroup.pokercalc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Set;

public class UpdateStatistics extends AppCompatActivity {
    private AlertDialog emailDialog;
    private SharedPreferences mPrefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_statistics);
        mPrefs = getSharedPreferences("poker_stat", MODE_PRIVATE);
        editor = mPrefs.edit();
        emailDialog = createEmailDialog();
        emailDialog.show();
    }

    private AlertDialog createEmailDialog() {
        AlertDialog.Builder builder;
        AlertDialog alertDialog;
        final EditText edittextEmail;
        Log.v("poker_calc", "creating dialog");
        builder = new AlertDialog.Builder(this);

        edittextEmail = new EditText(this);
        edittextEmail.setHint(getString(R.string.your_buy_in));
        edittextEmail.setInputType(EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        LinearLayout lay = new LinearLayout(this);
        lay.setOrientation(LinearLayout.VERTICAL);
        lay.addView(edittextEmail);
        builder.setTitle(getString(R.string.stat_title));

        builder.setView(lay);
        builder.setCancelable(false);

        builder.setPositiveButton(getString(R.string.update_stat), null);

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
                        float earned;
                        float oldPrefs;
                        float newVal;
                        float percentage;
                        String buy = edittextEmail.getText().toString();
                        earned = Float.valueOf(buy);
                        emailDialog.dismiss();
                        oldPrefs = mPrefs.getFloat("poker_total", 0);
                        Log.v("poker_calc", "old val " + oldPrefs);
                        newVal = oldPrefs + earned;
                        Log.v("poker_calc", "new val is  " + newVal);
                        if (oldPrefs != 0) {
                            percentage = ((newVal / oldPrefs) * 100) - 100;
                        } else {
                            percentage = 0;
                        }
                        Log.v("poker_calc", "percentage is + " + percentage);
                        editor.putFloat("poker_total", newVal);
                        editor.commit();
                        editor.putFloat("poker_percentage", percentage);
                        editor.commit();
                        updateGraph(String.valueOf(newVal));
                        finish();
                    }
                });
            }
        });


        return alertDialog;
    }

    public void updateGraph(String val) {
        Gson gson = new Gson();
        String jsonText = mPrefs.getString("graph_vals", null);
        ArrayList<String> graphVals = gson.fromJson(jsonText, ArrayList.class);
        if (graphVals != null) {
            if(graphVals.size() <8) {
                graphVals.add(val);
                String jsonTextSend = gson.toJson(graphVals);
                editor.putString("graph_vals", jsonTextSend);
                editor.commit();
            }else{
                for(int i = 0 ; i < 7 ; i++) {
                    graphVals.set(i, graphVals.get(i+1));
                }
                graphVals.set(7, val);
                String jsonTextSend = gson.toJson(graphVals);
                editor.putString("graph_vals", jsonTextSend);
                editor.commit();
            }
        } else {
            graphVals = new ArrayList<>();
            graphVals.add(val);
            String jsonTextSendElse = gson.toJson(graphVals);
            editor.putString("graph_vals", jsonTextSendElse);
            editor.commit();
        }


    }
}
