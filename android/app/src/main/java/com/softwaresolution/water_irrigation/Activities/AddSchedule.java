package com.softwaresolution.water_irrigation.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.softwaresolution.water_irrigation.Interactive.Loading;
import com.softwaresolution.water_irrigation.Pojo.Device;
import com.softwaresolution.water_irrigation.Pojo.SchedulePojo;
import com.softwaresolution.water_irrigation.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddSchedule extends AppCompatActivity {
    String TAG= "AddSchedule";
    boolean isUpdate = false;
    int index = -1;

    Calendar dateTimeCalendar= Calendar.getInstance();
    EditText editDate,editTime;
    Loading loading;

    ArrayList<SchedulePojo> spjs = new ArrayList<>( );
    DatabaseReference db = FirebaseDatabase.getInstance().getReference("waterScheduling");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
        editDate=(EditText) findViewById(R.id.edit_date);
        editTime=(EditText) findViewById(R.id.edit_time);
        loading = new Loading(AddSchedule.this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if(getIntent().getStringExtra("action") != null){
            isUpdate = getIntent().getStringExtra("action").equals("update") ? true : false;
        }
        index = getIntent().getIntExtra("index",-1);
        if(isUpdate){
            getSupportActionBar().setTitle("Update schedule");
            SchedulePojo sched = new Gson().fromJson(getIntent().getStringExtra("sched"),SchedulePojo.class);
            ((EditText) findViewById(R.id.edit_title)).setText(sched.getTitle());
            ((EditText) findViewById(R.id.edit_description)).setText(sched.getDescription());
            ((Switch) findViewById(R.id.s_mainG)).setChecked(sched.getMainGate() == 1 ? true : false);
            ((Switch) findViewById(R.id.s_gateA)).setChecked(sched.getGateA() == 1 ? true : false);
            ((Switch) findViewById(R.id.s_gateB)).setChecked(sched.getGateB() == 1 ? true : false);
            ((Switch) findViewById(R.id.s_gateC)).setChecked(sched.getGateC() == 1 ? true : false);
            dateTimeCalendar.setTimeInMillis(sched.getTimestamp());

            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy");
            ((EditText) findViewById(R.id.edit_date)).setText(df.format(dateTimeCalendar.getTime()));
            df = new SimpleDateFormat("hh:mm a");
            ((EditText) findViewById(R.id.edit_time)).setText(df.format(dateTimeCalendar.getTime()));
        }

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
                }catch (Exception ex){
                    Log.e(TAG, ex.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                dateTimeCalendar.set(Calendar.YEAR, year);
                dateTimeCalendar.set(Calendar.MONTH,month);
                dateTimeCalendar.set(Calendar.DAY_OF_MONTH,day);

                String myFormat="MM/dd/yy";
                SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
                editDate.setText(dateFormat.format(dateTimeCalendar.getTime()));
            }
        };

        editTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = dateTimeCalendar.get(Calendar.HOUR_OF_DAY);
                int minutes = dateTimeCalendar.get(Calendar.MINUTE);

                TimePickerDialog picker = new TimePickerDialog(
                        AddSchedule.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                String status = "AM";
                                if(sHour > 11)
                                {
                                    status = "PM";
                                }
                                int hour_of_12_hour_format;
                                if(sHour > 11){
                                    hour_of_12_hour_format = sHour - 12;
                                }
                                else {
                                    hour_of_12_hour_format = sHour;
                                }
                                editTime.setText(hour_of_12_hour_format + ":" + sMinute + " " + status);
                            }
                        }, hour, minutes, false);
                picker.show();
            }
        });

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddSchedule.this,date,
                        dateTimeCalendar.get(Calendar.YEAR),
                        dateTimeCalendar.get(Calendar.MONTH),
                        dateTimeCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public void onSubmit(View v){
        Calendar _calendar = dateTimeCalendar;
        _calendar.set(Calendar.HOUR_OF_DAY,dateTimeCalendar.get(Calendar.HOUR_OF_DAY));
        _calendar.set(Calendar.MINUTE,dateTimeCalendar.get(Calendar.MINUTE));
        Long timestamp = _calendar.getTimeInMillis();

        SchedulePojo spj = new SchedulePojo(
                ((EditText) findViewById(R.id.edit_title)).getText().toString(),
                ((EditText) findViewById(R.id.edit_description)).getText().toString(),
                timestamp,
                ((Switch) findViewById(R.id.s_mainG)).isChecked() ? 1 : 0,
                ((Switch) findViewById(R.id.s_gateA)).isChecked() ? 1 : 0,
                ((Switch) findViewById(R.id.s_gateB)).isChecked() ? 1 : 0,
                ((Switch) findViewById(R.id.s_gateC)).isChecked() ? 1 : 0
        );

        if(TextUtils.isEmpty(spj.getTitle())) {
            Toast.makeText(AddSchedule.this, "Title is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(spj.getDescription())) {
            Toast.makeText(AddSchedule.this, "Description is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(spj.getTimestamp() == 0) {
            Toast.makeText(AddSchedule.this, "Date and time is invalid", Toast.LENGTH_SHORT).show();
            return;
        }

        loading.loadDialog.show();
        String msg = "Save successfully";
        if(isUpdate && index >= 0 ){
//            UPDATE
            msg = "Updated successfully";
            spjs.set(index,spj);
        }else{
//            ADD
            spjs.add(spj);
        }

        db.setValue(spjs);

        loading.loadDialog.dismiss();
        Toast.makeText(AddSchedule.this, msg, Toast.LENGTH_LONG).show();
        onBackPressed();
    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}