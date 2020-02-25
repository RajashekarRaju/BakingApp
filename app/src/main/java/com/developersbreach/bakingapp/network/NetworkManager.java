package com.developersbreach.bakingapp.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

public class NetworkManager {

    public static boolean checkNetwork(Context context) {

        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();

            } else {

                boolean hasNetwork = false;
                Network[] networks = connectivityManager.getAllNetworks();
                if (networks.length > 0) {
                    for (Network network : networks) {
                        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
                        hasNetwork = capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
                    }
                }
                return hasNetwork;
            }
        }
        return false;
    }
}
