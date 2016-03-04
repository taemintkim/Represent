package com.cs160.joleary.represent;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends FragmentActivity {
    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
    ProfilePagerAdapter mProfilePagerAdapter;
    ViewPager mViewPager;
    String[] namearg = {"default 1", "default 2", "default 3"};
    String[] partyarg = {"dem", "rep", "ind"};
    String dataarg = "Vermin Supreme (77%)\nObama (13%)\nRomney (5%)\nZIP Code: ";
    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;
    String location = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            namearg = getNameArray(extras);
            partyarg = getPartyArray(extras);
            location = extras.getString("ZIP");
            dataarg = dataarg + location;
        }

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        mProfilePagerAdapter = new ProfilePagerAdapter(
                getSupportFragmentManager(), namearg, partyarg, dataarg);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mProfilePagerAdapter);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    String[] namearg = getNameArray(extras);
                    String[] partyarg = getPartyArray(extras);
                    location = extras.getString("ZIP");
                    dataarg = dataarg + location;
                    mProfilePagerAdapter = new ProfilePagerAdapter(
                            getSupportFragmentManager(), namearg, partyarg, dataarg);
                    mViewPager = (ViewPager) findViewById(R.id.pager);
                    mViewPager.setAdapter(mProfilePagerAdapter);
                }
            }
        };

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {
                Toast.makeText(getApplicationContext(), "New Location!", Toast.LENGTH_SHORT).show();
                Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
            sendIntent.putExtra("SHAKED", true);
            startService(sendIntent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }


    String[] getNameArray(Bundle extras) {
        String[] result = {extras.getString("NAME1"), extras.getString("NAME2"), extras.getString("NAME3")};
        return result;
    }

    String[] getPartyArray(Bundle extras) {
        String[] result = {extras.getString("PARTY1"), extras.getString("PARTY2"), extras.getString("PARTY3")};
        return result;
    }

    // Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
    public class ProfilePagerAdapter extends FragmentPagerAdapter {
        private String[] names;
        private String[] parties;
        private String data;


        public ProfilePagerAdapter(FragmentManager fm, String[] _names, String[] _party, String _data) {
            super(fm);
            names = _names;
            parties = _party;
            data = _data;
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment;
            Bundle args = new Bundle();

            if (i < 3) {
                fragment = new DemoObjectFragment();
                args.putString(DemoObjectFragment.NAME, names[i]);
                args.putString(DemoObjectFragment.PARTY, parties[i]);
            } else {
                fragment = new VoteFragment();
                args.putString(VoteFragment.DATA, data);
            }

            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }
    }

    public class DemoObjectFragment extends Fragment implements View.OnClickListener {
        public static final String NAME = "Name";
        public static final String PARTY = "Party";
        String prof_name;

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            // The last two arguments ensure LayoutParams are inflated
            // properly.
            View rootView = inflater.inflate(
                    R.layout.fragment, container, false);
            Bundle args = getArguments();

            prof_name = args.getString(NAME);

            ((TextView) rootView.findViewById(R.id.name)).setText(
                    args.getString(NAME));
            RelativeLayout ll = (RelativeLayout) rootView.findViewById(R.id.fragLayout);
            changeBackground(args.getString(PARTY), ll, rootView);

            ll.setOnClickListener(this);

            return rootView;
        }

        @Override
        public void onClick(View v) {
            Log.d("PROFILECLICKS", "profile clicked");
            Intent sendIntent = new Intent(getActivity(), WatchToPhoneService.class);
            sendIntent.putExtra("SHOW_PROFILE", prof_name);
            startService(sendIntent);
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

    public class VoteFragment extends Fragment {
        public static final String DATA = "Data";
        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(
                    R.layout.votefragment, container, false);
            Bundle args = getArguments();

            String data = args.getString(DATA);

            ((TextView) rootView.findViewById(R.id.vote_data)).setText(
                    args.getString(DATA));
            return rootView;
        }
    }
}



