package com.snehadatta.zmap;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;


public class MainActivity extends AppCompatActivity {
    private GestureWebView webView;
    private JavaScriptHandler javaScriptHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));

        setupWebView();
        setupArrowControls();
    }

    /**
     * Initializes and configures the WebView.
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        webView = findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setGeolocationEnabled(true);

        webView.addJavascriptInterface(new WebAppInterface(this, webView, this), "Android");
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl("http://192.168.29.110:3000/");

        javaScriptHandler = new JavaScriptHandler(webView);
        webView.setJavaScriptHandler(javaScriptHandler);
    }

    /**
     * Initializes arrow button controls for simulating key events in WebView.
     */
    private void setupArrowControls() {
        Arrow[] arrows = new Arrow[]{
                new Arrow(findViewById(R.id.arrow_up), "ArrowUp", "ArrowUp"),
                new Arrow(findViewById(R.id.arrow_down), "ArrowDown", "ArrowDown"),
                new Arrow(findViewById(R.id.arrow_right), "ArrowRight", "ArrowRight"),
                new Arrow(findViewById(R.id.arrow_left), "ArrowLeft", "ArrowLeft"),
        };

        for (Arrow arrow : arrows) {
            arrow.getIcon().setOnClickListener(v -> javaScriptHandler.simulateMarkerKeyEvents(arrow.getKey(), arrow.getCode(),false,false,false));
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

    /**
     * Shows a short toast message.
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.menus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int id = item.getItemId();
        if (id == R.id.menu_select_location) {
            // Code to handle "Select the current location of the pointer"
            javaScriptHandler.simulateMarkerKeyEvents(
                    "Enter",
                    "Enter",
                    /* isCtrl= */ false,
                    /* isShift= */ false,
                    /* isAlt= */ false
            );
            Toast.makeText(this, "Location selected!", Toast.LENGTH_SHORT).show();

            return true;

        } else if (id == R.id.menu_announce_location) {
            // Code to announce current location
            javaScriptHandler.simulateMarkerKeyEvents(
                    "KeyF",
                    "KeyF",
                    /* isCtrl= */ false,
                    /* isShift= */ false,
                    /* isAlt= */ false
            );
            Toast.makeText(this, "Announcing location!", Toast.LENGTH_SHORT).show();
            return true;

        } else if (id == R.id.menu_reset_cursor) {
            // Code to reset cursor
            javaScriptHandler.simulateMarkerKeyEvents(
                    "KeyL",
                    "KeyL",
                    /* isCtrl= */ false,
                    /* isShift= */ false,
                    /* isAlt= */ false
            );
            Toast.makeText(this, "Cursor reset!", Toast.LENGTH_SHORT).show();
            return true;

//        } else if (id == R.id.menu_distance_per_keypress) {
//            Toast.makeText(this, "Distance per keypress!", Toast.LENGTH_SHORT).show();
//            return true;

        } else if (id == R.id.menu_announce_altitude) {
            javaScriptHandler.simulateMarkerKeyEvents(
                    "KeyA",
                    "KeyA",
                    /* isCtrl= */ false,
                    /* isShift= */ false,
                    /* isAlt= */ false
            );
            return true;

        } else if (id == R.id.menu_distance_we) {
            javaScriptHandler.simulateMarkerKeyEvents(
                    "KeyD",
                    "KeyD",
                    /* isCtrl= */ false,
                    /* isShift= */ false,
                    /* isAlt= */ false
            );
            return true;

        } else if (id == R.id.menu_distance_ns) {
            javaScriptHandler.simulateMarkerKeyEvents(
                    "KeyD",
                    "KeyD",
                    /* isCtrl= */ false,
                    /* isShift= */ false,
                    /* isAlt= */ false
            );
            javaScriptHandler.simulateMarkerKeyEvents(
                    "KeyD",
                    "KeyD",
                    /* isCtrl= */ false,
                    /* isShift= */ false,
                    /* isAlt= */ false
            );
            return true;

        } else if (id == R.id.menu_distance_north) {
            javaScriptHandler.simulateMarkerKeyEvents(
                    "ArrowUp",
                    "ArrowUp",/* isCtrl= */ false,
                    /* isShift= */ true,
                    /* isAlt= */ false
            );
            return true;

        } else if (id == R.id.menu_distance_south) {
            javaScriptHandler.simulateMarkerKeyEvents(
                    "ArrowDown",
                    "ArrowDown",
                    /* isCtrl= */ false,
                    /* isShift= */ true,
                    /* isAlt= */ false
            );
            return true;

        } else if (id == R.id.menu_distance_east) {
            javaScriptHandler.simulateMarkerKeyEvents(
                    "ArrowRight",
                    "ArrowRight",
                    /* isCtrl= */ false,
                    /* isShift= */ true,
                    /* isAlt= */ false
            );
            return true;

        } else if (id == R.id.menu_distance_west) {
            javaScriptHandler.simulateMarkerKeyEvents(
                    "ArrowLeft",
                    "ArrowLeft",
                    /* isCtrl= */ false,
                    /* isShift= */ true,
                    /* isAlt= */ false
            );
            return true;

        } else {
            return super.onOptionsItemSelected(item);
        }

    }

}