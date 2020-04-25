package com.example.android.examshield;

import android.app.admin.DevicePolicyManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView msgWindow;
    private Button startExamButton;
    public ClipboardManager clipboardManager;
    private PolicyManager policyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD); //disables lock screen
        setContentView(R.layout.activity_main);
        policyManager = new PolicyManager(this);
        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("clipboard data ", "");
        clipboardManager.setPrimaryClip(clip);
        msgWindow = findViewById(R.id.loginmsg);
        startExamButton = findViewById(R.id.startExamButton);

        Toast.makeText(getApplicationContext(), "Please enable admin to give exam.", Toast.LENGTH_SHORT).show();

        startExamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (policyManager.isAdminActive()) {
                    Intent intent = new Intent(MainActivity.this, ForecastActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Please enable admin to give exam.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        activateAdmin();
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
