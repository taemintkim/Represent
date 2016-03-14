package com.cs160.joleary.represent;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cs160.joleary.represent.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Wearable;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.fabric.sdk.android.Fabric;

public class StartActivity extends Activity {

    LocationManager locationManager;
    double mLongitude;
    double mLatitude;

    private Button mZipButton;
    private Button mLocationButton;
    public final static String ZIP_CODE = "ZIP_CODE";
    MyApp myapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        myapp = (MyApp) getApplicationContext();

        mZipButton = (Button) findViewById(R.id.zipbutton);
        mLocationButton = (Button) findViewById(R.id.locationbutton);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("This is an invalid ZIP code!");
        alertDialogBuilder.setCancelable(true);
        final AlertDialog invalidZIP = alertDialogBuilder.create();

        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new MyLocationListener();
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, locationListener);



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
                sendIntent.putExtra(ZIP_CODE, -1);
                myapp.lat = mLatitude;
                myapp.lon = mLongitude;
                startActivity(sendIntent);
            }
        });
    }


    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {
            mLongitude = loc.getLongitude();
            mLatitude = loc.getLatitude();
            String longitude = "Longitude: " + loc.getLongitude();
            Log.v("longitude", longitude);
            String latitude = "Latitude: " + loc.getLatitude();
            Log.v("latitude", latitude);


        /*------- To get city name from coordinates -------- */
            String gcurl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=";
            String gcurl2 = "&key=AIzaSyBV2HWmMkwaZ5ex-Z4DH1dHCtyfF35BM78";
            String fullurl = gcurl + mLatitude + "," + mLongitude;

            myapp.geourl = fullurl;

//            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
//            List<Address> addresses;
//            try {
//                addresses = gcd.getFromLocation(loc.getLatitude(),
//                        loc.getLongitude(), 1);
//                if (addresses.size() > 0) {
//                    Log.d("Address", addresses.get(0).toString());
//                    myapp.county = addresses.get(0).getSubAdminArea();
//                    if (myapp.county == null) {
//                        myapp.county =addresses.get(0).getLocality();
//                        if (myapp.county == null) {
//                            myapp.county = "";
//                        }
//                    }
//                    myapp.state = addresses.get(0).getAdminArea();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }


}
