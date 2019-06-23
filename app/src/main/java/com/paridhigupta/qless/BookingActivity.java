package com.paridhigupta.qless;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.paridhigupta.qless.Adapter.MyViewPagerAdapter;
import com.paridhigupta.qless.Common.Common;
import com.paridhigupta.qless.Common.NonSwipeViewPager;
import com.paridhigupta.qless.Model.Machine;
import com.shuhart.stepview.StepView;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

public class BookingActivity extends AppCompatActivity {

    LocalBroadcastManager localBroadcastManager;
    AlertDialog alertDialog;
    CollectionReference machineRef;

    @BindView(R.id.step_view)
    StepView stepView;

    @BindView(R.id.view_pager)
    NonSwipeViewPager viewPager;

    @BindView(R.id.btn_previous_step)
    Button btn_previous_step;

    @BindView(R.id.btn_next_step)
    Button btn_next_step;

    @OnClick(R.id.btn_previous_step)
    void previousStep(){
        if(Common.step == 3  || Common.step>0){
            Common.step --;
            viewPager.setCurrentItem(Common.step);
            if(Common.step<3){
                btn_next_step.setEnabled(true);
                setColorButton();
            }
        }
    }
    @OnClick(R.id.btn_next_step)
    void nextClick(){
        if(Common.step < 3 || Common.step == 0){
            Common.step ++;

            if(Common.step == 1){
                if(Common.CurrentFloor != null){
                    loadMachinesOnFloor(Common.CurrentFloor.getFloor_id());
                }
            }

            else if(Common.step == 2) {
                if(Common.currentMachine != null)
                    loadTimeSlotOfMachine(Common.currentMachine.getMachine_id());
            }

            else if(Common.step == 3) {
                if(Common.currentTimeSlot != -1)
                    confirmBooking();

            }
            viewPager.setCurrentItem(Common.step);
        }
    }

    private void confirmBooking() {
        //send broadcast to fragment step 4
        Intent intent = new Intent(Common.KEY_CONFIRM_BOOKING);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void loadTimeSlotOfMachine(String machine_id) {
        //Send local Broadcast to fragment step 3
        Intent intent = new Intent(Common.KEY_DISPLAY_TIME_SLOT);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void loadMachinesOnFloor(String floor_id) {
        alertDialog.show();
        if(!TextUtils.isEmpty(Common.hostel)){
            machineRef = FirebaseFirestore.getInstance()
                    .collection("Hostel")
                    .document(Common.hostel)
                    .collection("Floor")
                    .document(floor_id)
                    .collection("WashingMachines");

            machineRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    ArrayList<Machine> machines = new ArrayList<>();
                    for(QueryDocumentSnapshot machineSnapShot : task.getResult()){
                        Machine machine = machineSnapShot.toObject(Machine.class);
                        machine.setMachine_id(machineSnapShot.getId());
                        machines.add(machine);
                    }

                    //Send Broadcast to BookingStep2Fragment to load Recycler
                    Intent i = new Intent(Common.KEY_MACHINE_LOAD_DONE);
                    i.putParcelableArrayListExtra(Common.KEY_MACHINE_LOAD_DONE, machines);
                    localBroadcastManager.sendBroadcast(i);
                    alertDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BookingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            });
        }
    }

    private BroadcastReceiver button_next_receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int step = intent.getIntExtra(Common.KEY_STEP, 0);
            if(step == 1)
                Common.CurrentFloor = intent.getParcelableExtra(Common.KEY_HOSTEL_FLOOR);
            else if(step == 2)
                Common.currentMachine = intent.getParcelableExtra(Common.KEY_MACHINE_SELECTED);
            else if(step == 3)
                Common.currentTimeSlot = intent.getIntExtra(Common.KEY_TIME_SLOT, -1);
            btn_next_step.setEnabled(true);
            setColorButton();
        }
    };

    @Override
    protected void onDestroy() {
        localBroadcastManager.unregisterReceiver(button_next_receiver);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        ButterKnife.bind(BookingActivity.this);
        setUpStepView();
        setColorButton();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(button_next_receiver, new IntentFilter(Common.KEY_ENABLE_BUTTON_NEXT));
        alertDialog = new SpotsDialog.Builder().setContext(this).build();
        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

                stepView.go(i, true);

                if(i == 0)
                    btn_previous_step.setEnabled(false);
                else
                    btn_previous_step.setEnabled(true);

                btn_next_step.setEnabled(false);

                setColorButton();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void setColorButton() {
        if(btn_next_step.isEnabled()){
            btn_next_step.setBackgroundResource(R.color.colorAccent);
        }
        else{
            btn_next_step.setBackgroundResource(R.color.colorPrimaryDark);
        }

        if(btn_previous_step.isEnabled()){
            btn_previous_step.setBackgroundResource(R.color.colorAccent);
        }
        else{
            btn_previous_step.setBackgroundResource(R.color.colorPrimaryDark);
        }
    }

    private void setUpStepView() {
        List<String> stepList = new ArrayList<>();
        stepList.add("Hostel");
        stepList.add("Machine");
        stepList.add("Time");
        stepList.add("Confirm");
        stepView.setSteps(stepList);
    }
    
}
