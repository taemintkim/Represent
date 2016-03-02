package com.cs160.joleary.represent;

import android.content.Intent;
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
        Log.d("T", "in WatchListenerService, got: " + messageEvent.getPath());
        //use the 'path' field in sendmessage to differentiate use cases
        //(here, fred vs lexy)

        if( messageEvent.getPath().equalsIgnoreCase( REP ) ) {
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Intent intent = new Intent(this, ProfileActivity.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //you need to add this flag since you're starting a new activity from a service
            parseRepData(value, intent);
            Log.d("T", "about to start watch ProfileActivity with new Representatives");
            startActivity(intent);

        } else if (messageEvent.getPath().equalsIgnoreCase( SWITCH )) {
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Intent intent = new Intent(this, MainActivity.class );
            intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
            //you need to add this flag since you're starting a new activity from a service
            intent.putExtra("CAT_NAME", "Lexy");
            Log.d("T", "about to start watch MainActivity with CAT_NAME: Lexy");
            startActivity(intent);
        } else {
            super.onMessageReceived( messageEvent );
        }

    }

    void parseRepData(String message, Intent intent) {
        String[] tokens = message.split("[%]+");

        intent.putExtra("NAME1", tokens[0]);
        intent.putExtra("PARTY1", tokens[1]);
        intent.putExtra("NAME2", tokens[0]);
        intent.putExtra("PARTY2", tokens[1]);
        intent.putExtra("NAME3", tokens[0]);
        intent.putExtra("PARTY3", tokens[1]);
    }

}