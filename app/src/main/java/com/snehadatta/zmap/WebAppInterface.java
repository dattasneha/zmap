package com.snehadatta.zmap;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

public class WebAppInterface {
    private final Context context;
    private final WebView myWebView;
    private final LocationHandler getLocation;
    private final MainActivity mainActivity;

    public WebAppInterface(Context context, WebView webView, MainActivity activity) {
        this.context = context;
        this.myWebView = webView;
        this.getLocation = new LocationHandler(context, webView);
        this.mainActivity = activity;
    }

    /**
     * Displays a toast message from JavaScript.
     * @param message The message to be shown.
     */
    @JavascriptInterface
    public void showToast(String message) {
        Log.d("WebView", "Received message from WebView: " + message);
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Requests the device location and sends it to JavaScript.
     * Ensures location permissions are granted before starting updates.
     */
    @JavascriptInterface
    public void getDeviceLocation() {
        if (mainActivity.checkLocationPermission()) {
            getLocation.startLocationUpdates();
        } else {
            Log.e("WebView", "Location permission not granted.");
            showToast("Location permission required!");
        }
    }
}