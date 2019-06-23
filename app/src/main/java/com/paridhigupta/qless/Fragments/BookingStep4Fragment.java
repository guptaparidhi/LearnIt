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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.paridhigupta.qless.Common.Common;
import com.paridhigupta.qless.Model.BookingInformation;
import com.paridhigupta.qless.Model.Floor;
import com.paridhigupta.qless.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class BookingStep4Fragment extends Fragment {

    SimpleDateFormat simpleDateFormat;
    LocalBroadcastManager localBroadcastManager;
    Unbinder unbinder;

    @BindView(R.id.text_booking_time)
    TextView text_booking_time;
    @BindView(R.id.text_booking_hostel)
    TextView text_booking_hostel;
    @BindView(R.id.text_booking_floor)
    TextView text_booking_floor;
    @BindView(R.id.text_booking_machine)
    TextView text_booking_machine;

    @OnClick(R.id.btn_confirm)
    void onConfirmBooking(){
        BookingInformation bookingInformation = new BookingInformation();
        bookingInformation.setCustomerName(Common.currentUser.getName());
        bookingInformation.setCustomerPhone(Common.currentUser.getPhoneNumber());
        bookingInformation.setFloorId(Common.CurrentFloor.getFloor_id());
        bookingInformation.setFloorNumb(Common.CurrentFloor.getFloor());
        bookingInformation.setMachineId(Common.currentMachine.getMachine_id());
        bookingInformation.setMachineNumb(Common.currentMachine.getNumb());
        bookingInformation.setMachineIdentity(Common.currentMachine.getIdentity());
        bookingInformation.setTime(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
                .append("on")
                .append(simpleDateFormat.format(Common.currentDate.getTime())).toString());
        bookingInformation.setSlot(Long.valueOf(Common.currentTimeSlot));

        //submit to machine document
        DocumentReference bookingDate = FirebaseFirestore.getInstance()
                .collection("Hostel")
                .document(Common.hostel)
                .collection("Floor")
                .document(Common.CurrentFloor.getFloor_id())
                .collection("WashingMachines")
                .document(Common.currentMachine.getMachine_id())
                .collection(Common.simpleFormatDate.format(Common.currentDate.getTime()))
                .document(String.valueOf(Common.currentTimeSlot));

        //write data
        bookingDate.set(bookingInformation).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                resetStaticData();
                getActivity().finish();
                Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetStaticData() {
        Common.step = 0;
        Common.currentTimeSlot = -1;
        Common.currentMachine = null;
        Common.CurrentFloor = null;
        Common.currentDate.add(Calendar.DATE, 0);
    }

    BroadcastReceiver confirmBookingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setData();
        }
    };

    private void setData() {
        text_booking_hostel.setText(Common.hostel);
        text_booking_floor.setText(Common.CurrentFloor.getFloor());
        text_booking_machine.setText(Common.currentMachine.getIdentity());
        text_booking_time.setText(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
        .append("on")
        .append(simpleDateFormat.format(Common.currentDate.getTime())));

    }

    static BookingStep4Fragment instance;
    public static BookingStep4Fragment getInstance() {
        if(instance==null){
            instance = new BookingStep4Fragment();
        }
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Date format visible on confirm page
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(confirmBookingReceiver, new IntentFilter(Common.KEY_CONFIRM_BOOKING));

    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(confirmBookingReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View itemView = inflater.inflate(R.layout.fragment_book_step_four, container, false);
        unbinder = ButterKnife.bind(this, itemView);
        return itemView;
    }
}
