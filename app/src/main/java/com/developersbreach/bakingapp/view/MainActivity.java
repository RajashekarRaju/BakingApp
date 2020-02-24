package com.developersbreach.bakingapp.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.view.recipeList.RecipeListFragment;

import java.util.Objects;

/**
 * App build with single activity and multiple fragments. This is made easy using NavigationComponent.
 * This activity hosts NavHostFragment and NavController has control over fragments.
 */
public class MainActivity extends AppCompatActivity {

    // NavController manages app navigation within a NavHost.
    private NavController mNavigationController;
    private boolean mIsConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkNetworkConnection();

        if (!mIsConnected) {
            DataBindingUtil.setContentView(this, R.layout.activity_no_network);
            mNavigationController = Navigation.findNavController(this, R.id.nav_host_fragment_state);
        } else {
            DataBindingUtil.setContentView(this, R.layout.activity_main);
            mNavigationController = Navigation.findNavController(this, R.id.nav_host_fragment);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences preferences = Objects.requireNonNull(getPreferences(Context.MODE_PRIVATE));
        SharedPreferences.Editor mEditor = preferences.edit();
        mEditor.putBoolean(RecipeListFragment.COMPLETED_ANIMATION_PREF_NAME, false);
        mEditor.apply();
    }

    private void checkNetworkConnection() {
        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
            mIsConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
    }

    // Set up up-button for ap, triggers when user clicks back button.
    @Override
    public boolean onSupportNavigateUp() {
        return mNavigationController.navigateUp();
    }

}
