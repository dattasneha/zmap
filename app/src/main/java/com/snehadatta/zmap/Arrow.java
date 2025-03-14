package com.snehadatta.zmap;

import android.widget.ImageView;

class Arrow {
    ImageView icon;
    String key;
    int keyCode;

    public Arrow(ImageView icon, String key, int keyCode) {
        this.icon = icon;
        this.key = key;
        this.keyCode = keyCode;
    }
}
