package com.cs160.joleary.represent;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by TK on 3/1/16.
 */
// Instances of this class are fragments representing a single
// object in our collection.
public class DemoObjectFragment extends Fragment {
    public static final String NAME = "Name";
    public static final String PARTY = "Party";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
                R.layout.fragment, container, false);
        Bundle args = getArguments();

                ((TextView) rootView.findViewById(R.id.name)).setText(
                        args.getString(NAME));
        RelativeLayout ll = (RelativeLayout) rootView.findViewById(R.id.fragLayout);
        changeBackground(args.getString(PARTY), ll, rootView);
        return rootView;
    }

    void changeBackground(String party, RelativeLayout ll, View view) {
        switch (party) {
            case "dem": ll.setBackgroundResource(R.drawable.dem_logo);
                break;
            case "rep": ll.setBackgroundResource(R.drawable.rep_logo);
                break;
            case "ind": ll.setBackgroundResource(R.drawable.dogeprofilepic);
                break;
            default: ll.setBackgroundResource(R.drawable.dogeprofilepic);
                break;
        }
    }

}