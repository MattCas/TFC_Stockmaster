package com.TFCStockmaster;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.TFCStockmaster.fragments.EintragFragment;
import com.TFCStockmaster.fragments.KatHinzFragment;
import com.TFCStockmaster.fragments.LBSuchenFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

  BottomNavigationView bottomNavigation;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    bottomNavigation = findViewById(R.id.bottom_navigation);
    bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    openFragment(EintragFragment.newInstance("", ""));
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
              openFragment(EintragFragment.newInstance("", ""));
              return true;
            case R.id.navigation_lbsuchen:
              openFragment(LBSuchenFragment.newInstance("", ""));
              return true;
            case R.id.navigation_kateghinz:
              openFragment(KatHinzFragment.newInstance("", ""));
              return true;
          }
          return false;
        }
      };
}
