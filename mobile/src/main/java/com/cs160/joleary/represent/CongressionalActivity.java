package com.cs160.joleary.represent;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
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

    public static String sunlightURL1 = "congress.api.sunlightfoundation.com/";
    public static String sunlightURL2 = "&apikey=8385878a45504ea29ded263a1d2f3f81";

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
        fetchRepData(zip);
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

    void fetchRepData(int zip) {
        //search zip
        String searchZipURL = sunlightURL1 + "/legislators/locate?zip=" + zip +sunlightURL2;
        checkConnection(searchZipURL);
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
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
//         onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            parseRepProfiles(this, result);
            // find and set bills for all representatives.
        }
    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 5000;

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

    void decodeImage() { //todo
        InputStream is = null;
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        ImageView imageView = (ImageView) findViewById(R.id.image_view);
        imageView.setImageBitmap(bitmap);
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    public String getFromJSON(String in, String object, String child) {
        try {
            JSONObject reader = new JSONObject(in);
            JSONObject sys = reader.getJSONObject(object);
            return sys.getString(child);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void parseRepProfiles(DownloadWebpageTask webtask, String in) {
        Representative firstRep = new Representative();
        Representative secondRep = new Representative();
        Representative thirdRep = new Representative();

        reps.add(0, firstRep);
        reps.add(1, secondRep);
        reps.add(2, thirdRep);
        int r = 0;
        try {
            JSONObject rootObject = new JSONObject(in);
            JSONArray jsonReps = rootObject.optJSONArray("results");

            for (Representative rep : reps) {
                rep.setPersonalInfo(jsonReps.getJSONObject(r));
                String searchZipURL = sunlightURL1 + "/bills?sponsor_id=" + rep.bioID + sunlightURL2;
                rep.setBills(webtask.doInBackground(searchZipURL));
                rep.setCommittees(this);
                r++;
            }
        } catch (JSONException e) { Log.d("JSON error", "wooops"); }
    }
}
