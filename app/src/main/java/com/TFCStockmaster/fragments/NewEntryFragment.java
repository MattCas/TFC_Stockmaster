package com.TFCStockmaster.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.TFCStockmaster.R;

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

/*  //@Override
  public void onCreateView(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {

    }
  }*/

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
            // Create new fragment and transaction
            Fragment newFragment = new ManualEntryFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack
            transaction.replace(((ViewGroup)(getView().getParent())).getId(), newFragment);
            transaction.addToBackStack(null);
            // Commit the transaction
            transaction.commit();
          }
        });
    return view;
  }
}
