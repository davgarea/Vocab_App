package com.davgarea.charlingo;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Draw behind the status and navigation bars so the app fills the
        // screen exactly like the launch screen does.
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
    }
}
