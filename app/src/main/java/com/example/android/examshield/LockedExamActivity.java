package com.example.android.examshield;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class LockedExamActivity extends AppCompatActivity {
    private final List blockedKeys = new ArrayList(Arrays.asList(KeyEvent.KEYCODE_VOLUME_DOWN, KeyEvent.KEYCODE_VOLUME_UP));
    TextView kioskWindow;
    private Button hiddenExitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD); //disables lock screen
        setContentView(R.layout.activity_lockedexam);
        kioskWindow = findViewById(R.id.kioskmode);
        // every time someone enters the kiosk mode, set the flag true
        PrefUtils.setKioskModeActive(true, getApplicationContext());
        if (PrefUtils.isKioskModeActive(getApplicationContext())) {
            kioskWindow.setText("Kiosk Mode is on");
        } else {
            kioskWindow.setText("Kiosk Mode is off");
        }

        hiddenExitButton = findViewById(R.id.hiddenExitButton);
        hiddenExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Break out!
                PrefUtils.setKioskModeActive(false, getApplicationContext());
                if (PrefUtils.isKioskModeActive(getApplicationContext())) {
                    kioskWindow.setText("Kiosk Mode is on");
                } else {
                    kioskWindow.setText("Kiosk Mode is off");
                }
                stopLockTask();
                Toast.makeText(getApplicationContext(), "You can leave the app now!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_HOME)) {
            Log.i("key", "home");
            Toast.makeText(getApplicationContext(),"Home pressed!!", Toast.LENGTH_SHORT).show();
            return true;
        }
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Toast.makeText(getApplicationContext(),"back pressed!!", Toast.LENGTH_SHORT).show();
            return true;
        }
        if ((keyCode == KeyEvent.KEYCODE_MENU)) {
            Log.i("key", "menu");
            Toast.makeText(getApplicationContext(),"menu pressed!!", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
    */
    /*Get's called if anything (mostly dialogs) get's over the activity, then it closes all dialogs */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {
            Toast.makeText(getApplicationContext(), "Can't take focus from this window!!", Toast.LENGTH_SHORT).show();
            // Close every kind of system dialog
            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
        }
    }

    @Override
    public void onBackPressed() {
        // nothing to do here
        // … really
        Toast.makeText(getApplicationContext(), "Back Pressed, no action defined", Toast.LENGTH_SHORT).show();
    }

    /*Get's called when any key is pressed, checks for keys specified by us, if yes then returs true, else returns the call to its father */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (blockedKeys.contains(event.getKeyCode())) {
            Toast.makeText(getApplicationContext(), "Volume button pressed, ignored", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

}
