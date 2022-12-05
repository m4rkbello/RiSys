package com.softwaresolution.water_irrigation.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.softwaresolution.water_irrigation.Pojo.Device;
import com.softwaresolution.water_irrigation.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Control extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    String TAG = "Control";
    Switch mainG,gateA,gateB,gateC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        init();
    }

    private void init() {
        mainG = (Switch) findViewById(R.id.s_mainG);
        gateA = (Switch) findViewById(R.id.s_gateA);
        gateB = (Switch) findViewById(R.id.s_gateB);
        gateC = (Switch) findViewById(R.id.s_gateC);

        mainG.setOnCheckedChangeListener(this);
        gateA.setOnCheckedChangeListener(this);
        gateB.setOnCheckedChangeListener(this);
        gateC.setOnCheckedChangeListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("waterIrrigation");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    Device d = dataSnapshot.getValue(Device.class);
                    d.setFlmL(Float.parseFloat(String.valueOf((d.getFlmL()*33.33))));
                    d.setFlL(d.getFlmL()/1000);
                    Log.d(TAG, new Gson().toJson(d));
                    mainG.setChecked(d.getMainGate() == 1);
                    gateA.setChecked(d.getGateA() == 1);
                    gateB.setChecked(d.getGateB() == 1);
                    gateC.setChecked(d.getGateC() == 1);
                    int value = d.getWaterLevel();
                    if (value > 700  ) {
                        d.setWaterStatus("High");
                    }
                    else if ((value > 301  ) && (value < 699)) {
                        d.setWaterStatus("Normal");
                    }
                    else{
                        d.setWaterStatus("Low");
                    }

                    ((TextView) findViewById(R.id.txt_waterLvl)).setText(d.getWaterLevel().toString());
                    ((TextView) findViewById(R.id.txt_waterLvlStatus)).setText(d.getWaterStatus() );
                    ((TextView) findViewById(R.id.txt_Fl)).setText(d.getFlRate()+" L/min");
                    ((TextView) findViewById(R.id.txt_flOutPut)).setText(d.getFlmL()+" ml / "+d.getFlL()+" L");
                }catch (Exception ex){
                    Log.e(TAG, ex.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MessageError",databaseError.getDetails());
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView == mainG){
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("waterIrrigation/mainGate");
            db.setValue(isChecked ?  1 : 0);
        }
        if(buttonView == gateA){
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("waterIrrigation/gateA");
            db.setValue(isChecked ?  1 : 0);
        }
        if(buttonView == gateB){
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("waterIrrigation/gateB");
            db.setValue(isChecked ?  1 : 0);
        }
        if(buttonView == gateC){
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("waterIrrigation/gateC");
            db.setValue(isChecked ?  1 : 0);
        }
    }
}