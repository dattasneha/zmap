package com.snehadatta.zmap;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupWebView();
        setupArrowControls();
    }

    /**
     * Initializes and configures the WebView.
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        myWebView = findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setGeolocationEnabled(true);

        myWebView.addJavascriptInterface(new WebAppInterface(this, myWebView, this), "Android");
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.setWebChromeClient(new WebChromeClient());
        myWebView.loadUrl("http://192.168.27.110:3000/");
    }

    /**
     * Initializes arrow button controls for simulating key events in WebView.
     */
    private void setupArrowControls() {
        Arrow[] arrows = new Arrow[]{
                new Arrow(findViewById(R.id.arrow_up), "ArrowUp", 38),
                new Arrow(findViewById(R.id.arrow_down), "ArrowDown", 40),
                new Arrow(findViewById(R.id.arrow_right), "ArrowRight", 39),
                new Arrow(findViewById(R.id.arrow_left), "ArrowLeft", 37),
        };

        for (Arrow arrow : arrows) {
            arrow.getIcon().setOnClickListener(v -> simulateKeyPressInWebView(arrow.getKey(), arrow.getKeyCode()));
        }
    }

    /**
     * Checks if location permission is granted, and requests it if not.
     *
     * @return true if permission is granted, false otherwise.
     */
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LocationHandler.LOCATION_REQUEST_CODE
            );
            return false;
        }
        return true;
    }

    /**
     * Handles the result of the location permission request.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LocationHandler.LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showToast("Location Permission Granted");
            } else {
                showToast("Location Permission Denied");
            }
        }
    }

    /**
     * Simulates a keyboard event inside the WebView.
     */
    private void simulateKeyPressInWebView(String key, int keyCode) {
        String script = "(function() {" +
                "var activeElement = document.activeElement;" +
                "if (activeElement) {" +
                "var event = new KeyboardEvent('keydown', {key: '" + key + "', keyCode: " + keyCode + ", which: " + keyCode + "});" +
                "activeElement.dispatchEvent(event);" +
                "}" +
                "})();";

        myWebView.evaluateJavascript(script, null);
    }

    /**
     * Shows a short toast message.
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}