package com.TFCStockmaster.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.TFCStockmaster.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManualEntryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManualEntryFragment extends Fragment {

    public ManualEntryFragment() {
        // Required empty public constructor
    }

    public static ManualEntryFragment newInstance(String param1, String param2) {
        ManualEntryFragment fragment = new ManualEntryFragment();
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manual_entry, container, false);
    }
}