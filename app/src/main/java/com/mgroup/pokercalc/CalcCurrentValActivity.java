package com.mgroup.pokercalc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.nio.channels.FileLock;

public class CalcCurrentValActivity extends AppCompatActivity {
    private AlertDialog emailDialog;
    TextView currentValView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_current_val);
        currentValView = this.findViewById(R.id.shekel);
        emailDialog = createEmailDialog();
        emailDialog.show();
    }




    private AlertDialog createEmailDialog() {
        AlertDialog.Builder builder;
        AlertDialog alertDialog;
        final EditText tableMoney;
        final EditText payboxMoney;
        final EditText currentMoney;
        Log.v("poker_calc", "creating dialog");
        builder = new AlertDialog.Builder(this);

        tableMoney = new EditText(this);
        tableMoney.setHint(getString(R.string.table_string));
        tableMoney.setInputType(EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        payboxMoney = new EditText(this);
        payboxMoney.setHint(getString(R.string.paybox_string));
        payboxMoney.setInputType(EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        currentMoney = new EditText(this);
        currentMoney.setHint(getString(R.string.your_string));
        currentMoney.setInputType(EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        LinearLayout lay = new LinearLayout(this);
        lay.setOrientation(LinearLayout.VERTICAL);
        lay.addView(tableMoney);
        lay.addView(payboxMoney);
        lay.addView(currentMoney);
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
                        float table;
                        float paybox;
                        float myMoney;
                        String tables = tableMoney.getText().toString();
                        String payb = payboxMoney.getText().toString();
                        String mym = currentMoney.getText().toString();
                        table = Float.valueOf(tables);
                        paybox = Float.valueOf(payb);
                        myMoney = Float.valueOf(mym);
                        float myShekels =((paybox/table)*myMoney);
                        currentValView.setText("your currently have "+myShekels+" shekels");
                        emailDialog.dismiss();
                    }
                });
            }
        });

        return alertDialog;
    }
}
