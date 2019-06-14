package com.paridhigupta.qless.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paridhigupta.qless.Model.Floor;
import com.paridhigupta.qless.R;

import java.util.List;

public class MyFloorAdapter extends RecyclerView.Adapter<MyFloorAdapter.MyViewHolder> {

    private Context context;
    private List<Floor> floorList;

    public MyFloorAdapter(Context context, List<Floor> floorList) {
        this.context = context;
        this.floorList = floorList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context)
        .inflate(R.layout.layout_floor, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.text_Floor.setText(floorList.get(i).getFloor());
//        myViewHolder.text_Floor_wm.setText(floorList.get(i).getNum());
//        myViewHolder.text_floor_id.setText(floorList.get(i).getIdentity());

    }

    @Override
    public int getItemCount() {
        return floorList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text_Floor, text_Floor_wm, text_floor_id;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            text_Floor = itemView.findViewById(R.id.text_floor);
//            text_Floor_wm = itemView.findViewById(R.id.text_floor_vm);
//            text_floor_id = itemView.findViewById(R.id.text_floor_id);
        }
    }
}
