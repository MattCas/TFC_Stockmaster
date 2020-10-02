package com.TFCStockmaster.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.TFCStockmaster.MainActivity;
import com.TFCStockmaster.R;

import java.util.Calendar;

public class ManualEntryFragment extends Fragment {
    DatePickerDialog picker;
    EditText eText, stockid;
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
        // Calendar related variables
        eText=(EditText) view.findViewById(R.id.man_entry_date_text);
        eText.setInputType(InputType.TYPE_NULL);
        // Calendar Listener
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

        // Submit Button variables
        final Button manEntrySubmitButton = view.findViewById(R.id.man_entry_submit_button);
        final EditText manEntryMaterial   = view.findViewById(R.id.man_entry_material);
        final EditText manEntrySpecs      = view.findViewById(R.id.man_entry_specs);
        final EditText manEntryDate       = view.findViewById(R.id.man_entry_date_text);
        stockid                           = view.findViewById(R.id.man_entry_stockid);
        // Submit Button listener
        manEntrySubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast toast = Toast.makeText(getActivity(), "manEntrySubmitButton Pressed", Toast.LENGTH_SHORT);
                //toast.show();
                // Have to setContentview before fetching text from EditText

                String material = manEntryMaterial.getText().toString();
                String specs = manEntrySpecs.getText().toString();
                String deliveryDate = manEntryDate.getText().toString();
                // Enter code to submit entry details here
                ((MainActivity) getActivity()).InsertDB(view, material, specs, deliveryDate);
                //Log.e("RES", material+specs+deliveryDate);

                // Rename image to match charge ID
                // Submit image to TFC Server rack
            }
        });

        // Submit Button variables
        final Button manEntryPhotoButton = view.findViewById(R.id.man_entry_photo_button);
        // Submit Button listener
        manEntryPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast toast = Toast.makeText(getActivity(), "manEntrySubmitButton Pressed", Toast.LENGTH_SHORT);
                //toast.show();
                //Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                //startActivityForResult(intent, 0);
                ((MainActivity) getActivity()).takePhoto(stockid.getText().toString());
                // Get image as variable
                // Rename image to match charge ID
                // Submit image to TFC Server rack

            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}