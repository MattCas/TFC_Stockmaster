package com.TFCStockmaster;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.TFCStockmaster.Database.ConnectionClass;
import com.TFCStockmaster.fragments.NewEntryFragment;
import com.TFCStockmaster.fragments.CategoryEditFragment;
import com.TFCStockmaster.fragments.StockSearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

  BottomNavigationView bottomNavigation;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    bottomNavigation = findViewById(R.id.bottom_navigation);
    bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    openFragment(NewEntryFragment.newInstance("", ""));
    CheckConnection(findViewById(android.R.id.content).getRootView());
  }

  public void CheckConnection(View view){
    try{
      if(ConnectionClass.con == null){
        new ConnectionClass().setConnection();
      }

      if(ConnectionClass.con != null){
        Statement stmt = ConnectionClass.con.createStatement();
        String sql = "select * from TablePresse1";
        ResultSet rs = stmt.executeQuery(sql);
        Log.e("ASK", "-------------------");
        while(rs.next()){
          Log.e("ASK",rs.getString("Zeit"));
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
}
