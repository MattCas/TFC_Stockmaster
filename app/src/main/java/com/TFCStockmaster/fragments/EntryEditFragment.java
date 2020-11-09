package com.TFCStockmaster.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.TFCStockmaster.MainActivity;
import com.TFCStockmaster.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

public class EntryEditFragment extends Fragment {

    PhotoViewAttacher photoZoomAttacher;
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
        final EditText userEnteredStockid    = view.findViewById(R.id.edit_entry_stock_number);
        final EditText material              = view.findViewById(R.id.edit_entry_material);
        final EditText specs                 = view.findViewById(R.id.edit_entry_spec_declared);
        final EditText measure               = view.findViewById(R.id.edit_entry_specs);
        final EditText quantity              = view.findViewById(R.id.edit_entry_quantity);
        final EditText date                  = view.findViewById(R.id.edit_entry_date);
        final EditText extra1                = view.findViewById(R.id.edit_entry_extra1);
        final EditText extra2                = view.findViewById(R.id.edit_entry_extra2);
        final EditText extra3                = view.findViewById(R.id.edit_entry_extra3);
        final EditText extra4                = view.findViewById(R.id.edit_entry_extra4);
        final EditText extra5                = view.findViewById(R.id.edit_entry_extra5);
        final EditText extra6                = view.findViewById(R.id.edit_entry_extra6);
        final EditText name                  = view.findViewById(R.id.edit_entry_name);
        final PhotoView imgview              = view.findViewById(R.id.edit_img_view);
        editEntryRetrieveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast toast = Toast.makeText(getActivity(), "EditEntryButtonPressed", Toast.LENGTH_SHORT);
                //toast.show();
                // Have to setContentview before fetching text from EditText
                //EditText userStockNum = view.findViewById(R.id.edit_entry_stock_number);
                String stockID = userEnteredStockid.getText().toString();
                // Enter code to retrieve entry details here
                ((MainActivity) getActivity()).SearchDB(view, stockID,material,specs, measure, quantity, date, name, extra1, extra2, extra3, extra4, extra5, extra6, imgview);
            }
        });
        return view;
    }
}