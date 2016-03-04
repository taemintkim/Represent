package com.cs160.joleary.represent;

import android.graphics.drawable.Drawable;

/**
 * Created by TK on 3/3/16.
 */
public class Representative {
    public String name;
    public String email = "defaultemail@doge.com";
    public String URL = "defaultURL.com";
    public int imgID;
    public String partyName;
    public int partyID;
    public int partyImg;

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
        switch (partyID) {
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
}
