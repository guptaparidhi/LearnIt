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
import com.paridhigupta.qless.Model.Machine;
import com.paridhigupta.qless.R;

import java.util.ArrayList;
import java.util.List;

public class MyMachineAdapter extends RecyclerView.Adapter<MyMachineAdapter.MyViewHolder> {

    Context context;
    List<Machine> machineList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public MyMachineAdapter(Context context, List<Machine> machineList) {
        this.context = context;
        this.machineList = machineList;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_machine, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        myViewHolder.text_machine_numb.setText(machineList.get(i).getNumb());
        myViewHolder.text_machine_id.setText(machineList.get(i).getIdentity());
        if(!cardViewList.contains(myViewHolder.card_machine))
            cardViewList.add(myViewHolder.card_machine);

        myViewHolder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                for(CardView cardView : cardViewList)
                    cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
            myViewHolder.card_machine.setCardBackgroundColor(context
            .getResources().getColor(R.color.light_orange));

            //send local Broadcast to enable button next
                Intent i = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                i.putExtra(Common.KEY_MACHINE_SELECTED,machineList.get(pos));
                i.putExtra(Common.KEY_STEP, 2);
                localBroadcastManager.sendBroadcast(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return machineList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView text_machine_numb, text_machine_id;
        CardView card_machine;
        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;


        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card_machine = itemView.findViewById(R.id.card_machine);
            text_machine_numb = itemView.findViewById(R.id.text_machine_numb);
            text_machine_id = itemView.findViewById(R.id.text_machine_id);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener.onItemSelectedListener(itemView, getAdapterPosition());
        }
    }
}
