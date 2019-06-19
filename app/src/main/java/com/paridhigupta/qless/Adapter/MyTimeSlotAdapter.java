package com.paridhigupta.qless.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paridhigupta.qless.Common.Common;
import com.paridhigupta.qless.Model.TimeSlot;
import com.paridhigupta.qless.R;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class MyTimeSlotAdapter extends RecyclerView.Adapter<MyTimeSlotAdapter.MyViewHolder> {

    Context context;
    List<TimeSlot> timeSlotList;

    public MyTimeSlotAdapter(Context context) {
        this.context = context;
        this.timeSlotList = new ArrayList<>();
    }

    public MyTimeSlotAdapter(Context context, List<TimeSlot> timeSlotList) {
        this.context = context;
        this.timeSlotList = timeSlotList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_time_slot, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.text_time_slot.setText(new StringBuilder(Common.convertTimeSlotToString(i)).toString());
        if(timeSlotList.size()==0){
            myViewHolder.card_time_slot.setCardBackgroundColor(context.getResources()
                    .getColor(R.color.white));
            myViewHolder.text_time_slot_desc.setText("Available");
            myViewHolder.text_time_slot_desc.setTextColor(context.getResources()
            .getColor(R.color.black));
            myViewHolder.text_time_slot.setTextColor(context.getResources()
            .getColor(R.color.black));

        }
        else{
            for(TimeSlot slotValue : timeSlotList){
                int slot = Integer.parseInt(slotValue.getSlot().toString());
                if(slot == i){
                    myViewHolder.card_time_slot.setCardBackgroundColor(context.getResources()
                            .getColor(R.color.gray));
                    myViewHolder.text_time_slot_desc.setText("Full");
                    myViewHolder.text_time_slot_desc.setTextColor(context.getResources()
                            .getColor(R.color.white));
                    myViewHolder.text_time_slot.setTextColor(context.getResources()
                            .getColor(R.color.white));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return Common.TIME_SLOT_TOTAL;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView text_time_slot, text_time_slot_desc;
        CardView card_time_slot;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card_time_slot = itemView.findViewById(R.id.card_time_slot);
            text_time_slot = itemView.findViewById(R.id.text_time_slot);
            text_time_slot_desc = itemView.findViewById(R.id.text_time_slot_desc);
        }
    }
}
