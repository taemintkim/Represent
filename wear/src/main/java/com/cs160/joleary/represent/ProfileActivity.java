package com.cs160.joleary.represent;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

public class ProfileActivity extends FragmentActivity {
    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
    ProfilePagerAdapter mProfilePagerAdapter;
    ViewPager mViewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        String[] namearg = {"default 1", "default 2", "default 3"};
        String[] partyarg = {"dem", "rep", "ind"};

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    String[] namearg = getNameArray(extras);
                    String[] partyarg = getPartyArray(extras);
                    mProfilePagerAdapter = new ProfilePagerAdapter(
                            getSupportFragmentManager(), namearg, partyarg);
                    mViewPager = (ViewPager) findViewById(R.id.pager);
                    mViewPager.setAdapter(mProfilePagerAdapter);
                }
            }
        };
//        mFeedBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
//                startService(sendIntent);
//            }
//        });
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            namearg = getNameArray(extras);
            partyarg = getPartyArray(extras);
        }


        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        mProfilePagerAdapter = new ProfilePagerAdapter(
                        getSupportFragmentManager(), namearg, partyarg);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mProfilePagerAdapter);
    }

    String[] getNameArray(Bundle extras) {
        String[] result = {extras.getString("NAME1"), extras.getString("NAME2"), extras.getString("NAME3")};
        return result;
    }

    String[] getPartyArray(Bundle extras) {
        String[] result = {extras.getString("PARTY1"), extras.getString("PARTY2"), extras.getString("PARTY3")};
        return result;
    }
}

// Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
class ProfilePagerAdapter extends FragmentPagerAdapter {
    private String[] names;
    private String[] parties;

    public ProfilePagerAdapter(FragmentManager fm, String[] _names, String[] _party) {
        super(fm);
        names = _names;
        parties = _party;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new DemoObjectFragment();

        Bundle args = new Bundle();
        // Our object is just an integer :-P
        args.putString(DemoObjectFragment.NAME, names[i]);
        args.putString(DemoObjectFragment.PARTY, parties[i]);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }
}


