package com.cs160.joleary.represent;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs160.joleary.represent.R;

public class DetailedActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        TextView name = (TextView) findViewById(R.id.detail_name);
        ImageView pic = (ImageView) findViewById(R.id.profile_pic);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            switch (extras.getString("SHOW_PROFILE")) {
                case "Doge":
                    pic.setImageResource(R.drawable.dogeprofilepic);
                    break;
                case "Nyan Cat":
                    pic.setImageResource(R.drawable.nyan_cat);
                    break;
                case "Trump":
                    pic.setImageResource(R.drawable.trump);
                    break;
            }
            name.setText(extras.getString("SHOW_PROFILE"));
        }
    }

}
