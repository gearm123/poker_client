package com.mgroup.pokercalc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class ViewStatisticsActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences mPrefs;
    SharedPreferences.Editor editor;
    float tVal;
    float percentage;
    TextView balance;
    TextView precent;
    Button clearButton;
    GraphView graph;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_statistics);
        balance = this.findViewById(R.id.tbalance);
        precent = this.findViewById(R.id.percen);
        clearButton = this.findViewById(R.id.clear);
        graph = this.findViewById(R.id.graph);
        clearButton.setOnClickListener(this);
        mPrefs = getSharedPreferences("poker_stat", MODE_PRIVATE);
        editor = mPrefs.edit();
        getVals();
        updateVIews();
        setUpGraph();
        showGraph();
    }

    public void setUpGraph(){
        graph.getGridLabelRenderer().setVerticalAxisTitle("total balance");
        graph.getGridLabelRenderer().setHorizontalAxisTitle("game num");
    }

    public void showGraph() {
        Gson gson = new Gson();
        String jsonText = mPrefs.getString("graph_vals", null);
        ArrayList<String> graphVals = gson.fromJson(jsonText, ArrayList.class);
        if (graphVals != null) {
            int size = graphVals.size();
            DataPoint[] dataPoints = new DataPoint[8]; // declare an array of DataPoint objects with the same size as your list
            for (int i = 0; i < size; i++) {
                dataPoints[i] = new DataPoint(i, Float.valueOf(graphVals.get(i)));//new DataPoint(i+1, Float.valueOf(graphVals.get(i))); // not sure but I think the second argument should be of type double
            }

            for (int j = size; j < 8; j++) {
                dataPoints[j] = new DataPoint(j, 0);
            }

            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoints);
            graph.addSeries(series);
        }
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
