package com.TFCStockmaster.fragments;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.TFCStockmaster.MainActivity;
import com.TFCStockmaster.PopUpClass;
import com.TFCStockmaster.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Calendar;

public class ManualEntryFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    DatePickerDialog picker;
    EditText eText, etStockid, etSpecDeclared, etQuantity, etExtra1, etExtra2, etExtra3, etExtra4, etExtra5, etExtra6;
    String material, specs, deliveryDate, stockidstring, spec_declared, quantity, photoid, extra1, extra2, extra3, extra4, extra5, extra6;
    TextView tvExtra1, tvExtra2, tvExtra3, tvExtra4, tvExtra5, tvExtra6;
    ImageView qrImgView;
    PopUpClass popUpClass = new PopUpClass();

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
        // Initialise labels
        initialiseLabels(view);
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
        final Button buttonSubmit         = view.findViewById(R.id.man_entry_submit_button);
        final Spinner spinnerMaterial     = view.findViewById(R.id.man_entry_material);
        final EditText etSpecs            = view.findViewById(R.id.man_entry_specs);
        final EditText etDeliveryDate     = view.findViewById(R.id.man_entry_date_text);
        etSpecDeclared                    = view.findViewById(R.id.man_entry_spec_declared);
        etQuantity                        = view.findViewById(R.id.man_entry_quantity);
        etStockid                         = view.findViewById(R.id.man_entry_stockid);
        etExtra1                          = view.findViewById(R.id.man_entry_extra1);
        etExtra2                          = view.findViewById(R.id.man_entry_extra2);
        etExtra3                          = view.findViewById(R.id.man_entry_extra3);
        etExtra4                          = view.findViewById(R.id.man_entry_extra4);
        etExtra5                          = view.findViewById(R.id.man_entry_extra5);
        etExtra6                          = view.findViewById(R.id.man_entry_extra6);
        qrImgView                         = view.findViewById(R.id.popup_qr_view);


        // Setup material spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMaterial.setAdapter(adapter);
        spinnerMaterial.setOnItemSelectedListener(this);

        // Submit Button listener
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Must be passed in method because they're final vars
                assignSubmitFields(etSpecs, etDeliveryDate);
                // Enter code to submit entry details here
                ((MainActivity) getActivity()).InsertDB(view,stockidstring, material, spec_declared, specs, quantity, deliveryDate, extra1, extra2, extra3, extra4, extra5, extra6, photoid);
                //Log.e("RES", material+specs+deliveryDate);

                // Rename image to match charge ID
                // Submit image to TFC Server rack
                popUpClass.showPopupWindow(view, makeQRCode());
                //qrImgView.setImageBitmap(makeQRCode());
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
                ((MainActivity) getActivity()).takePhoto(etStockid.getText().toString());
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
            //RelativeLayout currentLayout = view.findViewById(R.id.man_entry_layout);
           switch (material){
               case "Carbonflies":
                   initialiseExtraFields();
                  spec.setText(R.string.carbon_specs);
                  etExtra1.setHint("Stated tolerance");
                  etExtra2.setHint("Actual tolerance");
                  setExtraLabels("Given Tolerance", "Tested Tolerance", "Not Required", "Not Required", "Not Required", "Not Required");
                  break;
                  //need to separate?
               case "Harzmenge":
               case "Harter":
                   initialiseExtraFields();
                   spec.setText(R.string.reshard_specs);
                   setExtraLabels("Given Data", "Tested Data", "Not Required", "Not Required", "Not Required", "Not Required");
                   break;
               case "Schaum":
                   spec.setText(R.string.foam_specs);
                   setExtraLabels("Not Required", "Not Required", "Not Required", "Not Required", "Not Required", "Not Required");
                   break;
               case "Hose":
                   initialiseExtraFields();
                   spec.setText(R.string.hose_specs);
                   setExtraLabels("Type", "Tested Tolerance", "Not Required", "Not Required", "Not Required", "Not Required");
                   etExtra1.setHint("Type");
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
    // TODO change text to string resource
    public void initialiseExtraFields(){
        etExtra1.setHint("Leave blank");
        etExtra2.setHint("Leave blank");
        etExtra3.setHint("Leave blank");
        etExtra4.setHint("Leave blank");
        etExtra5.setHint("Leave blank");
        etExtra6.setHint("Leave blank");
    }

    public void assignSubmitFields(EditText etSpecs, EditText etDeliveryDate){
        // Create string entries for DB command and invoke DB insertion method
        stockidstring           = etStockid.getText().toString();
        specs                   = etSpecs.getText().toString();
        deliveryDate            = etDeliveryDate.getText().toString();
        spec_declared           = etSpecDeclared.getText().toString();
        quantity                = etQuantity.getText().toString();
        photoid                 = "photoID4Retrieval";
        extra1                  = etExtra1.getText().toString();
        extra2                  = etExtra2.getText().toString();
        extra3                  = etExtra3.getText().toString();
        extra4                  = etExtra4.getText().toString();
        extra5                  = etExtra5.getText().toString();
        extra6                  = etExtra6.getText().toString();
    }

    public void setExtraLabels(String lb1, String lb2, String lb3, String lb4, String lb5, String lb6){
        tvExtra1.setText(lb1);
        tvExtra2.setText(lb2);
        tvExtra3.setText(lb3);
        tvExtra4.setText(lb4);
        tvExtra5.setText(lb5);
        tvExtra6.setText(lb6);
    }

    public void initialiseLabels(View view){
        tvExtra1 = view.findViewById(R.id.extraLabel1);
        tvExtra2 = view.findViewById(R.id.extraLabel2);
        tvExtra3 = view.findViewById(R.id.extraLabel3);
        tvExtra4 = view.findViewById(R.id.extraLabel4);
        tvExtra5 = view.findViewById(R.id.extraLabel5);
        tvExtra6 = view.findViewById(R.id.extraLabel6);
    }

    private Bitmap makeQRCode() {
        String qrStock              = "StockID : " + stockidstring;
        String qrMaterial           = "Material: " + material;
        String qrSpecQuantifier     = "Menge: " + spec_declared;
        String qrSpec               = "Einheit: " + specs;
        String qrQuantity           = "Quantitaet: " + quantity;
        String qrDeliveryDate       = "Lieferdatum: " + deliveryDate;


        Bitmap qr = null;

        StringBuilder textToSend = new StringBuilder();
        textToSend.append(qrStock+" \n "+qrMaterial +" \n "+qrSpecQuantifier +" \n "+qrSpec +" \n "+qrQuantity +" \n "+ qrDeliveryDate );
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(textToSend.toString(), BarcodeFormat.QR_CODE, 300, 300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            qr = barcodeEncoder.createBitmap(bitMatrix);

        } catch (WriterException e) {
            e.printStackTrace();
        }
        return qr;
    }
}