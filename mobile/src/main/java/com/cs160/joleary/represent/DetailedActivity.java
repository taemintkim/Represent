package com.cs160.joleary.represent;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.CardView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs160.joleary.represent.R;

public class DetailedActivity extends Activity {
    MyApp myapp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        myapp = (MyApp) getApplicationContext();

        TextView name = (TextView) findViewById(R.id.detail_name);
        ImageView pic = (ImageView) findViewById(R.id.profile_pic);
        TextView party = (TextView) findViewById(R.id.partyText);
        TextView termDate = (TextView) findViewById(R.id.termDate);
        TextView committees = (TextView) findViewById(R.id.committees_content);
        TextView bills = (TextView) findViewById(R.id.billslistView);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            String name0 = extras.getString("SHOW_PROFILE");
            Representative rep = myapp.getRep(0);
            for (Representative rep0: myapp.reps) {
                if (rep0.name.equals(name0)) {rep = rep0;}
            }
            String longname = rep.title + " " + rep.name;
            name.setText(longname);
            pic.setImageBitmap(rep.profileImg);
            party.setText(rep.longParty);
            termDate.setText("Term ends on " + rep.termEndDate);
            committees.setText(rep.committees);
            bills.setText(rep.bills);
        }
    }

}
