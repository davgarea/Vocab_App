package com.davgarea.charlingo;

import android.graphics.Color;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

// Lets the web layer tell the native window what colour to show behind the
// WebView. Without this, switching the app's Light/Dark/LBL setting only
// repaints the WebView's own content - the OS's window background (always
// light, from styles.xml) still briefly shows through whenever the on-screen
// keyboard opens/closes and resizes the view, which reads as a light flash
// while in Dark or LBL mode.
@CapacitorPlugin(name = "ThemeBridge")
public class ThemeBridge extends Plugin {
    @PluginMethod
    public void setBackground(PluginCall call) {
        String color = call.getString("color");
        if (color == null) {
            call.reject("color is required");
            return;
        }
        try {
            int parsed = Color.parseColor(color);
            getActivity().runOnUiThread(() ->
                getActivity().getWindow().getDecorView().setBackgroundColor(parsed)
            );
            call.resolve();
        } catch (IllegalArgumentException e) {
            call.reject("invalid color: " + color, e);
        }
    }
}
