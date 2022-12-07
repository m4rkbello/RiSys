package com.softwaresolution.water_irrigation.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.softwaresolution.water_irrigation.Adapters.HistoryAdapter;
import com.softwaresolution.water_irrigation.Interactive.Loading;
import com.softwaresolution.water_irrigation.Pojo.HistoryPojo;
import com.softwaresolution.water_irrigation.R;

import java.util.ArrayList;

public class History extends AppCompatActivity {
    String TAG = "HistoryPojo";
    RecyclerView recyclerView;
    ArrayList<HistoryPojo> spjs = new ArrayList<>( );
    Loading loading;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference("waterIrrigationHistory");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        recyclerView = findViewById(R.id.recycler);
        loading = new Loading(History.this);


        loading.loadDialog.show();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    GenericTypeIndicator<ArrayList<HistoryPojo>> t = new GenericTypeIndicator<ArrayList<HistoryPojo>>() {};
                    ArrayList<HistoryPojo> list = dataSnapshot.getValue(t);
                    if(list != null){
                        spjs = list;
                    }
                    Log.d(TAG, new Gson().toJson(spjs));
                    HistoryAdapter adapter = new HistoryAdapter(History.this,spjs,db);
                    recyclerView.setLayoutManager(new LinearLayoutManager(History.this));
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