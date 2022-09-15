package com.softwaresolution.water_irrigation.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.softwaresolution.water_irrigation.Adapters.ScheduleAdapter;
import com.softwaresolution.water_irrigation.Interactive.Loading;
import com.softwaresolution.water_irrigation.Pojo.SchedulePojo;
import com.softwaresolution.water_irrigation.R;

import java.util.ArrayList;

public class Schedule extends AppCompatActivity {
    String TAG = "Schedule";
    RecyclerView recyclerView;
    ArrayList<SchedulePojo> spjs = new ArrayList<>( );
    Loading loading;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference("waterScheduling");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        recyclerView = findViewById(R.id.recycler);
        loading = new Loading(Schedule.this);
        ((Button) findViewById(R.id.btn_add)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Schedule.this,AddSchedule.class));
                    }
                }
        );


        loading.loadDialog.show();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    GenericTypeIndicator<ArrayList<SchedulePojo>> t = new GenericTypeIndicator<ArrayList<SchedulePojo>>() {};
                    ArrayList<SchedulePojo> list = dataSnapshot.getValue(t);
                    if(list != null){
                        spjs = list;
                    }
                    Log.d(TAG, new Gson().toJson(spjs));
                    ScheduleAdapter adapter = new ScheduleAdapter(Schedule.this,spjs,db);
                    recyclerView.setLayoutManager(new LinearLayoutManager(Schedule.this));
                    recyclerView.setAdapter(adapter);

                    loading.loadDialog.dismiss();
                }catch (Exception ex){
                    Log.e(TAG, ex.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}