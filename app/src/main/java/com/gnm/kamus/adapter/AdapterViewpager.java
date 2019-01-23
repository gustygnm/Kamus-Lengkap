package com.gnm.kamus.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdapterViewpager extends FragmentPagerAdapter {

    List<String> listTitle = new ArrayList<>();

    List<Fragment> listFragment = new ArrayList<>();

    public AdapterViewpager(FragmentManager manager) {
        super(manager);
    }

    public void getFragment(Fragment fragment, String title) {
        listFragment.add(fragment);
        listTitle.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    @Override
    public int getCount() {
        return listFragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return listTitle.get(position);
    }
}
