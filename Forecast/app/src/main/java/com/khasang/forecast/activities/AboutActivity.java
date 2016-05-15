package com.khasang.forecast.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.khasang.forecast.R;
import com.khasang.forecast.fragments.FeaturesFragment;
import com.khasang.forecast.fragments.VersionsFragment;
import com.khasang.forecast.fragments.TeamFragment;

import java.util.ArrayList;
import java.util.List;

/*
 * AboutActivity.java     15.04.2016
 *
 * Copyright (c) 2016 Vladislav Laptev,
 * All rights reserved. Used by permission.
 */

public class AboutActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.about_tabs);
        ViewPager viewPager  = (ViewPager) findViewById(R.id.about_viewpager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.about_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(" ");
            //actionBar.setSubtitle("Khasang Open Source");
        }

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    private void setupViewPager(ViewPager viewPager) {
        TabsAdapter adapter = new TabsAdapter(getSupportFragmentManager());
        adapter.addFragment(new VersionsFragment(),
                getResources().getString(R.string.tab_name_versions));
        adapter.addFragment(new FeaturesFragment(),
                getResources().getString(R.string.tab_name_features));
        adapter.addFragment(new TeamFragment(),
                getResources().getString(R.string.tab_name_team));
        viewPager.setAdapter(adapter);
    }

    class TabsAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public TabsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }
    }
}