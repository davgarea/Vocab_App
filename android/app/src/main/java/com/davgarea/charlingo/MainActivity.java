package com.davgarea.charlingo;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Must come after super.onCreate(): touching the window earlier builds
        // it with the launch theme (which has a title bar) instead of the
        // no-action-bar theme Capacitor switches to.
        EdgeToEdge.enable(this);
    }
}
