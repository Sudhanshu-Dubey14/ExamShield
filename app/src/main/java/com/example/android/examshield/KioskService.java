package com.example.android.examshield;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.concurrent.TimeUnit;

public class KioskService extends Service {

    private static final long INTERVAL = TimeUnit.SECONDS.toMillis(1); // periodic interval to check in seconds -> 2 seconds
    private static final String TAG = KioskService.class.getSimpleName();

    private Thread thread = null;
    private Context context = null;
    private boolean running = false;

    @Override
    public void onDestroy() {
        Log.i(TAG, "Stopping service 'KioskService'");
        running = false;
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Starting service 'KioskService'");
        running = true;
        context = this;

        // start a thread that periodically checks if screen is pinned
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    handleKioskMode();
                    try {
                        Thread.sleep(INTERVAL);
                    } catch (InterruptedException e) {
                        Log.i(TAG, "Thread interrupted: 'KioskService'");
                    }
                } while (running);
                stopSelf();
            }
        });
        thread.start();
        return Service.START_NOT_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void handleKioskMode() {
        // is Kiosk Mode active?
        if (PrefUtils.isKioskModeActive(context)) {
            ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            assert activityManager != null;
            if (activityManager.getLockTaskModeState() == ActivityManager.LOCK_TASK_MODE_NONE) {
                Log.i(TAG, "App is not Pinned, exiting app");
                sendBroadcast(new Intent("NotPinned"));
                Log.i(TAG, "stopping self");
                stopSelf();
            } else if (activityManager.getLockTaskModeState() == ActivityManager.LOCK_TASK_MODE_PINNED) {
                Log.i(TAG, "App is Pinned");
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}