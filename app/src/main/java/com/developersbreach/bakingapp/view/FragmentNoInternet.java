package com.developersbreach.bakingapp.view;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.databinding.FragmentNoInternetBinding;
import com.developersbreach.bakingapp.network.NetworkManager;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

/**
 * This fragment is added as only destination to {@link R.navigation#nav_graph_no_network} NavGraph
 * when there is no network connection.
 * <p>
 * This class functions to call {@link MainActivity} again when connection is available.
 * This kind of network connection could have be simplified in main default navigation graph itself
 * but we let NavigationController to give all destinations and let it decide.
 * <p>
 * If connection is available this host will be completely gone and actual default graph will be
 * called by NavigationController.
 * <p>
 * A simple {@link Fragment} subclass.
 */
public class FragmentNoInternet extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment using DataBinding.
        FragmentNoInternetBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_no_internet, container, false);

        // Set click listener to refresh state and check for connection again on button.
        binding.refreshConnectionButton.setOnClickListener(v -> {
            // Check status by calling checkNetwork from class NetworkManager.
            boolean isConnected = NetworkManager.checkNetwork(Objects.requireNonNull(getContext()));
            if (isConnected) {
                // Call method triggers MainActivity.
                callMainActivity();
            } else {
                // If connection is still not available, let the user know by simple SnackBar.
                Snackbar.make(v, R.string.no_internet, Snackbar.LENGTH_SHORT).show();
            }
        });

        // Return root view DataBinding.
        return binding.getRoot();
    }

    /**
     * If is connected, then start calling MainActivity to fallback into default NavGraph to show
     * all recipe data fro internet.
     * <p>
     * Call handler and execute in new post thread call to activity.
     * Use intents to call activity we need with activity window properties.
     */
    private void callMainActivity() {
        new Handler().post(() -> {
            Intent intentToMainActivity = Objects.requireNonNull(getActivity()).getIntent();
            intentToMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_NO_ANIMATION);
            getActivity().overridePendingTransition(0, 0);
            getActivity().finish();
            getActivity().overridePendingTransition(0, 0);
            startActivity(intentToMainActivity);
        });
    }
}
