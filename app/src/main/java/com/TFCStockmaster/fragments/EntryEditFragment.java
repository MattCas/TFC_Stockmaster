package com.TFCStockmaster.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.TFCStockmaster.MainActivity;
import com.TFCStockmaster.PopUpClass;
import com.TFCStockmaster.R;
import com.github.chrisbanes.photoview.PhotoView;

public class EntryEditFragment extends Fragment {

    String material, specs, measure, date, stockID, quantity, photoid, extra1, extra2, extra3, extra4, extra5, extra6, name, notes;
    PopUpClass popUpClass = new PopUpClass();

    // Required empty public constructor
    public EntryEditFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_entry_edit, container, false);
        final Button editEntryRetrieveButton = view.findViewById(R.id.edit_entry_retrieve_button);
        final Button editEntryEditButton     = view.findViewById(R.id.edit_entry_send_button);
        final EditText etStockID             = view.findViewById(R.id.edit_entry_stock_number);
        final EditText etMaterial              = view.findViewById(R.id.edit_entry_material);
        final EditText etSpecs                 = view.findViewById(R.id.edit_entry_spec_declared);
        final EditText etMeasure               = view.findViewById(R.id.edit_entry_specs);
        final EditText etQuantity              = view.findViewById(R.id.edit_entry_quantity);
        final EditText etDate                  = view.findViewById(R.id.edit_entry_date);
        final EditText etExtra1                = view.findViewById(R.id.edit_entry_extra1);
        final EditText etExtra2                = view.findViewById(R.id.edit_entry_extra2);
        final EditText etExtra3                = view.findViewById(R.id.edit_entry_extra3);
        final EditText etExtra4                = view.findViewById(R.id.edit_entry_extra4);
        final EditText etExtra5                = view.findViewById(R.id.edit_entry_extra5);
        final EditText etExtra6                = view.findViewById(R.id.edit_entry_extra6);
        final EditText etName                  = view.findViewById(R.id.edit_entry_name);
        final EditText etNotes                 = view.findViewById(R.id.edit_entry_notes);
        final PhotoView imgview              = view.findViewById(R.id.edit_img_view);

        // Retrieve entry button listener
        editEntryRetrieveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stockID = etStockID.getText().toString();
                // Code to retrieve entry details
                ((MainActivity) getActivity()).SearchDB(view, stockID, etMaterial, etSpecs, etMeasure, etQuantity, etDate, etName, etExtra1, etExtra2, etExtra3, etExtra4, etExtra5, etExtra6, imgview, etNotes);
            }
        });

        // Edit entry button listener
        editEntryEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sets text from EditText to string variables
                assignSubmitFields(etMaterial, etSpecs, etDate, etMeasure, etQuantity, etName, etExtra1, etExtra2, etExtra3, etExtra4, etExtra5, etExtra6, etNotes);
                // Edit entry in DB via SQL query
                int editOutcome = ((MainActivity) getActivity()).ReplaceDB(view,stockID, material, specs,
                        measure, quantity, date, name, extra1, extra2, extra3, extra4, extra5, extra6, "getPhotoURLHereSomehow", notes);
                if (editOutcome == 1) {
                    // Show QR code in popup window
                    popUpClass.showPopupWindow(view, ((MainActivity) getActivity()).makeQRCode(name, stockID, material, specs, measure, date));
                    // Reset EditText and ImageView after submission
                    ((MainActivity) getActivity()).postSubmissionCleanup(etMaterial, etSpecs, etQuantity, etDate, etStockID, etMeasure, etName, etExtra1, etExtra2, etExtra3, etExtra4, etExtra5, etExtra6, imgview, etNotes);
                }
            }
        });
        return view;
    }
    // Sets String variable values to text in EditText vars
    public void assignSubmitFields(EditText etMaterial, EditText etSpecs, EditText etDate, EditText etMeasure, EditText etQuantity, EditText etName, EditText etExtra1, EditText etExtra2, EditText etExtra3, EditText etExtra4, EditText etExtra5, EditText etExtra6, EditText etNotes){
        // Create string entries for DB command and invoke DB insertion method
        material                  = etMaterial.getText().toString();
        specs                     = etSpecs.getText().toString();
        date                      = etDate.getText().toString();
        measure                   = etMeasure.getText().toString();
        quantity                  = etQuantity.getText().toString();
        name                      = etName.getText().toString();
        photoid                   = "photoID4Retrieval";
        extra1                    = etExtra1.getText().toString();
        extra2                    = etExtra2.getText().toString();
        extra3                    = etExtra3.getText().toString();
        extra4                    = etExtra4.getText().toString();
        extra5                    = etExtra5.getText().toString();
        extra6                    = etExtra6.getText().toString();
        notes                     = etNotes.getText().toString();
    }
}