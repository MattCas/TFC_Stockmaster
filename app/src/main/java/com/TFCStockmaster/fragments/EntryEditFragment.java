package com.TFCStockmaster.fragments;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.TFCStockmaster.MainActivity;
import com.TFCStockmaster.PopUpClass;
import com.TFCStockmaster.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class EntryEditFragment extends Fragment {

    String material, specs, measure, date, stockID, quantity, photoid, extra1, extra2, extra3, extra4, extra5, extra6, name;
    ImageView qrImgView;
    PopUpClass popUpClass = new PopUpClass();

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
        final PhotoView imgview              = view.findViewById(R.id.edit_img_view);


        editEntryRetrieveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stockID = etStockID.getText().toString();
                // Enter code to retrieve entry details here
                ((MainActivity) getActivity()).SearchDB(view, stockID, etMaterial, etSpecs, etMeasure, etQuantity, etDate, etName, etExtra1, etExtra2, etExtra3, etExtra4, etExtra5, etExtra6, imgview);

            }
        });

        editEntryEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Must be passed in method because they're final vars
                //assignSubmitFields(etSpecs, etDate);
                assignSubmitFields(etMaterial, etMeasure, etDate, etMeasure, etQuantity, etName, etExtra1, etExtra2, etExtra3, etExtra4, etExtra5, etExtra6);
                // Edit entry in DB
                ((MainActivity) getActivity()).ReplaceDB(view,stockID, material, specs,
                        measure, quantity, date, name, extra1, extra2, extra3, extra4, extra5, extra6, "getPhotoURLHereSomehow");

                popUpClass.showPopupWindow(view, makeQRCode());

                postSubmissionCleanup(etSpecs, etQuantity, etDate, etStockID, etMeasure, etName, etExtra1, etExtra2, etExtra3, etExtra4, etExtra5, etExtra6, imgview);
            }
        });



        return view;
    }

    public void assignSubmitFields(EditText etMaterial, EditText etSpecs, EditText etDate, EditText etMeasure, EditText etQuantity, EditText etName, EditText etExtra1, EditText etExtra2, EditText etExtra3, EditText etExtra4, EditText etExtra5, EditText etExtra6){
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
    }


    public void postSubmissionCleanup(EditText etSpecs, EditText etQuantity, EditText etDeliveryDate, EditText etStockid, EditText etMeasure, EditText etName, EditText etExtra1, EditText etExtra2, EditText etExtra3, EditText etExtra4, EditText etExtra5, EditText etExtra6, PhotoView imgview){
        etSpecs.getText().clear();
        etQuantity.getText().clear();
        etDeliveryDate.getText().clear();
        etStockid.getText().clear();
        etName.getText().clear();
        etMeasure.getText().clear();
        etExtra1.getText().clear();
        etExtra2.getText().clear();
        etExtra3.getText().clear();
        etExtra3.getText().clear();
        etExtra4.getText().clear();
        etExtra5.getText().clear();
        etExtra6.getText().clear();
        imgview.setImageResource(android.R.color.transparent);
    }


    private Bitmap makeQRCode() {
        String qrName               = "Name: "          + name;
        String qrStock              = "StockID: "       + stockID;
        String qrMaterial           = "Material: "      + material;
        String qrSpecQuantifier     = "Menge: "         + specs;
        String qrSpec               = "Einheit: "       + measure;
        String qrDeliveryDate       = "Lieferdatum: "   + date;
        Bitmap qr = null;

        StringBuilder textToSend = new StringBuilder();
        textToSend.append(qrName+" \n "+qrStock+" \n "+qrMaterial +" \n "+qrSpecQuantifier +" \n "+qrSpec +" \n "+ qrDeliveryDate );
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(textToSend.toString(), BarcodeFormat.QR_CODE, 1170, 720);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            qr = barcodeEncoder.createBitmap(bitMatrix);
            qr = addQrLabel(textToSend.toString(), qr);


        } catch (WriterException e) {
            e.printStackTrace();
        }
        return qr;
    }

    private Bitmap addQrLabel(String label, Bitmap qr){
        Bitmap bm1 = null;
        Bitmap newBitmap = null;
        bm1 = qr;
        Bitmap.Config config = bm1.getConfig();
        if(config == null){
            config = Bitmap.Config.ARGB_8888;
        }

        newBitmap = Bitmap.createBitmap(bm1.getWidth(), bm1.getHeight(), config);
        Canvas newCanvas = new Canvas(newBitmap);

        newCanvas.drawBitmap(bm1, 0, 0, null);

        String captionString = label;
        if(captionString != null){

            Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintText.setColor(Color.BLACK);
            paintText.setTextSize(39);
            paintText.setStyle(Paint.Style.FILL);
            paintText.setTextAlign(Paint.Align.LEFT);
            //paintText.setShadowLayer(10f, 10f, 10f, Color.BLACK);

            Rect rectText = new Rect();
            paintText.getTextBounds(captionString, 0, captionString.length(), rectText);

            newCanvas.drawText(captionString,
                    0, rectText.height(), paintText);
        }else{
            Toast.makeText(getContext(),
                    "caption empty!",
                    Toast.LENGTH_LONG).show();
        }
        return newBitmap;
    }
}