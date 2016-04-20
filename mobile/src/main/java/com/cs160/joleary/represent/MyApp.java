package com.cs160.joleary.represent;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by TK on 3/11/16.
 * For storing synchronized variables across activities.
 */

public class MyApp extends Application {
    public int zip;
    public ArrayList<Representative> reps = new ArrayList<>();

    public ArrayList<Representative> getReps() {
        return reps;
    }

    public Representative getRep(int index) {
        return reps.get(index);
    }

    public double lat;
    public double lon;
    public String state;
    public String county;
    public String geourl;
}
