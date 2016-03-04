package com.cs160.joleary.represent;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.cs160.joleary.represent.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class CongressionalActivity extends Activity {
    TextView name1;
    TextView name2;
    TextView name3;

    TextView email1;
    TextView email2;
    TextView email3;

    ImageView pic1;
    ImageView pic2;
    ImageView pic3;

    ImageView party1;
    ImageView party2;
    ImageView party3;

    Button button1;
    Button button2;
    Button button3;

    ArrayList<Representative> reps = new ArrayList<>();

    int zip = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional);
        findViews();
        updateRepsInfo();

        Intent intent = getIntent();
        zip = intent.getIntExtra(StartActivity.ZIP_CODE, 0);
        if (zip == -9999) {
            random();
        }
        updateViews();
        sendToWatch();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(getBaseContext(), DetailedActivity.class);
                sendIntent.putExtra("SHOW_PROFILE", reps.get(0).name);
                startActivity(sendIntent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(getBaseContext(), DetailedActivity.class);
                sendIntent.putExtra("SHOW_PROFILE", reps.get(1).name);
                startActivity(sendIntent);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(getBaseContext(), DetailedActivity.class);
                sendIntent.putExtra("SHOW_PROFILE", reps.get(2).name);
                startActivity(sendIntent);
            }
        });

    }

    void findViews() {
        name1 = (TextView) findViewById(R.id.name1);
        name2 = (TextView) findViewById(R.id.name2);
        name3 = (TextView) findViewById(R.id.name3);

        email1 = (TextView) findViewById(R.id.email1);
        email2 = (TextView) findViewById(R.id.email2);
        email3 = (TextView) findViewById(R.id.email3);

        pic1 = (ImageView) findViewById(R.id.pic1);
        pic2 = (ImageView) findViewById(R.id.pic2);
        pic3 = (ImageView) findViewById(R.id.pic3);

        party1 = (ImageView) findViewById(R.id.party1);
        party2 = (ImageView) findViewById(R.id.party2);
        party3 = (ImageView) findViewById(R.id.party3);

        button1 = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
    }

    //searches for representatives. sends info to watch.
    void sendToWatch() {
        Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
        sendIntent.putExtra("IS_NEW_REPDATA", true);

        sendIntent.putExtra("NAME1", reps.get(0).name);
        sendIntent.putExtra("PARTY1", reps.get(0).partyName);

        sendIntent.putExtra("NAME2", reps.get(1).name);
        sendIntent.putExtra("PARTY2", reps.get(1).partyName);

        sendIntent.putExtra("NAME3", reps.get(2).name);
        sendIntent.putExtra("PARTY3", reps.get(2).partyName);

        sendIntent.putExtra("ZIP", Integer.toString(zip));

        startService(sendIntent);
    }

    void updateRepsInfo() {
        Representative first = new Representative("Doge", 0, R.drawable.dogeprofilepic);
        Representative second = new Representative("Nyan Cat", 1, R.drawable.nyan_cat);
        Representative third = new Representative("Trump", 2, R.drawable.trump);

        reps.add(0, first);
        reps.add(1, second);
        reps.add(2, third);
    }

    void updateViews() {
        name1.setText(reps.get(0).name);
        pic1.setImageResource(reps.get(0).imgID);
        email1.setText(reps.get(0).email);
        party1.setImageResource(reps.get(0).partyImg);

        name2.setText(reps.get(1).name);
        pic2.setImageResource(reps.get(1).imgID);
        email2.setText(reps.get(1).email);
        party2.setImageResource(reps.get(1).partyImg);

        name3.setText(reps.get(2).name);
        pic3.setImageResource(reps.get(2).imgID);
        email3.setText(reps.get(2).email);
        party3.setImageResource(reps.get(2).partyImg);
    }

    void random() {
        Random rand = new Random();
        int x = rand.nextInt(1000);
        int y = rand.nextInt(1000);
        zip = rand.nextInt(10000);

        //shuffle
        Collections.shuffle(reps);
//        temp.add(0, reps.get(1));
//        temp.add(1, reps.get(2));
//        temp.add(2, temp);

        updateViews();
        Toast.makeText(getApplicationContext(), "Random Location! (" + x +", " + y+")", Toast.LENGTH_SHORT).show();
    }

}
