package com.snehadatta.zmap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.util.concurrent.TimeUnit;

public class GetLocation {
    private static final String TAG = "Location";
    public static final int LOCATION_REQUEST_CODE = 1000;
    private final FusedLocationProviderClient fusedLocationProviderClient;
    private final LocationRequest locationRequest;
    private final LocationCallback locationCallback;
    private Context context;
    private Location currentLocation;

    public GetLocation(Context context) {
        this.context = context;
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, TimeUnit.SECONDS.toMillis(60))
                .setMinUpdateIntervalMillis(TimeUnit.SECONDS.toMillis(30)) // Replaces setFastestInterval()
                .setMaxUpdateDelayMillis(TimeUnit.MINUTES.toMillis(2)) // Replaces setMaxWaitTime()
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
                } else {
                    Log.d(TAG, "Location information isn't available.");
                }
            }
        };
    }
    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
        );
    }

    public void stopLocationUpdates() {
        if (fusedLocationProviderClient != null && locationCallback != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Location updates stopped.");
                            Toast.makeText(context, "Location updates stopped.", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "Failed to stop location updates.");
                            Toast.makeText(context, "Failed to stop location updates.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Log.d(TAG, "FusedLocationProviderClient or LocationCallback is null.");
        }
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }
}
