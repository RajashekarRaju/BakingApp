package com.developersbreach.bakingapp;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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
        DataBindingUtil.setContentView(this, R.layout.activity_main);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Find a mNavigationController given the id of a View and its containing activity.
        mNavigationController = Navigation.findNavController(this, R.id.nav_host_fragment);
    }

    // Set up up-button for ap, triggers when user clicks back button.
    @Override
    public boolean onSupportNavigateUp() {
        return mNavigationController.navigateUp();
    }
}
