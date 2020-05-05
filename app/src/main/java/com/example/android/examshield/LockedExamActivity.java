package com.example.android.examshield;

//import for browser start

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import android.support.v7.app.AppCompatActivity;
//import for browser end

public class LockedExamActivity extends AppCompatActivity {
    private final List blockedKeys = new ArrayList(Arrays.asList(KeyEvent.KEYCODE_VOLUME_DOWN, KeyEvent.KEYCODE_VOLUME_UP));
    private Button hiddenExitButton;
    boolean firstTime = true;
    WebView webView;
    ProgressBar progressBar;

    private final BroadcastReceiver getDestroyed = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            PrefUtils.setKioskModeActive(false, getApplicationContext());
            stopLockTask();
            Toast.makeText(getApplicationContext(), "Exiting the app!", Toast.LENGTH_SHORT).show();
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lockedexam);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD); //disables lock screen
        registerReceiver(getDestroyed, new IntentFilter("NotPinned"));

        // every time someone enters the kiosk mode, set the flag true
        PrefUtils.setKioskModeActive(true, getApplicationContext());

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setMax(100);
        progressBar.setVisibility(View.VISIBLE);
        webView = findViewById(R.id.web_view);

        if (savedInstanceState != null) {
            webView.restoreState(savedInstanceState);
        } else {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setSupportMultipleWindows(true);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webView.setBackgroundColor(Color.WHITE);

            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                    progressBar.setProgress(newProgress);
                    if (newProgress < 100 && progressBar.getVisibility() == ProgressBar.GONE) {
                        progressBar.setVisibility(ProgressBar.VISIBLE);
                    }
                    if (newProgress == 100) {
                        progressBar.setVisibility(ProgressBar.GONE);
                    } else {
                        progressBar.setVisibility(ProgressBar.VISIBLE);
                    }
                }
            });
        }

        webView.setWebViewClient(new MyWebViewClient());

        Intent intent =getIntent();
        String url=intent.getStringExtra("url");
        Map<String, String> headers = new HashMap<String, String>();     
        headers.put("X-SafeExamBrowser-RequestHash", "81aad4ab9dfd447cc479e6a4a7c9a544e2cafc7f3adeb68b2a21efad68eca4dc");
        webView.getSettings().setUserAgentString("SEB");
        webView.loadUrl(url,headers);

    }

    /*
    @Override
    public void onStop() {
        super.onStop();
        finish();
    } */

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(getDestroyed);
    }
    /*Get's called if anything (mostly dialogs) get's over the activity, then it closes all dialogs */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {
            // Close every kind of system dialog
            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
        } else if (firstTime) {
            Log.i("LockedActivity", "Activity has focus");
            ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            assert activityManager != null;
            if (activityManager.getLockTaskModeState() == ActivityManager.LOCK_TASK_MODE_PINNED) {
                Log.i("LockedActivity", "App Pinned, launching service");
                startService(new Intent(LockedExamActivity.this, KioskService.class));
            } else {
                Log.i("LockedActivity", "App not Pinned, restarting MainActivity");
                startActivity(new Intent(LockedExamActivity.this, MainActivity.class));
                finish();
            }
            firstTime = false;
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
