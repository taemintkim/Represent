package com.cs160.joleary.represent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;


public class ProfileActivity extends FragmentActivity {

    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
    ProfilePagerAdapter mProfilePagerAdapter;
    ViewPager mViewPager;
    String[] namearg = {"", "", ""};
    String[] partyarg = {"D", "R", "I"};
    String[] dataarg;
    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;
    String location = "";
    Context context;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = getApplicationContext();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        Log.d("MESSAGE_RECEIVED", "in Profile Activity");
        if (extras != null) {
            namearg = getNameArray(extras);
            partyarg = getPartyArray(extras);
            location = extras.getString("countyState");
            String rom = extras.getString("ROMNEY");
            String oba = extras.getString("OBAMA");
            String[] dataarg0 = { oba, rom, location};
            dataarg = dataarg0;
        }

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        mProfilePagerAdapter = new ProfilePagerAdapter(
                getSupportFragmentManager(), namearg, partyarg, dataarg);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mProfilePagerAdapter);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        // When a new location is picked from mobile, update this activity with
        // the new info.
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    String[] namearg = getNameArray(extras);
                    String[] partyarg = getPartyArray(extras);
                    location = extras.getString("countyState");
                    String rom = extras.getString("ROMNEY");
                    String oba = extras.getString("OBAMA");
                    String[] dataarg0 = { oba, rom, location};
                    dataarg = dataarg0;
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
        private String[] data;


        public ProfilePagerAdapter(FragmentManager fm, String[] _names, String[] _party, String[] _data) {
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
                args.putStringArray(VoteFragment.DATA, data);
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
            Log.d("PROFILECLICKS", "profile clicked: " + prof_name);
            Intent sendIntent = new Intent(getActivity(), WatchToPhoneService.class);
            sendIntent.putExtra("SHOW_PROFILE", prof_name);
            startService(sendIntent);
        }

        void changeBackground(String party, RelativeLayout ll, View view) {
            switch (party) {
                case "D": ll.setBackgroundResource(R.drawable.dem_logo);
                    break;
                case "R": ll.setBackgroundResource(R.drawable.rep_logo);
                    break;
                case "I": ll.setBackgroundResource(R.drawable.indep_logo);
                    break;
                default: ll.setBackgroundResource(R.drawable.flag);
                    break;
            }
        }
    }

    public class VoteFragment extends Fragment {
        private RelativeLayout mainLayout;
        public static final String DATA = "Data";
        private PieChart mChart;
        private float[] yData = new float[2];
        private String[] xData = { "Obama", "Romney" };
        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(
                    R.layout.votefragment, container, false);
            Bundle args = getArguments();
            String[] data = args.getStringArray(DATA);
            yData[0] = Float.parseFloat(data[0]);
            yData[1] = Float.parseFloat(data[1]);

//            ((TextView) rootView.findViewById(R.id.vote_data)).setText(
//                    args.getString(DATA));
//            rootView.setBackgroundResource(R.drawable.flag);

            mChart = (PieChart) rootView.findViewById(R.id.chart1);;
            TextView title = (TextView) rootView.findViewById(R.id.votetitle);
            title.setText("2012 Presidential Election Votes at\n" + location);
            // add pie chart to main layout

            // configure pie chart
            mChart.setUsePercentValues(true);

            // enable hole and configure
            mChart.setDrawHoleEnabled(true);
//            mChart.setHoleColor(true);
            mChart.setHoleRadius(7);
            mChart.setTransparentCircleRadius(10);

            // enable rotation of the chart by touch
            mChart.setRotationAngle(0);
            mChart.setRotationEnabled(true);

            // add data
            addData();

            return rootView;
        }

        private void addData() {
            ArrayList<Entry> yVals1 = new ArrayList<Entry>();

            for (int i = 0; i < yData.length; i++)
                yVals1.add(new Entry(yData[i], i));

            ArrayList<String> xVals = new ArrayList<String>();

            for (int i = 0; i < xData.length; i++)
                xVals.add(xData[i]);

            // create pie data set
            PieDataSet dataSet = new PieDataSet(yVals1, "");
            dataSet.setSliceSpace(3);
            dataSet.setSelectionShift(5);

            // add many colors
            ArrayList<Integer> colors = new ArrayList<Integer>();

            colors.add(getResources().getColor(android.R.color.holo_blue_light));
            colors.add(getResources().getColor(android.R.color.holo_red_light));
            dataSet.setColors(colors);

            // instantiate pie data object now
            PieData data = new PieData(xVals, dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.WHITE);

            mChart.setData(data);
            mChart.getLegend().setEnabled(false);
            mChart.setDescription("");

            // undo all highlights
            mChart.highlightValues(null);

            // update pie chart
            mChart.invalidate();
        }
    }
}



