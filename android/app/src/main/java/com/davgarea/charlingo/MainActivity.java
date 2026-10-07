package com.davgarea.charlingo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatDelegate;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Custom plugins must be registered before super.onCreate().
        registerPlugin(ThemeBridge.class);
        registerPlugin(OrientationBridge.class);

        // Start in the theme the user last picked inside the app (saved by
        // ThemeBridge), not whatever the phone's system theme is. Must be set
        // before super.onCreate() builds the window; doing it later at runtime
        // would recreate the activity.
        SharedPreferences p = ThemeBridge.prefs(this);
        if (p.contains(ThemeBridge.KEY_DARK)) {
            AppCompatDelegate.setDefaultNightMode(
                p.getBoolean(ThemeBridge.KEY_DARK, false)
                    ? AppCompatDelegate.MODE_NIGHT_YES
                    : AppCompatDelegate.MODE_NIGHT_NO
            );
        }

        super.onCreate(savedInstanceState);
        // Must come after super.onCreate(): touching the window earlier builds
        // it with the launch theme (which has a title bar) instead of the
        // no-action-bar theme Capacitor switches to.
        EdgeToEdge.enable(this);

        // Paint the window and WebView in the saved theme colour right away,
        // so nothing light shows while the page is still loading.
        if (p.contains(ThemeBridge.KEY_BG)) {
            int bg = p.getInt(ThemeBridge.KEY_BG, 0);
            getWindow().getDecorView().setBackgroundColor(bg);
            if (getBridge() != null) {
                WebView wv = getBridge().getWebView();
                if (wv != null) wv.setBackgroundColor(bg);
            }
        }
    }
}
