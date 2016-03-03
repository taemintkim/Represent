package com.cs160.joleary.represent;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.cs160.joleary.represent.R;

import java.util.ArrayList;

public class CongressionalActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional);
        Intent intent = getIntent();
        int zip = intent.getIntExtra(StartActivity.ZIP_CODE, 94704);
        search();

        Button button1 = (Button) findViewById(R.id.button);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(getBaseContext(), DetailedActivity.class);
                startActivity(sendIntent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(getBaseContext(), DetailedActivity.class);
                startActivity(sendIntent);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(getBaseContext(), DetailedActivity.class);
                startActivity(sendIntent);
            }
        });

    }

    //searches for representatives. sends info to watch.
    void search() {

        Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
        sendIntent.putExtra("IS_NEW_REPDATA", true);

        sendIntent.putExtra("NAME1", "Doge");
        sendIntent.putExtra("PARTY1", "dem");

        sendIntent.putExtra("NAME2", "Nyan Cat");
        sendIntent.putExtra("PARTY2", "rep");

        sendIntent.putExtra("NAME3", "Trump");
        sendIntent.putExtra("PARTY3", "ind");

        startService(sendIntent);
    }

}
