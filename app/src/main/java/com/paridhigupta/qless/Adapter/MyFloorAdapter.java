package com.paridhigupta.qless.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paridhigupta.qless.Common.Common;
import com.paridhigupta.qless.Interface.IRecyclerItemSelectedListener;
import com.paridhigupta.qless.Model.Floor;
import com.paridhigupta.qless.R;

import java.util.ArrayList;
import java.util.List;

public class MyFloorAdapter extends RecyclerView.Adapter<MyFloorAdapter.MyViewHolder> {

    private Context context;
    private List<Floor> floorList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public MyFloorAdapter(Context context, List<Floor> floorList) {
        this.context = context;
        this.floorList = floorList;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context)
        .inflate(R.layout.layout_floor, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        myViewHolder.text_Floor.setText(floorList.get(i).getFloor());
        if(!cardViewList.contains(myViewHolder.card_floor))
            cardViewList.add(myViewHolder.card_floor);

        myViewHolder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                for(CardView cardView : cardViewList)
                    cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
                myViewHolder.card_floor.setCardBackgroundColor(context.getResources().getColor(R.color.colorAccent));

                //send broadcast to tell BookingActivity to enable next button
                Intent i = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                i.putExtra(Common.KEY_HOSTEL_FLOOR, floorList.get(pos));
                i.putExtra(Common.KEY_STEP, 1);
                localBroadcastManager.sendBroadcast(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return floorList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView card_floor;
        TextView text_Floor, text_Floor_wm, text_floor_id;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card_floor = itemView.findViewById(R.id.card_floor);
            text_Floor = itemView.findViewById(R.id.text_floor);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener.onItemSelectedListener(v, getAdapterPosition());
        }
    }
}
