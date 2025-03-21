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
                ( isCtrl ? "ctrlKey: 'true', " : "") +
                ( isShift ? "shiftKey: 'true', " : "") +
                ( isAlt ? "altKey: 'true', " +", " : "") +
                "});" +
                "activeElement.dispatchEvent(event);" +
                "}" +
                "})();";


        webView.post(() ->webView.evaluateJavascript(script, null));
    }

    public void simulateGeneralKeyEvents(String key, String code,Boolean isCtrl, Boolean isShift, Boolean isAlt) {
        String script = "(function() {" +
                "const event = new KeyboardEvent('keydown', {" +
                "key: '" + key + "', " +
                "code: '" + code + "'," +
                ( isCtrl ? "ctrlKey: 'true', " : "") +
                ( isShift ? "shiftKey: 'true', " : "") +
                ( isAlt ? "altKey: 'true', " +", " : "") +
                "});" +
                "document.dispatchEvent(event);" +
                "})();";

        webView.post(() -> webView.evaluateJavascript(script, null));
    }
}