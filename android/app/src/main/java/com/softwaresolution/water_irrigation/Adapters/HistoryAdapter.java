package com.softwaresolution.water_irrigation.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.softwaresolution.water_irrigation.Interactive.Loading;
import com.softwaresolution.water_irrigation.Pojo.HistoryPojo;
import com.softwaresolution.water_irrigation.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String TAG ="HistoryAdapter";
    private ArrayList<HistoryPojo> items;
    private Context context;
    private Loading loading;
    private DatabaseReference ref;
    public HistoryAdapter(Context context, ArrayList<HistoryPojo> items, DatabaseReference ref) {
        this.context = context;
        this.items =items;
        this.ref = ref;
        loading = new Loading(context);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View menuItemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_history, parent, false);
        return new HistoryAdapter.MainHolder(menuItemLayoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,
                                 final int position) {
        final HistoryPojo historyPojo =(HistoryPojo) items.get(position);
        HistoryAdapter.MainHolder h = (HistoryAdapter.MainHolder) holder;

        historyPojo.setFlmL(Float.parseFloat(String.valueOf((historyPojo.getFlmL()*33.33))));
        historyPojo.setFlL(historyPojo.getFlmL()/1000);

        int value = historyPojo.getWaterLevel();
        if (value > 700  ) {
            historyPojo.setWaterStatus("High");
        }
        else if ((value > 301  ) && (value < 699)) {
            historyPojo.setWaterStatus("Normal");
        }
        else{
            historyPojo.setWaterStatus("Low");
        }


        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(historyPojo.getCreatedDate());
        SimpleDateFormat df = new SimpleDateFormat("MMM dd,  yyyy  hh:mm a");
        h.txt_title.setText(historyPojo.getTitle());
        h.txt_desc.setText(historyPojo.getTitle());
        h.txt_datetime.setText(df.format(c.getTime()));

        h.txt_maingate.setText("Main Gate: "+(historyPojo.getMainGate() == 1 ? "On" : "Off"));
        h.txt_gatea.setText("Gate A: "+(historyPojo.getGateA() == 1 ? "On" : "Off"));
        h.txt_gateb.setText("Gate B: "+(historyPojo.getGateB() == 1 ? "On" : "Off"));
        h.txt_gatec.setText("Gate C: "+(historyPojo.getGateC() == 1 ? "On" : "Off"));

        h.txt_waterLevel.setText("Water Level: "+historyPojo.getWaterLevel().toString());
        h.txt_waterLevelTrigger.setText("Water Level Trigger: "+historyPojo.getWaterLevelTrigger().toString());


        h.txt_waterLvlStatus.setText("Water Status: "+historyPojo.getWaterStatus());
        h.txt_Fl.setText("Flow rate: "+String.format("%.2f",historyPojo.getFlRate())+" L/min");
        h.txt_flOutPut.setText("Output Liquid Quantity: "+String.format("%.2f",historyPojo.getFlmL())+" ml / "+String.format("%.2f", historyPojo.getFlL())+" L");
    }

    public static class MainHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView img;
        TextView txt_title,txt_desc,txt_datetime,
                txt_maingate,txt_gatea,txt_gateb,txt_gatec,
                txt_Fl,txt_flOutPut,txt_waterLevel,txt_waterLvlStatus,
                txt_waterLevelTrigger;
        public MainHolder(@NonNull View v) {
            super(v);
            cardView = v.findViewById(R.id.card);
            txt_title = v.findViewById(R.id.txt_title);
            txt_desc = v.findViewById(R.id.txt_desc);
            txt_datetime = v.findViewById(R.id.txt_datetime);
            txt_maingate = v.findViewById(R.id.txt_maingate);
            txt_gatea = v.findViewById(R.id.txt_gatea);
            txt_gateb = v.findViewById(R.id.txt_gateb);
            txt_gatec = v.findViewById(R.id.txt_gatec);
            txt_Fl = v.findViewById(R.id.txt_Fl);
            txt_flOutPut = v.findViewById(R.id.txt_flOutPut);
            txt_waterLevel = v.findViewById(R.id.txt_waterLevel);
            txt_waterLvlStatus = v.findViewById(R.id.txt_waterLvlStatus);
            txt_waterLevelTrigger = v.findViewById(R.id.txt_waterLevelTrigger);
        }
    }
}