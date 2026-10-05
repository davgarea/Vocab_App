package com.davgarea.charlingo;

import android.app.UiModeManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

// Lets the web layer tell the native side which theme the app is in, and
// remembers it so the NEXT launch can start in that theme from the very first
// frame (before any web code has run):
//  - the window / WebView background, so no light flash shows behind the page
//    or when the on-screen keyboard resizes the view;
//  - the app's day/night mode (read by MainActivity before the window is
//    built, and on Android 15+ also handed to the system so it can colour the
//    launch splash to match).
@CapacitorPlugin(name = "ThemeBridge")
public class ThemeBridge extends Plugin {
    static final String PREFS = "charlingo_native_theme";
    static final String KEY_BG = "bg";
    static final String KEY_DARK = "dark";

    static SharedPreferences prefs(Context c) {
        return c.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    @PluginMethod
    public void setBackground(PluginCall call) {
        String color = call.getString("color");
        if (color == null) {
            call.reject("color is required");
            return;
        }
        try {
            final int parsed = Color.parseColor(color);
            final boolean dark = call.getBoolean("dark", false);

            SharedPreferences p = prefs(getContext());
            boolean hadDark = p.contains(KEY_DARK);
            boolean wasDark = p.getBoolean(KEY_DARK, false);
            p.edit().putInt(KEY_BG, parsed).putBoolean(KEY_DARK, dark).apply();

            getActivity().runOnUiThread(() -> {
                getActivity().getWindow().getDecorView().setBackgroundColor(parsed);
                if (getBridge() != null && getBridge().getWebView() != null) {
                    getBridge().getWebView().setBackgroundColor(parsed);
                }
            });

            // Android 15+: persist the app's own night mode with the system, so
            // the launch splash drawn before our code runs picks the matching
            // (values-night) colour next time. Only when it actually changed.
            if (Build.VERSION.SDK_INT >= 35 && (!hadDark || wasDark != dark)) {
                try {
                    UiModeManager um = (UiModeManager) getContext().getSystemService(Context.UI_MODE_SERVICE);
                    if (um != null) {
                        um.setApplicationNightMode(dark ? UiModeManager.MODE_NIGHT_YES : UiModeManager.MODE_NIGHT_NO);
                    }
                } catch (Throwable ignored) {
                    // best effort only
                }
            }
            call.resolve();
        } catch (IllegalArgumentException e) {
            call.reject("invalid color: " + color, e);
        }
    }
}
