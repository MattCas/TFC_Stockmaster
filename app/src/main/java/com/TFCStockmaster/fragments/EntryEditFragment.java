package com.TFCStockmaster.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.TFCStockmaster.MainActivity;
import com.TFCStockmaster.R;
import com.google.android.material.textfield.TextInputEditText;

public class EntryEditFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    //EditText userStockNum;


    public EntryEditFragment() {
        // Required empty public constructor
    }

    public static EntryEditFragment newInstance(String param1, String param2) {
        EntryEditFragment fragment = new EntryEditFragment();
        return fragment;
    }

/*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_entry_edit, container, false);
        final Button editEntryRetrieveButton = view.findViewById(R.id.edit_entry_retrieve_button);
        final EditText userEnteredStockid = view.findViewById(R.id.edit_entry_stock_number);
        final EditText material = view.findViewById(R.id.edit_entry_material);
        final EditText specs = view.findViewById(R.id.edit_entry_specs);
        final EditText date = view.findViewById(R.id.edit_entry_date);
        editEntryRetrieveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getActivity(), "EditEntryButtonPressed", Toast.LENGTH_SHORT);
                toast.show();
                // Have to setContentview before fetching text from EditText
                //EditText userStockNum = view.findViewById(R.id.edit_entry_stock_number);
                String stockID = userEnteredStockid.getText().toString();
                // Enter code to retrieve entry details here
                ((MainActivity) getActivity()).SearchDB(view, stockID,material,specs,date); // Replace hardcoded ID with that in textedit box
                //Log.e("RES", stockID);
            }
        });
        return view;
    }
}