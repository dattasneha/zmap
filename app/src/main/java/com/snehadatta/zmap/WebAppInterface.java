package com.snehadatta.zmap;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

public class WebAppInterface {
    private Context context;
    private WebView myWebView;
    private GetLocation getLocation;
    private final MainActivity mainActivity;
    WebAppInterface(Context c,WebView webView,MainActivity activity) {
        this.context = c;
        this.myWebView = webView;
        this.getLocation = new GetLocation(c);
        this.mainActivity = activity;
    }

    /** Show a toast from the web page. */
    @JavascriptInterface
    public void showToast(String toast) {
        Log.e("WebView", "Received data from web: " + toast);
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
    }

    /** Call method from web to fetch current location of device and send data to WebView. */
    @JavascriptInterface
    public void getDeviceLocation() {
        if (mainActivity.checkLocationPermission()) {
            getLocation.startLocationUpdates();
        }
        Location location = getLocation.getCurrentLocation();

        if(location != null) {
            Log.e("debug", location.toString());
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            //Send the location data to WebView
            String script = "getLocationData("+ location + ")";
            myWebView.post(()->myWebView.evaluateJavascript(script, null));
        }
    }

}
