package com.softwaresolution.water_irrigation.Adapters;

import android.app.Activity;
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

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.softwaresolution.water_irrigation.Activities.AddSchedule;
import com.softwaresolution.water_irrigation.Interactive.Loading;
import com.softwaresolution.water_irrigation.Pojo.SchedulePojo;
import com.softwaresolution.water_irrigation.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String TAG ="ScheduleAdapter";
    private ArrayList<SchedulePojo> items;
    private Context context;
    private Loading loading;
    private DatabaseReference ref;
    public ScheduleAdapter(Context context, ArrayList<SchedulePojo> items, DatabaseReference ref) {
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
                R.layout.item_schedule, parent, false);
        return new ScheduleAdapter.MainHolder(menuItemLayoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,
                                 final int position) {
        final SchedulePojo schedPojo =(SchedulePojo) items.get(position);
        ScheduleAdapter.MainHolder h = (ScheduleAdapter.MainHolder) holder;

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(schedPojo.getTimestamp());
        SimpleDateFormat df = new SimpleDateFormat("MMM dd,  yyyy  hh:mm a");
        Log.d(TAG, c.getTime().toString()+df.format(c.getTime()) );
        h.txt_title.setText(schedPojo.getTitle());
        h.txt_desc.setText(schedPojo.getTitle());
        h.txt_datetime.setText(df.format(c.getTime()));
        h.txt_exe.setText(schedPojo.getIsExecuted() ? "Executed" : "Not yet execute");
        if(schedPojo.getIsExecuted()){
            h.btn_update.setVisibility(View.GONE);
        }
        h.btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, AddSchedule.class)
                        .putExtra("action","update")
                        .putExtra("sched",new Gson().toJson(schedPojo))
                        .putExtra("index",h.getAdapterPosition()));
            }
        });
        h.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.loadDialog.show();
                items.remove(h.getAdapterPosition());
                ref.setValue(items);
                loading.loadDialog.dismiss();
                Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void removeAt(int position) {
        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, items.size());
    }

    public static class MainHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView img;
        TextView txt_title,txt_desc,txt_datetime,txt_exe;
        Button btn_delete,btn_update;
        public MainHolder(@NonNull View v) {
            super(v);
            cardView = v.findViewById(R.id.card);
            txt_title = v.findViewById(R.id.txt_title);
            txt_desc = v.findViewById(R.id.txt_desc);
            txt_datetime = v.findViewById(R.id.txt_datetime);
            txt_exe = v.findViewById(R.id.txt_exe);
            btn_update = v.findViewById(R.id.btn_update);
            btn_delete = v.findViewById(R.id.btn_delete);

        }
    }
}