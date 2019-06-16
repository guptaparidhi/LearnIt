package com.paridhigupta.qless.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paridhigupta.qless.Adapter.MyMachineAdapter;
import com.paridhigupta.qless.Common.Common;
import com.paridhigupta.qless.Common.spacesItemDecoration;
import com.paridhigupta.qless.Model.Machine;
import com.paridhigupta.qless.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BookingStep2Fragment extends Fragment {

    Unbinder unbinder;
    LocalBroadcastManager localBroadcastManager;

    @BindView(R.id.recycler_machine)
    RecyclerView recycler_machine;

    private BroadcastReceiver machineDoneReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Machine> machineArrayList = intent.getParcelableArrayListExtra(Common.KEY_MACHINE_LOAD_DONE);
            MyMachineAdapter adapter = new MyMachineAdapter(getContext(), machineArrayList);
            recycler_machine.setAdapter(adapter);
        }
    };

    static BookingStep2Fragment instance;
    public static BookingStep2Fragment getInstance() {
        if(instance==null){
            instance = new BookingStep2Fragment();
        }
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(machineDoneReceiver, new IntentFilter(Common.KEY_MACHINE_LOAD_DONE));
    }


    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(machineDoneReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View itemView = inflater.inflate(R.layout.fragment_book_step_two, container, false);
        unbinder = ButterKnife.bind(this, itemView);
        initView();
        return itemView;
    }

    private void initView() {
        recycler_machine.setHasFixedSize(true);
        recycler_machine.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recycler_machine.addItemDecoration(new spacesItemDecoration(4));
    }
}
