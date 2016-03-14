package com.cs160.joleary.represent;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import io.fabric.sdk.android.Fabric;

public class CongressionalActivity extends Activity {
    private static final String TWITTER_KEY = "qFL4OPP5BhqrHDUaSpJV5GUOy";
    private static final String TWITTER_SECRET = "aCKOooqGBeuAxj7RJcxsb51NgRZO6gVxTMsA1BfTZGifd7dAZ7";
    private TwitterApiClient twitterApiClient;

    TextView name1;
    TextView name2;
    TextView name3;

    ImageView pic1;
    ImageView pic2;
    ImageView pic3;

    ImageView party1;
    ImageView party2;
    ImageView party3;

    Button detailbutton1;
    Button detailbutton2;
    Button detailbutton3;

    Button sitebutton1;
    Button sitebutton2;
    Button sitebutton3;

    Button emailbutton1;
    Button emailbutton2;
    Button emailbutton3;

    TextView tweet1;
    TextView tweet2;
    TextView tweet3;

    ProgressDialog dialog;

    double mLongitude;
    double mLatitude;
    private int doneCount = 0;
    public static String sunlightURL1 = "http://congress.api.sunlightfoundation.com";
    public static String sunlightURL2 = "&apikey=8385878a45504ea29ded263a1d2f3f81";
    MyApp myapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional);
        myapp = (MyApp) getApplicationContext();

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        findViews();
        if (myapp.reps.isEmpty()) {
            myapp.reps.add(0, new Representative());
            myapp.reps.add(1, new Representative());
            myapp.reps.add(2, new Representative());
        }

        Intent intent = getIntent();
        int intentzip = intent.getIntExtra(StartActivity.ZIP_CODE, -99);
        Log.d("CongressionalACtivity", "new activity with zip: " + intentzip);
        if (intentzip == -9999) { //this activity is triggered by a shake.
            random();
            fetchRepData(myapp.zip);
        } else if (intentzip == -99) { //this activity should not find new data
            updateViews(); //use stored data.
        } else if (intentzip == -1) { // get current location
            fetchRepData(mLatitude, mLongitude);
        } else { //find new data
            myapp.zip = intentzip;
            fetchRepData(intentzip);
        }

        detailbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(getBaseContext(), DetailedActivity.class);
                sendIntent.putExtra("SHOW_PROFILE", myapp.getRep(0).name);
                startActivity(sendIntent);
            }
        });

        detailbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(getBaseContext(), DetailedActivity.class);
                sendIntent.putExtra("SHOW_PROFILE", myapp.getRep(1).name);
                startActivity(sendIntent);
            }
        });

        detailbutton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(getBaseContext(), DetailedActivity.class);
                sendIntent.putExtra("SHOW_PROFILE", myapp.getRep(2).name);
                startActivity(sendIntent);
            }
        });


        emailbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb = makeAlertDialog(0);
                adb.setMessage(myapp.getRep(0).email);
                adb.create();
                adb.show();
            }
        });
        emailbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb = makeAlertDialog(1);
                adb.setMessage(myapp.getRep(1).email);
                adb.create();
                adb.show();
            }
        });
        emailbutton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb = makeAlertDialog(2);
                adb.setMessage(myapp.getRep(2).email);
                adb.create();
                adb.show();
            }
        });

        sitebutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb = makeSiteDialog(0);
                adb.setMessage(myapp.getRep(0).URL);
                adb.create();
                adb.show();
            }
        });

        sitebutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb = makeSiteDialog(1);
                adb.setMessage(myapp.getRep(1).URL);
                adb.create();
                adb.show();
            }
        });

        sitebutton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb = makeSiteDialog(2);
                adb.setMessage(myapp.getRep(2).URL);
                adb.create();
                adb.show();
            }
        });
    }

    AlertDialog.Builder makeAlertDialog(final int repindex) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setPositiveButton("SEND EMAIL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_EMAIL, myapp.getRep(repindex).email);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
        alertDialogBuilder.setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return alertDialogBuilder;
    }

    AlertDialog.Builder makeSiteDialog(final int repindex) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setPositiveButton("OPEN SITE ON BROWSER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Uri webpage = Uri.parse(myapp.getRep(repindex).URL);
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
        alertDialogBuilder.setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return alertDialogBuilder;
    }

    void findViews() {
        name1 = (TextView) findViewById(R.id.name1);
        name2 = (TextView) findViewById(R.id.name2);
        name3 = (TextView) findViewById(R.id.name3);

        pic1 = (ImageView) findViewById(R.id.pic1);
        pic2 = (ImageView) findViewById(R.id.pic2);
        pic3 = (ImageView) findViewById(R.id.pic3);

        party1 = (ImageView) findViewById(R.id.party1);
        party2 = (ImageView) findViewById(R.id.party2);
        party3 = (ImageView) findViewById(R.id.party3);

        detailbutton1 = (Button) findViewById(R.id.detailbutton1);
        detailbutton2 = (Button) findViewById(R.id.detailbutton2);
        detailbutton3 = (Button) findViewById(R.id.detailbutton3);

        sitebutton1 = (Button) findViewById(R.id.sitebutton1);
        sitebutton2 = (Button) findViewById(R.id.sitebutton2);
        sitebutton3 = (Button) findViewById(R.id.sitebutton3);

        emailbutton1 = (Button) findViewById(R.id.emailbutton1);
        emailbutton2 = (Button) findViewById(R.id.emailbutton2);
        emailbutton3 = (Button) findViewById(R.id.emailbutton3);

        tweet1 = (TextView) findViewById(R.id.tweet1);
        tweet2 = (TextView) findViewById(R.id.tweet2);
        tweet3 = (TextView) findViewById(R.id.tweet3);

    }

    //searches for representatives. sends info to watch.
    void sendToWatch() {
        Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
        sendIntent.putExtra("IS_NEW_REPDATA", true);

        sendIntent.putExtra("NAME1", myapp.reps.get(0).name);
        sendIntent.putExtra("PARTY1", myapp.reps.get(0).partyName);

        sendIntent.putExtra("NAME2", myapp.reps.get(1).name);
        sendIntent.putExtra("PARTY2", myapp.reps.get(1).partyName);

        sendIntent.putExtra("NAME3", myapp.reps.get(2).name);
        sendIntent.putExtra("PARTY3", myapp.reps.get(2).partyName);

        sendIntent.putExtra("ZIP", Integer.toString(myapp.zip));
        String votes = get2012Election(myapp.county, myapp.state);
        sendIntent.putExtra("VOTES", votes);

        startService(sendIntent);
    }

    void updateViews() {
        name1.setText(myapp.reps.get(0).name);
        pic1.setImageBitmap(myapp.getRep(0).profileImg);
        party1.setImageResource(myapp.reps.get(0).partyImg);
        tweet1.setText(myapp.reps.get(0).tweet);

        name2.setText(myapp.reps.get(1).name);
        pic2.setImageBitmap(myapp.getRep(1).profileImg);
        party2.setImageResource(myapp.reps.get(1).partyImg);
        tweet2.setText(myapp.reps.get(1).tweet);

        name3.setText(myapp.reps.get(2).name);
        pic3.setImageBitmap(myapp.getRep(2).profileImg);
        pic3.setImageBitmap(myapp.getRep(2).profileImg);
        pic3.setImageBitmap(myapp.getRep(2).profileImg);
        party3.setImageResource(myapp.reps.get(2).partyImg);
        tweet3.setText(myapp.reps.get(2).tweet);
    }

    void random() {
        int[] ys = {58501, 83214, 22664};
        Random rand = new Random();
        myapp.zip = ys[rand.nextInt(3)];

        updateViews();
        Toast.makeText(getApplicationContext(), "Random Location! New ZIP: " + myapp.zip, Toast.LENGTH_SHORT).show();
    }

    /**
     * Check URL connection, update views, send data to watch.
     */
    void fetchRepData(int zip) {
        //search zip
        String gcurl = "https://maps.googleapis.com/maps/api/geocode/json?address=";
        String gcurl2 = "&key=AIzaSyBV2HWmMkwaZ5ex-Z4DH1dHCtyfF35BM78";
        String searchZipURL = sunlightURL1 + "/legislators/locate?zip=" + zip + sunlightURL2;
        myapp.geourl = gcurl + zip + gcurl2;
        checkConnection(searchZipURL);
    }

    void fetchRepData(double lat, double lon) {
        String searchCoordi = sunlightURL1 + "/legislators/locate?latitude=" + myapp.lat + "&longitude=" + myapp.lon + sunlightURL2;
        checkConnection(searchCoordi);
    }


    public void checkConnection(String stringUrl) {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask().execute(stringUrl);
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("No network connection available");
        }
    }

    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // displayed in the UI by the AsyncTask's onPostExecute method.
    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                String urlresult = downloadUrl(urls[0]);
                getVoteStats();
                parseRepProfiles(urlresult);
                getTwitterApiClient();
                sendToWatch();
                while (doneCount < 3) {
                }
                downloadImgs();
                return "successful";
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        //         onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            updateViews();
        }
    }


    public TwitterApiClient getTwitterApiClient() {
        doneCount = 0;
        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
            @Override
            public void success(Result<AppSession> result) {
                Log.d("Twitter Auth", "loginGuest.callback.success called");
                AppSession guestAppSession = result.data;
                twitterApiClient = TwitterCore.getInstance().getApiClient(guestAppSession);
                for (int i = 0; i < 3; i++) {
                    getTwitterPic(i);
                }
                Toast twitterfail = Toast.makeText(getApplicationContext(), "done getting twitter pics", Toast.LENGTH_SHORT);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("Twitter Auth", "loginGuest.callback.failure called");
                // unable to get an AppSession with guest auth
                Toast twitterfail = Toast.makeText(getApplicationContext(), "Twitter Authentication Failed!", Toast.LENGTH_SHORT);
            }
        });
        return twitterApiClient;
    }

    void getTwitterPic(final int repIndex) {
        StatusesService twapiclient = twitterApiClient.getStatusesService();
        twapiclient.userTimeline(null, myapp.getRep(repIndex).twitter_id, null, null, null, null, null, null, null, new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> listResult) {

//                System.out.println("listResult"+listResult.data.size());
//                System.out.println("listResult"+listResult.data.get(0).user);
//                System.out.println("listResult" + listResult.data.get(0).text);
                myapp.getRep(repIndex).tweet = listResult.data.get(0).text;
                String origurl = listResult.data.get(0).user.profileImageUrl;
                myapp.getRep(repIndex).twitterImageURL = origurl.replaceAll("_normal", "");
                doneCount++;
            }

            @Override
            public void failure(TwitterException e) {

            }
        });
    }

    void downloadImgs() {
        for (int i = 0; i < 3; i++) {
            Bitmap mIcon;
            Representative myrep = myapp.getRep(i);
            try {
                URL url = new URL(myrep.twitterImageURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(100000 /* milliseconds */);
                conn.setConnectTimeout(150000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                Log.d("HTTP CONNECTION", "The response is: " + response);
                InputStream in = conn.getInputStream();
                mIcon = BitmapFactory.decodeStream(in);
                myrep.profileImg = mIcon;
            } catch (Exception e) {
                Log.e("Twitter Img Error", "couldn't get img from url");
            }
        }
    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 50000;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("HTTP CONNECTION", "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), 8);
        StringBuilder sb = new StringBuilder();

        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        return sb.toString();
    }

    public void parseRepProfiles(String in) {
        Log.d("CongressionalActivity", "parsing Rep profiles... " + myapp.zip);

        int r = 0;
        try {
            JSONObject rootObject = new JSONObject(in);
            JSONArray jsonReps = rootObject.optJSONArray("results");

            for (Representative rep : myapp.reps) {
                rep.setPersonalInfo(jsonReps.getJSONObject(r));
                String searchBillsURL = sunlightURL1 + "/bills?sponsor_id=" + rep.bioID + sunlightURL2;
                rep.setBills(downloadUrl(searchBillsURL));

                String searchCommURL = sunlightURL1 + "/committees?member_ids=" + rep.bioID + sunlightURL2;
                rep.setCommittees(downloadUrl(searchCommURL));
                r++;
            }
        } catch (JSONException e) {
            Log.d("JSON error", e.toString());
        } catch (IOException j) {
        }
    }

    String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("election.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    String get2012Election(String county, String state) {
        String countyState2 = county.replaceAll(" County", "County") + ", " + state;
        String countyState = county + ", " + state;
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONObject votes = obj.getJSONObject(countyState2);
            double romney = votes.getDouble("romney");
            double obama = votes.getDouble("obama");
            Log.d("2012 STATS", "ROM: " + romney + " OBA: " + obama);
            String res = countyState + "%" + Double.toString(romney) + "%" + Double.toString(obama);
            Log.d("string: ", res);
            return res;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return countyState + "Not%Available";
    }

    void getVoteStats() {
        try {
            String geoJSON = downloadUrl(myapp.geourl);
            JSONObject rootObject = new JSONObject(geoJSON);
            JSONArray addarray2 = rootObject.optJSONArray("results");
            JSONObject ac = addarray2.getJSONObject(0);
            JSONArray addarray = ac.optJSONArray("address_components");
            for (int i = 0; i < addarray.length(); i++) {
                if (addarray.getJSONObject(i).getJSONArray("types").getString(0).equals("administrative_area_level_2")) {
                    myapp.county = addarray.getJSONObject(i).getString("long_name");
                }
                if (addarray.getJSONObject(i).getJSONArray("types").getString(0).equals("administrative_area_level_1")) {
                    myapp.state = addarray.getJSONObject(i).getString("short_name");
                }
            }
            get2012Election(myapp.county, myapp.state);
        } catch (IOException e) {}
        catch (JSONException i) {}
    }

}
