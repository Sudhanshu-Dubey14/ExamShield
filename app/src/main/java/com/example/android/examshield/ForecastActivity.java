package com.example.android.examshield;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * ForecastActivity
 *
 * When created it fetches forecast data from the server once and renders them.
 */
public class ForecastActivity extends AppCompatActivity implements FetchDataCallbackInterface {


    // data is class member because this activity should behave like a singleton for data
    // this way data are fetched only once and reused by different class instances
    static String data = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // fetch server data only once
        if(ForecastActivity.data == null) {
            // automatically calls the renderData function
            Toast.makeText(getApplicationContext(), "Taking you to Exam. Be Ready", Toast.LENGTH_LONG).show();

            new FetchData("https://shivcharanmt.gdy.club/data.php", this).execute();
       }
        else {
            renderData();
        }
    }

    @Override
    public void fetchDataCallback(String result) {
        data = result;
        renderData();
    }

    public void renderData() {
        if(data != "") {
            Intent intent = new Intent(ForecastActivity.this, LockedExamActivity.class);
            startLockTask();
            intent.putExtra("url", data);
            startActivity(intent);
            finish();
            // do something with your data here
        }
    }

}