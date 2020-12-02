package com.TFCStockmaster.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.TFCStockmaster.Database.SqlAnywhereConnClass;
import com.TFCStockmaster.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class NewEntryFragment extends Fragment {

  public NewEntryFragment() {
    // Required empty public constructor
  }

  // TODO: Rename and change types and number of parameters
  public static NewEntryFragment newInstance(String param1, String param2) {
    NewEntryFragment fragment = new NewEntryFragment();
    Bundle args = new Bundle();
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_new_entry, container, false);
        final Button manualEntryButton = view.findViewById(R.id.manual_entry_button);
        manualEntryButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            //Toast toast = Toast.makeText(getActivity(), "ManEntryButtonPressed", Toast.LENGTH_SHORT);
            //toast.show();

            // Create new fragment and transaction then open it
            Fragment newFragment = new ManualEntryFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack
            transaction.replace(((ViewGroup)(getView().getParent())).getId(), newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
          }
        });
    final Button editEntryButton = view.findViewById(R.id.entry_edit_button);
    editEntryButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // Create new fragment and transaction then open it
        Fragment newFragment = new EntryEditFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(((ViewGroup)(getView().getParent())).getId(), newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
      }
    });

    final  Button scanButton = view.findViewById(R.id.deliverynote_entry_button);
    scanButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        SqlAnywhereConnClass cc=new SqlAnywhereConnClass("mcas","Gedumu49", "hsab_tcf_sbs","192.168.2.4");
        Connection cn= cc.getConnection();
        Statement st=null;
        ResultSet rs;
        try {
          st=(Statement) cn.createStatement();
          rs=st.executeQuery("SELECT adressnr FROM \"hs\".\"std_firma\"");
          if(rs.next())
          {
            Toast toast = Toast.makeText(getActivity(), "Data Found", Toast.LENGTH_SHORT);
            toast.show();
          }
          else
          {
            Toast toast = Toast.makeText(getActivity(), "Data NOT Found", Toast.LENGTH_SHORT);
            toast.show();
          }
        } catch (SQLException ex) {
          Log.e("Error here 1 : ", ex.getMessage());
        }
      }
    });
    return view;
  }
}
