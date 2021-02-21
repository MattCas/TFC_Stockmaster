package com.TFCStockmaster;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.TFCStockmaster.Database.ConnectionClass;
import com.TFCStockmaster.Database.SqlAnywhereConnClass;
import com.TFCStockmaster.fragments.CategoryEditFragment;
import com.TFCStockmaster.fragments.NewEntryFragment;
import com.TFCStockmaster.fragments.StockSearchFragment;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

  BottomNavigationView bottomNavigation;
  private ImageView statusIndicator;
  private static final String TAG = "upload";
  private Button deLangButton, enLangButton, dbTestButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);
    bottomNavigation = findViewById(R.id.bottom_navigation);
    bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    openFragment(NewEntryFragment.newInstance("", ""));

    statusIndicator = findViewById(R.id.status_img_view);
    CheckConnection(findViewById(android.R.id.content).getRootView(), statusIndicator);


    // Set German language
    deLangButton = this.findViewById(R.id.de_lang_button);
    deLangButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // Must be passed in method because they're final vars
        setLocale("de");
        recreate();
      }
    });

    // Set English language
    enLangButton = this.findViewById(R.id.en_lang_button);
    enLangButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // Must be passed in method because they're final vars
        setLocale("en");
        recreate();
      }
    });

    // Set English language
    dbTestButton = this.findViewById(R.id.db_test_button);
    dbTestButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // Must be passed in method because they're final vars
        CheckConnection(view, statusIndicator);
      }
    });
  }


  // Search DB by stockID
  // TODO Return results as usable variables, rather than void method
  public void SearchDB(View view, String stockID, EditText material, EditText specs, EditText measure, EditText quantity, EditText date, EditText name,
                       EditText extra1, EditText extra2, EditText extra3, EditText extra4, EditText extra5, EditText extra6, ImageView deliverynoteview, EditText notes) {
    try {
      if (ConnectionClass.con == null) {
        new ConnectionClass().setConnection();
      }

      if (ConnectionClass.con != null) {
        String photourl = "nada";
        Statement stmt = ConnectionClass.con.createStatement();
        String sql = "select * from StockTable WHERE stockid='" + stockID + "';";
        ResultSet rs = stmt.executeQuery(sql);
        Log.e("ASK", "-------------------");
        // Set all strings to edittext obj passed in
        while (rs.next()) {
          material.setText(rs.getString("material"));
          specs.setText(rs.getString("menge"));
          measure.setText(rs.getString("einheit"));
          quantity.setText(rs.getString("quantitaet"));
          date.setText(rs.getString("lieferdatum"));
          name.setText(rs.getString("produktname"));
          extra1.setText(rs.getString("extra_spez1"));
          extra2.setText(rs.getString("extra_spez2"));
          extra3.setText(rs.getString("extra_spez3"));
          extra4.setText(rs.getString("extra_spez4"));
          extra5.setText(rs.getString("extra_spez5"));
          extra6.setText(rs.getString("extra_spez6"));
          photourl = (rs.getString("fotoid"));
          notes.setText(rs.getString("notes"));
        }
        Log.e("ASK", "------------------");
        loadImageFromLocalStore(photourl, deliverynoteview);

        Toast.makeText(getApplicationContext(), "Search Query executed successfully", Toast.LENGTH_LONG).show();
      } else {
        Toast.makeText(getApplicationContext(), "Connection to server failed!", Toast.LENGTH_LONG).show();
      }
    } catch (Exception e) {
      Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
      Log.e("ASK", e.getMessage());
    }
  }

  // Insert into DB
  public int InsertDB(View view, String stockid, String material, String spec_declared, String specs, String quantity, String deliveryDate, String name, String extra1, String extra2, String extra3, String extra4, String extra5, String extra6, String deliveryNotePhoto, String notes) {
    int insertOutcome = 0;
    try {
      if (ConnectionClass.con == null) {
        new ConnectionClass().setConnection();
      }

      if (ConnectionClass.con != null) {
        Statement stmt = ConnectionClass.con.createStatement();
        // SQL statement
        String sql = "insert INTO StockTable VALUES('" + stockid + "','" + material + "','" + spec_declared +
                "','" + specs + "','" + quantity + "','" + deliveryDate + "','" + name + "','" + extra1 + "','" + extra2 + "','" + extra3 +
                "','" + extra4 + "','" + extra5 + "','" + extra6 + "','" + deliveryNotePhoto + "', getDate(),'" + notes +"' );";
        int res = stmt.executeUpdate(sql);
        Log.e("DBCOM", sql);
        // Debug elseif
        if (res == 0) {
          Log.e("INSERT", "Inserted Failed");
          insertOutcome = res;
        } else {
          Log.e("INSERT", "Inserted Normally");
          Toast.makeText(getApplicationContext(), "Insert Query executed successfully", Toast.LENGTH_LONG).show();
          insertOutcome = res;
        }

      } else {
        Toast.makeText(getApplicationContext(), "Connection to server failed!", Toast.LENGTH_LONG).show();
        insertOutcome = 0;
      }
    } catch (Exception e) {
      Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
      Log.e("INSERT", e.getMessage());
    }
    return insertOutcome;
  }

  // Replace into DB using StockID as primary key
  public int ReplaceDB(View view, String stockid, String material, String spec_declared, String specs, String quantity, String deliveryDate, String name, String extra1, String extra2, String extra3, String extra4, String extra5, String extra6, String deliveryNotePhoto, String notes) {
    int replaceOutcome = 0;
    try {
      if (ConnectionClass.con == null) {
        new ConnectionClass().setConnection();
      }

      if (ConnectionClass.con != null) {
        Statement stmt = ConnectionClass.con.createStatement();
        // SQL statement
        String sql = "UPDATE StockTable SET stockid = '"+ stockid +"', material = '"+ material +"', menge = '"+ spec_declared +"', einheit = '"+ specs +"', quantitaet = '"+ quantity +"', " +
                "lieferdatum = '"+ deliveryDate +"', produktname = '"+ name +"', extra_spez1 = '"+ extra1 +"', extra_spez2 = '"+ extra2 +"', extra_spez3 = '"+ extra3 +"', extra_spez4 = '"+ extra4 +"', " +
                "extra_spez5 = '"+ extra5 +"', extra_spez6 = '"+ extra6 + "', notes = '"+ notes +"' WHERE stockid = '"+ stockid +"'";
        Log.e("DBCOM", sql);
        int res = stmt.executeUpdate(sql);

        // Debug elseif
        if (res == 0) {
          Log.e("INSERT", "Inserted Failed");
          replaceOutcome = res;
        } else {
          Log.e("INSERT", "Inserted Normally");
          replaceOutcome = res;
        }
        Toast.makeText(getApplicationContext(), "Insert Query executed successfully", Toast.LENGTH_LONG).show();
      } else {
        Toast.makeText(getApplicationContext(), "Connection to server failed!", Toast.LENGTH_LONG).show();
        replaceOutcome = 0;
      }
    } catch (Exception e) {
      Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
      Log.e("INSERT", e.getMessage());
      replaceOutcome = 0;
    }
    return replaceOutcome;
  }


  // Purchase DB lookup
  public void purchaseDbLookup(String orderID, List<String> results) {
    SqlAnywhereConnClass cc = new SqlAnywhereConnClass("mcas", "Gedumu49", "hsab_tcf_sbs", "192.168.2.4");
    Connection cn = cc.getConnection();
    Statement st = null;
    ResultSet rs;
    ResultSetMetaData rsmt;
    results.clear();

    try {
      st = (Statement) cn.createStatement();
      rs = st.executeQuery("SELECT menge_pe, text_is_rtf FROM \"hs\".\"vk_beleg_pos\" WHERE belegnr IN (" + orderID + ")");

      while (rs.next()) {
        StringBuilder lineResult = new StringBuilder();
        Toast toast = Toast.makeText(getApplicationContext(), "Data Found", Toast.LENGTH_SHORT);
        toast.show();
/*
        if (rs.getString("text") != null) {
          Log.e("rtf name: ", rs.getString("text"));
          lineResult.append(" ");
          lineResult.append(rs.getString("text"));
        }

        if (rs.getString("plain_text") != null) {
          Log.e("plaintext name: ", rs.getString("plain_text"));
          lineResult.append(" ");
          lineResult.append(rs.getString("plain_text"));
        }


 */

        if (rs.getString("menge_pe") != null) {
          Log.e("qnty: ", rs.getString("menge_pe"));
          // lineResult.append(" ");
          lineResult.append(rs.getString("menge_pe"));
        }

        Log.e("text is rtf: ", String.valueOf(rs.getBoolean("text_is_rtf")));
       // Log.e("column count", String.valueOf(rs.getMetaData().getColumnCount()));
        results.add(lineResult.toString());
      }

    }
    catch (SQLException ex)    {
      Log.e("Error here 1 : ", ex.getMessage());
    }
    //Log.e("arrayContents", sts.toString());
    Log.e("arrayContents", results.toString());
  }

  // Start new fragment
  public void openFragment(Fragment fragment) {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.container, fragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }

  // Bottom bar listener
  BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
          new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
              switch (item.getItemId()) {
                case R.id.navigation_eintrag:
                  openFragment(NewEntryFragment.newInstance("", ""));
                  return true;
                case R.id.navigation_lbsuchen:
                  openFragment(StockSearchFragment.newInstance("", ""));
                  return true;
                case R.id.navigation_kateghinz:
                  openFragment(CategoryEditFragment.newInstance("", ""));
                  return true;
              }
              return false;
            }
          };

  // Basic check for database connection established
  public void CheckConnection(View view, ImageView dbStatusIndicator) {

    try {
      if (ConnectionClass.con == null) {
        new ConnectionClass().setConnection();
      }

      if (ConnectionClass.con != null) {
        Statement stmt = ConnectionClass.con.createStatement();
        String sql = "select * from StockTable";
        //stmt.setQueryTimeout(3);
        ResultSet rs = stmt.executeQuery(sql);
        Log.e("ASK", "-------------------");
        while (rs.next()) {
          Log.e("ASK", rs.getString("stockid"));
          Log.e("ASK", rs.getString("material"));
          Log.e("ASK", rs.getString("menge"));
        }
        Log.e("ASK", "------------------");

        Toast.makeText(getApplicationContext(), "Query executed successfully", Toast.LENGTH_LONG).show();
        dbStatusIndicator.setImageResource(android.R.color.holo_green_dark);
      } else {
        Toast.makeText(getApplicationContext(), "Connection to server failed!", Toast.LENGTH_LONG).show();
        dbStatusIndicator.setImageResource(android.R.color.holo_red_light);
      }
    } catch (Exception e) {
      Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
      Log.e("ASK", e.getMessage());
      dbStatusIndicator.setImageResource(android.R.color.holo_red_light);
    }
  }

  @Override
  protected void onResume() {
    // TODO Auto-generated method stub
    super.onResume();
    Log.i(TAG, "onResume: " + this);
  }

  @Override
  protected void onPause() {
    // TODO Auto-generated method stub
    super.onPause();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    // TODO Auto-generated method stub
    super.onConfigurationChanged(newConfig);
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    // TODO Auto-generated method stub
    super.onSaveInstanceState(outState);
    Log.i(TAG, "onSaveInstanceState");
  }

  // Multilingual support
  public void setLocale(String lang) {
    Resources res = getResources();
    // Change locale settings in the app
    DisplayMetrics dm = res.getDisplayMetrics();
    android.content.res.Configuration conf = res.getConfiguration();
    conf.locale = new Locale(lang);
    res.updateConfiguration(conf, dm);

    setContentView(R.layout.activity_main);
  }

  private void loadImageFromLocalStore(String url, ImageView imageView){
    Bitmap bitmap = BitmapFactory.decodeFile(url);
    bitmap = rotateImage(bitmap, 90);
    imageView.setImageBitmap(bitmap);
  }

  public static Bitmap rotateImage(Bitmap img, int degree) {
    Matrix matrix = new Matrix();
    matrix.postRotate(degree);
    Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
    img.recycle();
    return rotatedImg;
  }

  public Bitmap makeQRCode(String name, String stockID, String material, String specs, String measure, String date ) {
    String qrName               = "Name-"          + name;
    String qrStock              = "StockID-"       + stockID;
    String qrMaterial           = "Material-"      + material;
    String qrSpecQuantifier     = "Menge-"         + specs;
    String qrSpec               =  measure;
    String qrDeliveryDate       = "Lieferdatum-"   + date;
    Bitmap qr = null;

    StringBuilder textToSend = new StringBuilder();
    textToSend.append(qrName+"\n"+qrStock+"\n"+qrMaterial +"\n"+qrSpecQuantifier +qrSpec +"\n"+ qrDeliveryDate );
    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
    try {
      BitMatrix bitMatrix = multiFormatWriter.encode(textToSend.toString(), BarcodeFormat.QR_CODE, 2000, 720);
      BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
      qr = barcodeEncoder.createBitmap(bitMatrix);
      qr = addQrLabel(textToSend.toString(), qr);


    } catch (WriterException e) {
      e.printStackTrace();
    }
    return qr;
  }


  public Bitmap addQrLabel(String label, Bitmap qr){
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
      Rect rectText = new Rect();
      paintText.getTextBounds(captionString, 0, captionString.length(), rectText);

      newCanvas.drawText(captionString,
              0, rectText.height(), paintText);
    }else{
      Toast.makeText(this,
              "caption empty!",
              Toast.LENGTH_LONG).show();
    }
    return newBitmap;
  }

  // Clear all fields
  public void postSubmissionCleanup(EditText etMaterial, EditText etSpecs, EditText etQuantity, EditText etDeliveryDate, EditText etStockid, EditText etMeasure, EditText etName, EditText etExtra1, EditText etExtra2, EditText etExtra3, EditText etExtra4, EditText etExtra5, EditText etExtra6, PhotoView imgview, EditText etNotes){
    etMaterial.getText().clear();
    etSpecs.getText().clear();
    etQuantity.getText().clear();
    etDeliveryDate.getText().clear();
    etStockid.getText().clear();
    etName.getText().clear();
    etNotes.getText().clear();
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
}
