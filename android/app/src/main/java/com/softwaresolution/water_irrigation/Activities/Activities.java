package com.softwaresolution.water_irrigation.Activities;

import com.softwaresolution.water_irrigation.R;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Activities extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}