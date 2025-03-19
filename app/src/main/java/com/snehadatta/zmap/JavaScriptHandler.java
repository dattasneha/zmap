package com.snehadatta.zmap;

import android.location.Location;
import android.webkit.WebView;

public class JavaScriptHandler {
    WebView webView;

    public  JavaScriptHandler(WebView webView) {
        this.webView = webView;
    }

    public void sendLocationToWeb(Location location) {
        String script = "updateDeviceLocation(" + location.getLatitude() + ", " + location.getLongitude() + ");";

        webView.post(() -> webView.evaluateJavascript(script, null));
    }

    public void simulateMarkerKeyEvents(String key, String keyCode,Boolean isCtrl, Boolean isShift, Boolean isAlt ) {
        String script = "(function() {" +
                "var activeElement = document.activeElement;" +
                "if (activeElement) {" +
                "var event = new KeyboardEvent('keydown', {" +
                "key: '" + key + "', " +
                "code: '" + keyCode + "', " +
                "altKey: '"+ isAlt +"', " +
//                "shiftKey: '"+ isShift +"', " +
                "ctrlKey: '"+ isCtrl+"', " +
                "});" +
                "activeElement.dispatchEvent(event);" +
                "}" + // Close the if block
                "})();"; // Close the IIFE properly


        webView.post(() ->webView.evaluateJavascript(script, null));
    }

    public void simulateGeneralKeyEvents(String key, String code,Boolean isCtrl, Boolean isShift, Boolean isAlt) {
        String script = "(function() {" +
                "const event = new KeyboardEvent('keydown', {" +
                "key: '" + key + "', " +
                "code: '" + code + "'," +
                "altKey: '"+ isAlt +"', " +
                "shiftKey: '"+ isShift +"', " +
                "ctrlKey: '"+ isCtrl+"', " + // Removed unnecessary space, no semicolon needed here
                "});" +  // Correctly added semicolon to end this line
                "document.dispatchEvent(event);" +
                "})();";

        webView.post(() -> webView.evaluateJavascript(script, null));
    }



}
