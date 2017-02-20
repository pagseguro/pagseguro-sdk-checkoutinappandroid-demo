package br.com.uol.ps.example;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        Button btNext = (Button) view.findViewById(R.id.bt_next);
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new ExampleActivityFragment()).addToBackStack(null).commit();
            }
        });

        ((ExampleActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((ExampleActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);

        return view;
    }

}
