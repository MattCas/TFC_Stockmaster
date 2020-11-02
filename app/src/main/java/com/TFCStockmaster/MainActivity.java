package com.TFCStockmaster;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

  BottomNavigationView bottomNavigation;
  private static final String TAG = "upload";
  private Button deLangButton, enLangButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);
    bottomNavigation = findViewById(R.id.bottom_navigation);
    bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    openFragment(NewEntryFragment.newInstance("", ""));
    //CheckConnection(findViewById(android.R.id.content).getRootView());


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
  }



  // Search DB by stockID
  // TODO Return results as usable variables, rather than void method
  public void SearchDB(View view, String stockID, EditText material, EditText specs, EditText date) {
    try {
      if (ConnectionClass.con == null) {
        new ConnectionClass().setConnection();
      }

      if (ConnectionClass.con != null) {
        Statement stmt = ConnectionClass.con.createStatement();
        String sql = "select * from StockSample WHERE StockID='" + stockID + "';";
        ResultSet rs = stmt.executeQuery(sql);
        Log.e("ASK", "-------------------");
        while (rs.next()) {
          Log.e("ASK", rs.getString("Sample"));
          date.setText(rs.getString("Sample"));
          Log.e("ASK", rs.getString("StockID"));
          specs.setText(rs.getString("StockID"));
          Log.e("ASK", rs.getString("Category"));
          material.setText(rs.getString("Category"));
        }
        Log.e("ASK", "------------------");

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
  // TODO Adjust inputs and recreate database to match
  public void InsertDB(View view, String stockid, String material, String spec_declared, String specs, String quantity, String deliveryDate, String extra1, String extra2, String extra3, String extra4, String extra5, String extra6, String deliveryNotePhoto) {
    try {
      if (ConnectionClass.con == null) {
        new ConnectionClass().setConnection();
      }

      if (ConnectionClass.con != null) {
        Statement stmt = ConnectionClass.con.createStatement();
        // SQL statement
        String sql = "insert INTO StockTable VALUES('" + stockid + "','" + material + "','" + spec_declared +
                "','" + specs + "','" + quantity + "','" + deliveryDate + "','" + extra1 + "','" + extra2 + "','" + extra3 +
                "','" + extra4 + "','" + extra5 + "','" + extra6 + "','" + deliveryNotePhoto + "', getDate());";
        int res = stmt.executeUpdate(sql);
        //Log.e("DBCOM", sql);
        // Debug elseif
        if (res == 0) {
          Log.e("INSERT", "Inserted Failed");
        } else {
          Log.e("INSERT", "Inserted Normally");
        }
        Toast.makeText(getApplicationContext(), "Insert Query executed successfully", Toast.LENGTH_LONG).show();
      } else {
        Toast.makeText(getApplicationContext(), "Connection to server failed!", Toast.LENGTH_LONG).show();
      }
    } catch (Exception e) {
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
  // TODO Delete before app delivery or use to visualise database status
  public void CheckConnection(View view) {
    try {
      if (ConnectionClass.con == null) {
        new ConnectionClass().setConnection();
      }

      if (ConnectionClass.con != null) {
        Statement stmt = ConnectionClass.con.createStatement();
        String sql = "select * from StockSample";
        ResultSet rs = stmt.executeQuery(sql);
        Log.e("ASK", "-------------------");
        while (rs.next()) {
          Log.e("ASK", rs.getString("Sample"));
          Log.e("ASK", rs.getString("StockID"));
          Log.e("ASK", rs.getString("Category"));
        }
        Log.e("ASK", "------------------");

        Toast.makeText(getApplicationContext(), "Query executed successfully", Toast.LENGTH_LONG).show();
      } else {
        Toast.makeText(getApplicationContext(), "Connection to server failed!", Toast.LENGTH_LONG).show();
      }
    } catch (Exception e) {
      Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
      Log.e("ASK", e.getMessage());
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


  public void setLocale(String lang) {
    Resources res = getResources();
    // Change locale settings in the app.
    DisplayMetrics dm = res.getDisplayMetrics();
    android.content.res.Configuration conf = res.getConfiguration();
    conf.locale = new Locale(lang);
    res.updateConfiguration(conf, dm);

    setContentView(R.layout.activity_main);
  }
}
