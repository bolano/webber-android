package com.webber.webber.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.webber.webber.mainui.fragments.CompanyFragment;
import com.webber.webber.mainui.fragments.MapFragment;
import com.webber.webber.mainui.fragments.PersonFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return new PersonFragment();
            case 1:
                return new CompanyFragment();
            case 2:
                return new MapFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}
