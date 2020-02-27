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
 * <p>
 * MainActivity has two states with two layouts chosen by different navigation graph.
 * If we have activity internet connection we inflate --> {@link R.layout#activity_main}
 * If no connection is available we inflate --> {@link R.layout#activity_main_no_internet}
 * <p>
 * {@link R.layout#activity_main} is set to be defaultNavHost and has different destination fragment,
 * this triggers NavGraph {@link R.navigation#nav_graph_default}.
 * <p>
 * {@link R.layout#activity_main_no_internet} also defaultNavHost with single fragment in destination,
 * this triggers NavGraph {@link R.navigation#nav_graph_no_network}.
 */
public class MainActivity extends AppCompatActivity {

    // NavController manages app navigation within a NavHost.
    private NavController mNavigationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Don't inflate layout until we find type of network connection available to device.
        boolean isConnected = NetworkManager.checkNetwork(this);

        // If network is not available, navigate to #activity_main_no_internet inflating with Data
        // binding. And call NavigationController to to set default NavHost as nav_host_fragment_state.
        if (!isConnected) {
            DataBindingUtil.setContentView(this, R.layout.activity_main_no_internet);
            mNavigationController = Navigation.findNavController(this, R.id.nav_host_fragment_state);
        } else {
            // If network is available, navigate to #activity_main inflating with DataBinding.
            // And call NavigationController to to set default NavHost as nav_host_fragment.
            DataBindingUtil.setContentView(this, R.layout.activity_main);
            mNavigationController = Navigation.findNavController(this, R.id.nav_host_fragment);
        }
    }

    // Set up up-button for ap, triggers when user clicks up button.
    @Override
    public boolean onSupportNavigateUp() {
        return mNavigationController.navigateUp();
    }
}
