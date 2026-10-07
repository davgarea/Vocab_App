package com.davgarea.charlingo;

import android.content.pm.ActivityInfo;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

// The app is locked to portrait (see AndroidManifest.xml). The writing pad's
// split view may turn sideways, so the web layer asks for that here while the
// window is open and goes back to portrait when it closes.
@CapacitorPlugin(name = "OrientationBridge")
public class OrientationBridge extends Plugin {
    @PluginMethod
    public void setMode(PluginCall call) {
        String mode = call.getString("mode", "portrait");
        final int requested = "sensor".equals(mode)
            ? ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
            : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        getActivity().runOnUiThread(() -> getActivity().setRequestedOrientation(requested));
        call.resolve();
    }
}
