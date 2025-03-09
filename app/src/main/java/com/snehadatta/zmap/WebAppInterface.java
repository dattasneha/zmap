package com.snehadatta.zmap;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class WebAppInterface {
    Context mContext;

    /** Instantiate the interface and set the context. */
    WebAppInterface(Context c) {
        this.mContext = c;
    }

    /** Show a toast from the web page. */
    @JavascriptInterface
    public void showToast(String toast) {
        Log.e("WebView", "Received data from web: " + toast);
        new Handler(Looper.getMainLooper()).post(() ->
                Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
        );
    }
}
