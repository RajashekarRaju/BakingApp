package com.developersbreach.bakingapp.view;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.network.NetworkManager;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNoInternet extends Fragment {

    public FragmentNoInternet() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_no_internet, container, false);

        Button networkRefreshButton = view.findViewById(R.id.refresh_connection_button);

        networkRefreshButton.setOnClickListener(v -> {
            boolean isConnected = NetworkManager.checkNetwork(Objects.requireNonNull(getContext()));

            if (isConnected) {

                new Handler().post(() -> {
                    Intent intent = Objects.requireNonNull(getActivity()).getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    getActivity().overridePendingTransition(0, 0);
                    getActivity().finish();
                    getActivity().overridePendingTransition(0, 0);
                    startActivity(intent);
                });

            } else {
                Snackbar.make(v, "No Internet", Snackbar.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
