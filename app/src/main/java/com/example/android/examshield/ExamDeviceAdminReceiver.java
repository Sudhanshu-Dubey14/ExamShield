package com.example.android.examshield;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;

public class ExamDeviceAdminReceiver extends DeviceAdminReceiver {

    private Button startExamButton;

    @Override
    public void onDisabled(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Toast.makeText(context, "Admin Disabled. You cannot give Exam now.", Toast.LENGTH_SHORT).show();
        super.onDisabled(context, intent);
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Toast.makeText(context, "Admin Enabled. You can give Exam now.", Toast.LENGTH_SHORT).show();
        super.onEnabled(context, intent);
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Toast.makeText(context, "Admin Disable Request", Toast.LENGTH_SHORT)
                .show();
        return super.onDisableRequested(context, intent);
    }

}
