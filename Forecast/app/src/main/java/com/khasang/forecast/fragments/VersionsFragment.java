package com.khasang.forecast.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khasang.forecast.R;

/*
 * VersionsFragment.java     15.04.2016
 *
 * Copyright (c) 2016 Vladislav Laptev,
 * All rights reserved. Used by permission.
 */

public class VersionsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_versions, container, false);

        return view;
    }

}