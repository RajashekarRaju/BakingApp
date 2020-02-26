package com.developersbreach.bakingapp.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;


/**
 * This class has method to perform network connection test.
 * Performs for Android devices Q and below.
 */
public class NetworkManager {

    /**
     * @param context needs to be declared to call system services.
     * @return if false no network is available.
     */
    public static boolean checkNetwork(Context context) {

        // Queries about the state of network connectivity. It also notifies applications when
        // network connectivity changes.
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // No need to check connectivity if null
        if (connectivityManager != null) {
            // Because getActiveNetworkInfo is deprecated for Android devices below Q
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();

            } else {

                boolean hasNetwork = false;
                // Gets all available network types
                Network[] networks = connectivityManager.getAllNetworks();
                // If length is not zero we have connection types to perform capabilities.
                if (networks.length > 0) {
                    for (Network network : networks) {
                        // Representation of the capabilities of an active network.
                        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
                        // Returns false if no capabilities are known or un-available.
                        hasNetwork = capabilities != null &&
                                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
                    }
                }
                // Return boolean value, true if network available else false.
                return hasNetwork;
            }
        }
        // Return false if connectivity manager is null and no network is available.
        return false;
    }
}
