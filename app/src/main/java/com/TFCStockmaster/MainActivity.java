package com.TFCStockmaster;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.TFCStockmaster.Database.ConnectionClass;
import com.TFCStockmaster.fragments.CategoryEditFragment;
import com.TFCStockmaster.fragments.NewEntryFragment;
import com.TFCStockmaster.fragments.StockSearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

  BottomNavigationView bottomNavigation;
  private static final String TAG = "upload";
  private ImageView mImageView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    bottomNavigation = findViewById(R.id.bottom_navigation);
    bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    openFragment(NewEntryFragment.newInstance("", ""));
    //CheckConnection(findViewById(android.R.id.content).getRootView());
  }

  // Search DB by stockID
  // TODO Return results as usable variables, rather than void method
  public void SearchDB(View view, String stockID, EditText material, EditText specs, EditText date){
    try{
      if(ConnectionClass.con == null){
        new ConnectionClass().setConnection();
      }

      if(ConnectionClass.con != null){
        Statement stmt = ConnectionClass.con.createStatement();
        String sql = "select * from StockSample WHERE StockID='" + stockID + "';";
        ResultSet rs = stmt.executeQuery(sql);
        Log.e("ASK", "-------------------");
        while(rs.next()){
          Log.e("ASK",rs.getString("Sample"));
          date.setText(rs.getString("Sample"));
          Log.e("ASK",rs.getString("StockID"));
          specs.setText(rs.getString("StockID"));
          Log.e("ASK",rs.getString("Category"));
          material.setText(rs.getString("Category"));
        }
        Log.e("ASK", "------------------");

        Toast.makeText(getApplicationContext(), "Search Query executed successfully", Toast.LENGTH_LONG).show();
      } else {
        Toast.makeText(getApplicationContext(), "Connection to server failed!", Toast.LENGTH_LONG).show();
      }
    }
    catch(Exception e){
      Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
      Log.e("ASK", e.getMessage());
    }
  }
  // Insert into DB
  // TODO Adjust inputs and recreate database to match
  public void InsertDB(View view, String material, String specs, String date){
    try{
      if(ConnectionClass.con == null){
        new ConnectionClass().setConnection();
      }

      if(ConnectionClass.con != null){
        Statement stmt = ConnectionClass.con.createStatement();
        String sql = "insert INTO StockSample(Sample, StockID, Category) VALUES('" + date + "','" +  specs + "','" + material + "');";
        int res = stmt.executeUpdate(sql);
        // Debug elseif
        if (res == 0){
          Log.e("INSERT", "Inserted Failed");
        } else{
          Log.e("INSERT", "Inserted Normally");
        }
        Toast.makeText(getApplicationContext(), "Insert Query executed successfully", Toast.LENGTH_LONG).show();
      } else {
        Toast.makeText(getApplicationContext(), "Connection to server failed!", Toast.LENGTH_LONG).show();
      }
    }
    catch(Exception e){
      Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
      Log.e("INSERT", e.getMessage());
    }
  }

  public void openFragment(Fragment fragment) {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.container, fragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }

  BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
      new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
  // TODO Delete before app delivery
  public void CheckConnection(View view){
    try{
      if(ConnectionClass.con == null){
        new ConnectionClass().setConnection();
      }

      if(ConnectionClass.con != null){
        Statement stmt = ConnectionClass.con.createStatement();
        String sql = "select * from StockSample";
        ResultSet rs = stmt.executeQuery(sql);
        Log.e("ASK", "-------------------");
        while(rs.next()){
          Log.e("ASK",rs.getString("Sample"));
          Log.e("ASK",rs.getString("StockID"));
          Log.e("ASK",rs.getString("Category"));
        }
        Log.e("ASK", "------------------");

        Toast.makeText(getApplicationContext(), "Query executed successfully", Toast.LENGTH_LONG).show();
      } else {
        Toast.makeText(getApplicationContext(), "Connection to server failed!", Toast.LENGTH_LONG).show();
      }
    }
    catch(Exception e){
      Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
      Log.e("ASK", e.getMessage());
    }
  }



  // TODO Fix method to invoke dispatchTakePictureIntent() method
  public void takePhoto(String stockid) {
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		startActivityForResult(intent, 0);
    //dispatchTakePictureIntent(stockid);
  }


  private void sendPhoto(Bitmap bitmap) throws Exception {
    new MainActivity.UploadTask().execute(bitmap);
  }

  private class UploadTask extends AsyncTask<Bitmap, Void, Void> {

    protected Void doInBackground(Bitmap... bitmaps) {
      if (bitmaps[0] == null)
        return null;
      setProgress(0);

      Bitmap bitmap = bitmaps[0];
      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); // convert Bitmap to ByteArrayOutputStream
      InputStream in = new ByteArrayInputStream(stream.toByteArray()); // convert ByteArrayOutputStream to ByteArrayInputStream

      DefaultHttpClient httpclient = new DefaultHttpClient();
      try {
        HttpPost httppost = new HttpPost(
                "http://192.168.8.84:8003/savetofile.php"); // server

        MultipartEntity reqEntity = new MultipartEntity();
        reqEntity.addPart("myFile",
                System.currentTimeMillis() + ".jpg", in);
        httppost.setEntity(reqEntity);

        Log.i(TAG, "request " + httppost.getRequestLine());
        HttpResponse response = null;
        try {
          response = httpclient.execute(httppost);
        } catch (ClientProtocolException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        try {
          if (response != null)
            Log.i(TAG, "response " + response.getStatusLine().toString());
        } finally {

        }
      } finally {

      }

      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }

      if (stream != null) {
        try {
          stream.close();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }

      return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
      // TODO Auto-generated method stub
      super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void result) {
      // TODO Auto-generated method stub
      super.onPostExecute(result);
      Toast.makeText(MainActivity.this, "Uploaded", Toast.LENGTH_LONG).show();
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

  String mCurrentPhotoPath;

  static final int REQUEST_TAKE_PHOTO = 1;
  File photoFile = null;

  private void dispatchTakePictureIntent(String stockid) {
    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    // Ensure that there's a camera activity to handle the intent
    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
      // Create the File where the photo should go
      File photoFile = null;
      try {
        photoFile = createImageFile(stockid);
      } catch (IOException ex) {
        // Error occurred while creating the File

      }
      // Continue only if the File was successfully created
      if (photoFile != null) {
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photoFile));
        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
      }
    }
  }

  /**
   * http://developer.android.com/training/camera/photobasics.html
   */
  private File createImageFile(String stockid) throws IOException {
    // Create an image file name
    //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    String imageFileName = stockid;
    String storageDir = Environment.getExternalStorageDirectory() + "/picupload";
    File dir = new File(storageDir);
    if (!dir.exists())
      dir.mkdir();

    File image = new File(storageDir + "/" + imageFileName + ".jpg");

    // Save a file: path for use with ACTION_VIEW intents
    mCurrentPhotoPath = image.getAbsolutePath();
    Log.i(TAG, "photo path = " + mCurrentPhotoPath);
    return image;
  }

  private void setPic() {
    // Get the dimensions of the View
    int targetW = mImageView.getWidth();
    int targetH = mImageView.getHeight();

    // Get the dimensions of the bitmap
    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
    bmOptions.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
    int photoW = bmOptions.outWidth;
    int photoH = bmOptions.outHeight;

    // Determine how much to scale down the image
    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

    // Decode the image file into a Bitmap sized to fill the View
    bmOptions.inJustDecodeBounds = false;
    bmOptions.inSampleSize = scaleFactor << 1;
    bmOptions.inPurgeable = true;

    Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

    Matrix mtx = new Matrix();
    mtx.postRotate(90);
    // Rotating Bitmap
    Bitmap rotatedBMP = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mtx, true);

    if (rotatedBMP != bitmap)
      bitmap.recycle();

    mImageView.setImageBitmap(rotatedBMP);

    try {
      sendPhoto(rotatedBMP);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
