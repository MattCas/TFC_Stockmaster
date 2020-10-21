package com.TFCStockmaster.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.TFCStockmaster.MainActivity;
import com.TFCStockmaster.R;

import java.util.Calendar;

public class ManualEntryFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    DatePickerDialog picker;
    EditText eText, stockid;
    String material, specs, deliveryDate, stockidstring, quantity, photoid, extra1, extra2, extra3, extra4, extra5, extra6;

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
        final Spinner manEntryMaterial    = view.findViewById(R.id.man_entry_material);
        final EditText manEntrySpecs      = view.findViewById(R.id.man_entry_specs);
        final EditText manEntryDate       = view.findViewById(R.id.man_entry_date_text);
        stockid                           = view.findViewById(R.id.man_entry_stockid);
        // Setup material spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        manEntryMaterial.setAdapter(adapter);
        manEntryMaterial.setOnItemSelectedListener(this);
        // Submit Button listener
        manEntrySubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stockidstring = stockid.getText().toString();
                specs = manEntrySpecs.getText().toString();
                deliveryDate = manEntryDate.getText().toString();
                quantity = "qty variable";
                photoid = "photoID here";
                extra1 = "extra info here";
                extra2 = "extra info here";
                extra3 = "extra info here";
                extra4 = "extra info here";
                extra5 = "extra info here";
                extra6 = "extra info here";
                // Enter code to submit entry details here                                              ADD OTHER COLUMNS HERE -> MODIFY METHOD IN MAIN CLASS TO ACCEPT MORE ARGUMENTS
                ((MainActivity) getActivity()).InsertDB(view,stockidstring, material, quantity, specs, deliveryDate, extra1, extra2, extra3, extra4, extra5, extra6, photoid);
                //Log.e("RES", material+specs+deliveryDate);

                // Rename image to match charge ID
                // Submit image to TFC Server rack
            }
        });

        // Photo Button variables
        final Button manEntryPhotoButton = view.findViewById(R.id.man_entry_photo_button);
        // Photo Button listener
        manEntryPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                //startActivityForResult(intent, 0);
                ((MainActivity) getActivity()).takePhoto(stockid.getText().toString());
                //((MainActivity) getActivity()).sendPhoto();
                // Get image as variable
                // Rename image to match charge ID
                // Submit image to TFC Server rack
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
    // Set Material variable from spinner selection
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        Log.e("randlist", "listener was called");
        material = parent.getItemAtPosition(pos).toString();
        EditText spec = getActivity().findViewById(R.id.man_entry_specs);
        if (spec!= null){
           switch (material){
               case "Carbonflies":
                  spec.setText(R.string.carbon_specs);
                  break;
               case "Harzmenge":
               case "Harter":
                   spec.setText(R.string.reshard_specs);
                   break;
               case "Schaum":
                   spec.setText(R.string.foam_specs);
                   break;
               case "Hose":
                   spec.setText(R.string.hose_specs);
                   break;
           }
        }
        else{
            Log.e("randlist", "EditText obj is null");
        }

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}