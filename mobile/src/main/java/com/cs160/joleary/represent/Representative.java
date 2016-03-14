package com.cs160.joleary.represent;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by TK on 3/3/16.
 */
public class Representative {
    public String name;
    public String email;
    public String URL;
    public int imgID;
    public String partyName;
    public String longParty;
    public int partyID; //D = 1, R = 2, I = 3
    public int partyImg;
    public String committees;
    public String bills;
    public String bill_dates;
    public String termEndDate = "";
    public String twitter_id;
    public String bioID;
    public String title;
    public String twitterImageURL;
    public Bitmap profileImg;
    public String tweet;
    public Location mLastLocation;

    public Representative() {}

    public Representative(String _name, String _email, String _URL, int _partyID, int _img) {
        name = _name;
        email = _email;
        URL = _URL;
        imgID = _img;
        partyID = _partyID;
        letsparty();
    }

    public Representative(String _name, int _partyID, int _img) {
        name = _name;
        imgID = _img;
        partyID = _partyID;
        letsparty();
    }

    void letsparty() {
        switch (partyName) {
            case "D": partyImg = R.drawable.democrat_logo;
                partyID = 0;
                longParty = "Democrat";
                break;
            case "R": partyImg = R.drawable.rep_logo;
                partyID = 1;
                longParty = "Republican";

                break;
            case "I": partyImg = R.drawable.indep_logo;
                partyID = 2;
                longParty = "Independent";
                break;
        }
    }

    void setPersonalInfo(JSONObject res) {
        try {
            bioID = res.getString("bioguide_id");
            name = res.getString("first_name") + " " + res.getString("last_name"); //what if no first or last name?
            Log.d("Rep Info Update", "updating name of " + name);
            email = res.getString("oc_email");
            partyName = res.getString("party");
            twitter_id = res.getString("twitter_id");
            termEndDate = res.getString("term_end");
            URL = res.getString("website");
            title = res.getString("title");
            if (title.equals("Sen")) {
                title = "Senator";
            } else {
                title = "Representative";
            }
            letsparty();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void setBills(String inJSON) {
        bills = "";
        bill_dates = "";
        try {
            JSONObject rootObject = new JSONObject(inJSON);
            JSONArray res = rootObject.getJSONArray("results");
            int count = res.length();

            for (int i = 0; i<count; i++) {
                JSONObject billObject = res.getJSONObject(i);
                String title = billObject.getString("short_title");
                if (title.equals("null")) {
                    title = billObject.getString("official_title");
                }
                String intro_date = billObject.getString("introduced_on");
                bills = bills + intro_date +  "  |\n" + title + "\n\n";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void setCommittees(String inJSON) {
        committees = "";
        try {
            JSONObject rootObject = new JSONObject(inJSON);
            JSONArray res = rootObject.getJSONArray("results");
            int count = res.length();

            for (int i = 0; i<count; i++) {
                JSONObject commObject = res.getJSONObject(i);
                String name = commObject.getString("name");
                committees = committees + name + "\n\n";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
