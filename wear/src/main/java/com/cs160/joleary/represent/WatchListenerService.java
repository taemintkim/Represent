package com.cs160.joleary.represent;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

/**
 * Created by joleary and noon on 2/19/16 at very late in the night. (early in the morning?)
 */
public class WatchListenerService extends WearableListenerService {
    // In PhoneToWatchService, we passed in a path, either "/FRED" or "/LEXY"
    // These paths serve to differentiate different phone-to-watch messages
    private static final String REP = "/RepData";
    private static final String SWITCH = "/Switch";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        //use the 'path' field in sendmessage to differentiate use cases
        //(here, fred vs lexy)

        LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(this);

        if( messageEvent.getPath().equalsIgnoreCase( REP ) ) {
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Log.d("MESSAGERECEIVED###", "in WatchListenerService, got: " + value);
            Intent intent = new Intent(this, ProfileActivity.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //you need to add this flag since you're starting a new activity from a service
            parseRepData(value, intent);
            startActivity(intent);
            broadcaster.sendBroadcast(intent);
        } else {
            super.onMessageReceived(messageEvent);
        }

    }

    void parseRepData(String message, Intent intent) {
        String[] tokens = message.split("[%]+");

        intent.putExtra("NAME1", tokens[0]);
        intent.putExtra("PARTY1", tokens[1]);
        intent.putExtra("NAME2", tokens[2]);
        intent.putExtra("PARTY2", tokens[3]);
        intent.putExtra("NAME3", tokens[4]);
        intent.putExtra("PARTY3", tokens[5]);
        intent.putExtra("ZIP", tokens[6]);
        intent.putExtra("countyState", tokens[7]);
        intent.putExtra("ROMNEY", tokens[8]);
        intent.putExtra("OBAMA", tokens[9]);
    }

}