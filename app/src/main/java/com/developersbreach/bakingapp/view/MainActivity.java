package com.developersbreach.bakingapp.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.network.NetworkManager;

/**
 * App build with single activity and multiple fragments. This is made easy using NavigationComponent.
 * This activity hosts NavHostFragment and NavController has control over fragments.
 */
public class MainActivity extends AppCompatActivity {

    // NavController manages app navigation within a NavHost.
    private NavController mNavigationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isConnected = NetworkManager.checkNetwork(this);

        if (!isConnected) {
            DataBindingUtil.setContentView(this, R.layout.activity_main_no_internet);
            mNavigationController = Navigation.findNavController(this, R.id.nav_host_fragment_state);
        } else {
            DataBindingUtil.setContentView(this, R.layout.activity_main);
            mNavigationController = Navigation.findNavController(this, R.id.nav_host_fragment);
        }
    }

    // Set up up-button for ap, triggers when user clicks back button.
    @Override
    public boolean onSupportNavigateUp() {
        return mNavigationController.navigateUp();
    }
}
