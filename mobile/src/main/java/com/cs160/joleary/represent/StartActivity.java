package com.cs160.joleary.represent;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cs160.joleary.represent.R;

public class StartActivity extends Activity {

    private Button mZipButton;
    private Button mLocationButton;
    public final static String ZIP_CODE = "ZIP_CODE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mZipButton = (Button) findViewById(R.id.zipbutton);
        mLocationButton = (Button) findViewById(R.id.locationbutton);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("This is an invalid ZIP code!");
        alertDialogBuilder.setCancelable(true);
        final AlertDialog invalidZIP = alertDialogBuilder.create();


        mZipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(getBaseContext(), CongressionalActivity.class);

                EditText zipInput = (EditText) findViewById(R.id.zipText);
                CharSequence zipchar = zipInput.getText();
                int zipint = 0;
                try {
                    zipint = Integer.parseInt(zipchar.toString());
                } catch (NumberFormatException e) {
                    invalidZIP.show();
                }

                if (zipint < 0) {
                    invalidZIP.show();
                }

                sendIntent.putExtra(ZIP_CODE, zipint);
                startActivity(sendIntent);
            }
        });

        mLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(getBaseContext(), CongressionalActivity.class);
                sendIntent.putExtra(ZIP_CODE, 94704);
                startActivity(sendIntent);
            }
        });
    }


    
    

}
