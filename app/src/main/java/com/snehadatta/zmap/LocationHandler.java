package com.snehadatta.zmap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.util.concurrent.TimeUnit;

public class LocationHandler {
    private static final String TAG = "GetLocation";
    public static final int LOCATION_REQUEST_CODE = 1000;

    private final Context context;
    private final WebView webView;
    private final FusedLocationProviderClient fusedLocationProviderClient;
    private final LocationRequest locationRequest;
    private final LocationCallback locationCallback;

    private Location currentLocation;

    private JavaScriptHandler javaScriptHandler;
    public LocationHandler(Context context, WebView webView) {
        this.context = context;
        this.webView = webView;
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        javaScriptHandler = new JavaScriptHandler(webView);

        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, TimeUnit.SECONDS.toMillis(60))
                .setMinUpdateIntervalMillis(TimeUnit.SECONDS.toMillis(30)) // Minimum interval between updates
                .setMaxUpdateDelayMillis(TimeUnit.MINUTES.toMillis(2)) // Maximum delay before updates
                .build();


        // Initialize LocationCallback
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    Log.d(TAG, "New Location: Lat = " + location.getLatitude() + ", Lng = " + location.getLongitude());
                    currentLocation = location;
                    javaScriptHandler.sendLocationToWeb(location);
                    stopLocationUpdates(); // Stop updates after first successful fetch
                } else {
                    Log.d(TAG, "Location information isn't available.");
                }
            }
        };
    }

    public boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    /**
     * Starts location updates by first checking the last known location.
     * If unavailable, requests new location updates.
     */
    @SuppressLint("MissingPermission")
    public void startLocationUpdates() {
        if (!hasLocationPermission()) {
            Log.e(TAG, "Location permission not granted.");
            return;
        }

        if (!isLocationEnabled()) {
            Log.e(TAG, "Location services are turned off.");

            // Ensure UI operations run on the main thread
            new Handler(Looper.getMainLooper()).post(() -> {
                new AlertDialog.Builder(context)
                        .setTitle("Enable Location Services")
                        .setMessage("Location services are turned off. Please enable them for accurate location updates.")
                        .setPositiveButton("Go to Settings", (dialog, which) -> {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            context.startActivity(intent);
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .show();
            });

            return;
        }

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        Log.d(TAG, "Last known location retrieved.");
                        javaScriptHandler.sendLocationToWeb(location);
                    } else {
                        Log.d(TAG, "No last known location. Requesting fresh location...");
                        requestNewLocation();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to get last known location", e);
                    requestNewLocation();
                });
    }


    /**
     * Requests real-time location updates.
     */
    @SuppressLint("MissingPermission")
    private void requestNewLocation() {
        fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
        );

        Toast.makeText(context, "Fetching location, please wait!", Toast.LENGTH_LONG).show();
    }

    /**
     * Stops location updates.
     */
    public void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Location updates stopped.");
//                        Toast.makeText(context, "Location updates stopped.", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "Failed to stop location updates.");
//                        Toast.makeText(context, "Failed to stop location updates.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Sends the retrieved location to the WebView via JavaScript.
     */

    /**
     * Checks if the app has location permissions.
     */
    private boolean hasLocationPermission() {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Returns the most recently acquired location.
     */
    public Location getCurrentLocation() {
        return currentLocation;
    }
}