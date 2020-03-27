package com.example.android.examshield;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private final List blockedKeys = new ArrayList(Arrays.asList(KeyEvent.KEYCODE_VOLUME_DOWN, KeyEvent.KEYCODE_VOLUME_UP));
    TextView msgWindow;
    private Button startExamButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD); //disables lock screen
        setContentView(R.layout.activity_main);
        msgWindow = findViewById(R.id.loginmsg);
        startExamButton = findViewById(R.id.startExamButton);
        startExamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(MainActivity.this, KioskService.class));
                startLockTask();
                //TODO: check if screen is pinned or not
                Intent intent = new Intent(MainActivity.this, LockedExamActivity.class);
                startActivity(intent);
            }
        });
    }
}
