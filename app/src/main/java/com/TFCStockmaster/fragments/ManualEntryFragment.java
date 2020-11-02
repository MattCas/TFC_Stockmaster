package com.TFCStockmaster.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
    EditText etDeliveryDate, etStockid, etSpecDeclared, etQuantity, etExtra1, etExtra2, etExtra3, etExtra4, etExtra5, etExtra6;
    String material, specs, deliveryDate, stockidstring, spec_declared, quantity, photoid, extra1, extra2, extra3, extra4, extra5, extra6;
    TextView tvExtra1, tvExtra2, tvExtra3, tvExtra4, tvExtra5, tvExtra6;
    ImageView qrImgView, imageView;
    PopUpClass popUpClass = new PopUpClass();
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

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
        etDeliveryDate =(EditText) view.findViewById(R.id.man_entry_date_text);
        etDeliveryDate.setInputType(InputType.TYPE_NULL);
        // Calendar Listener
        etDeliveryDate.setOnClickListener(new View.OnClickListener() {
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
                                etDeliveryDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
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
        imageView                         = view.findViewById(R.id.man_img_view);


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
                ((MainActivity) getActivity()).InsertDB(view,stockidstring, material, spec_declared,
                        specs, quantity, deliveryDate, extra1, extra2, extra3, extra4, extra5, extra6, photoid);
                //Log.e("RES", material+specs+deliveryDate);

                // Rename image to match charge ID
                // Submit image to TFC Server rack
                popUpClass.showPopupWindow(view, makeQRCode());
                postSubmissionCleanup();

            }
        });

        // Photo Button variables
        final Button manEntryPhotoButton = view.findViewById(R.id.man_entry_photo_button);
        // Photo Button listener
        manEntryPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
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
               case "Carbonfiber":
                   initialiseExtraFields();
                  spec.setText(R.string.carbon_specs);
                  etExtra1.setHint("");
                  etExtra2.setHint("");
                  setExtraLabels(getString(R.string.given_tolerance), getString(R.string.tested_tolerance), getString(R.string.not_required), getString(R.string.not_required), getString(R.string.not_required), getString(R.string.not_required));
                  break;
                  //need to separate?
               case "Harzmenge":
               case "Harter":
               case "Resin":
               case "Hardener":
                   initialiseExtraFields();
                   spec.setText(R.string.reshard_specs);
                   setExtraLabels(getString(R.string.given_data), getString(R.string.tested_data), getString(R.string.not_required), getString(R.string.not_required), getString(R.string.not_required), getString(R.string.not_required));
                   break;
               case "Schaum":
               case "Foam":
                   spec.setText(R.string.foam_specs);
                   setExtraLabels(getString(R.string.not_required), getString(R.string.not_required), getString(R.string.not_required), getString(R.string.not_required), getString(R.string.not_required), getString(R.string.not_required));
                   break;
               case "Hose":
                   initialiseExtraFields();
                   spec.setText(R.string.hose_specs);
                   setExtraLabels(getString(R.string.type), getString(R.string.tested_tolerance), getString(R.string.not_required), getString(R.string.not_required), getString(R.string.not_required), getString(R.string.not_required));
                   etExtra1.setHint("");
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
        etExtra1.setHint(R.string.leave_blank);
        etExtra2.setHint(R.string.leave_blank);
        etExtra3.setHint(R.string.leave_blank);
        etExtra4.setHint(R.string.leave_blank);
        etExtra5.setHint(R.string.leave_blank);
        etExtra6.setHint(R.string.leave_blank);
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

    public void postSubmissionCleanup(){
        etSpecDeclared.getText().clear();
        etQuantity.getText().clear();
        etDeliveryDate.getText().clear();
        etStockid.getText().clear();
        etExtra1.getText().clear();
        etExtra2.getText().clear();
        etExtra3.getText().clear();
        etExtra3.getText().clear();
        etExtra4.getText().clear();
        etExtra5.getText().clear();
        etExtra6.getText().clear();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(getContext(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }
}