package com.TFCStockmaster.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import com.TFCStockmaster.MainActivity;
import com.TFCStockmaster.R;

import java.util.Calendar;

public class ManualEntryFragment extends Fragment {
    DatePickerDialog picker;
    EditText eText;
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
        View view = inflater.inflate(R.layout.fragment_manual_entry, container, false);
        eText=(EditText) view.findViewById(R.id.man_entry_date_text);
        eText.setInputType(InputType.TYPE_NULL);
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}