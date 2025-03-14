package com.snehadatta.zmap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private static final int locationRequestCode = GetLocation.LOCATION_REQUEST_CODE;
    private ImageView arrowUp;
    private ImageView arrowDown;
    private ImageView arrowLeft;
    private ImageView arrowRight;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setGeolocationEnabled(true);
        myWebView.addJavascriptInterface(new WebAppInterface(this, myWebView,this), "Android");
        myWebView.loadUrl("http://192.168.27.110:3000/");
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.setWebChromeClient(new WebChromeClient());

        Arrow[] arrows = new Arrow[]{
                new Arrow(findViewById(R.id.arrow_up), "ArrowUp", 38),
                new Arrow(findViewById(R.id.arrow_down), "ArrowDown", 40),
                new Arrow(findViewById(R.id.arrow_right), "ArrowRight", 39),
                new Arrow(findViewById(R.id.arrow_left), "ArrowLeft", 37),
        };

        for (Arrow arrow: arrows) {
            arrow.icon.setOnClickListener(v -> simulateKeyPressInWebView(myWebView, arrow.key, arrow.keyCode));
        }

    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    locationRequestCode
            );
            return false;
        }
        return true;
    }
    //Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == locationRequestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Location Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void simulateKeyPressInWebView(WebView myWebView, String key, int keyCode) {
        String script = "(function() {" +
                "var activeElement = document.activeElement;" + // Get the currently focused element
                "if (activeElement) {" +
                "var event = new KeyboardEvent('keydown', {key: '" + key + "', keyCode: " + keyCode + ", which: " + keyCode + "});" +
                "activeElement.dispatchEvent(event);" + // Simulate key press
                "}" +
                "})();";

        myWebView.evaluateJavascript(script, null); // Execute JavaScript inside WebView
    }


}

