package com.example.android.examshield;

import android.app.NotificationManager;
import android.app.admin.DevicePolicyManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton startExamButton;
    public ClipboardManager clipboardManager;
    private PolicyManager policyManager;
    private AudioManager audiomanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //for geting do not disturb  to mute mobile
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

            startActivity(intent);
        }


        policyManager = new PolicyManager(this);
        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("clipboard data ", "");
        clipboardManager.setPrimaryClip(clip);
        audiomanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        assert audiomanager != null;
        audiomanager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);




        startExamButton = findViewById(R.id.startExamButton);
        if (!policyManager.isAdminActive()) {
            Toast.makeText(getApplicationContext(), "Please enable admin to give exam.", Toast.LENGTH_SHORT).show();
        }
        startExamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audiomanager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                activateAdmin();
                if (NetworkState.connectionAvailable(getApplicationContext())) {
                    if (policyManager.isAdminActive()) {
                        Intent intent = new Intent(MainActivity.this, ForecastActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enable admin to give exam.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Internet Inactive. Please connect to internet.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(getApplicationContext(), "Finishing everything!!", Toast.LENGTH_SHORT).show();
        finishAndRemoveTask();
    }

    public void activateAdmin() {
        if (!policyManager.isAdminActive()) {
            Intent activateDeviceAdmin = new Intent(
                    DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            activateDeviceAdmin.putExtra(
                    DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                    policyManager.getAdminComponent());
            activateDeviceAdmin
                    .putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                            "After activating admin, you will be able to give exam");
            startActivityForResult(activateDeviceAdmin,
                    PolicyManager.DPM_ACTIVATION_REQUEST_CODE);
        }
    }
}
