package com.snehadatta.zmap;

import android.widget.ImageView;

/**
 * Represents an arrow key mapping for UI interaction.
 */
public class Arrow {
    private final ImageView icon;
    private final String key;
    private final String code;

    /**
     * Constructs an Arrow object.
     *
     * @param icon The ImageView representing the arrow.
     * @param key The corresponding key (e.g., "ArrowUp").
     * @param code The key code (e.g., 38 for up arrow).
     */
    public Arrow(ImageView icon, String key, String code) {
        this.icon = icon;
        this.key = key;
        this.code = code;
    }

    /**
     * Gets the ImageView associated with this arrow.
     *
     * @return The ImageView representing the arrow.
     */
    public ImageView getIcon() {
        return icon;
    }

    /**
     * Gets the key name.
     *
     * @return The key as a string (e.g., "ArrowUp").
     */
    public String getKey() {
        return key;
    }

    /**
     * Gets the key code.
     *
     * @return The key code (e.g., 38 for up arrow).
     */
    public String getCode() {
        return code;
    }
}