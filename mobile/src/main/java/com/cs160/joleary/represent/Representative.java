package com.cs160.joleary.represent;

import android.graphics.drawable.Drawable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by TK on 3/3/16.
 */
public class Representative {
    public String name;
    public String email = "defaultemail@doge.com";
    public String URL = "defaultURL.com";
    public int imgID;
    public String partyName;
    public int partyID; //D = 1, R = 2, I = 3
    public int partyImg;
    private String committees = "";
    private String bills = "";
    public String termEndDate = "";
    public String twitter_id;
    public String bioID;

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
        switch (partyID) { //todo change "dem" -> "D" etc.
            case 0: partyImg = R.drawable.democrat_logo;
                partyName = "dem";
                break;
            case 1: partyImg = R.drawable.rep_logo;
                partyName = "rep";
                break;
            case 2: partyImg = R.drawable.fred_160;
                partyName = "ind";
                break;
        }
    }

    void setPersonalInfo(JSONObject res) {
        try {
            bioID = res.getString("bioguide_id");
            name = res.getString("first_name") + " " + res.getString("last_name"); //what if no first or last name?
            email = res.getString("oc_email");
            partyName = res.getString("party");
            twitter_id = res.getString("twitter_id");
            termEndDate = res.getString("term_end");
            URL = res.getString("website");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void setBills(String inJSON) {
        try {
            JSONObject reader = new JSONObject(inJSON);
            JSONObject page = reader.getJSONObject("page");
            int count = Integer.parseInt(page.getString("count"));
            for (int i = 0; i<count; i++) {
                JSONObject res = reader.getJSONObject("results");
                bills = res.getString(count);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void setCommittees(String inJSON) {

    }
}
