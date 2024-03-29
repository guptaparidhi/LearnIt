package com.paridhigupta.qless.Fragments;

import android.app.AlertDialog;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.paridhigupta.qless.Adapter.MyMachineAdapter;
import com.paridhigupta.qless.Adapter.MyTimeSlotAdapter;
import com.paridhigupta.qless.Common.Common;
import com.paridhigupta.qless.Common.spacesItemDecoration;
import com.paridhigupta.qless.Interface.ITimeSlotLoadListener;
import com.paridhigupta.qless.Model.TimeSlot;
import com.paridhigupta.qless.R;

import org.xml.sax.helpers.NamespaceSupport;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import dmax.dialog.SpotsDialog;

public class BookingStep3Fragment extends Fragment implements ITimeSlotLoadListener {

    DocumentReference machineDoc;
    ITimeSlotLoadListener iTimeSlotLoadListener;
    AlertDialog dialog;
    Unbinder unbinder;
    LocalBroadcastManager localBroadcastManager;
    @BindView(R.id.recycler_time_slot)
    RecyclerView recycler_time_slot;
    @BindView(R.id.calenderView)
    HorizontalCalendarView calenderView;
    SimpleDateFormat simpleDateFormat;
    Calendar selected_Date;


    BroadcastReceiver displayTimeSlot = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Calendar date = Calendar.getInstance();
            date.add(Calendar.DATE, 0);
            loadAvailableTimeSlotOfMachine(Common.currentMachine.getMachine_id(),
                    simpleDateFormat.format(date.getTime()));
        }
    };
// /Hostel/Kaveri/Floor/FREp2qfXg3OZh27QpoiK/WashingMachines/IyZfXqew1ebJYkqyuaI3
    private void loadAvailableTimeSlotOfMachine(String machine_id, final String bookDate) {
        dialog.show();
        machineDoc = FirebaseFirestore.getInstance()
                .collection("Hostel")
                .document(Common.hostel)
                .collection("Floor")
                .document(Common.CurrentFloor.getFloor_id())
                .collection("WashingMachines")
                .document(Common.currentMachine.getMachine_id());

        //Get info of this machine
        machineDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    final DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){ //if machine available
                        CollectionReference date = FirebaseFirestore.getInstance()
                                .collection("Hostel")
                                .document(Common.hostel)
                                .collection("Floor")
                                .document(Common.CurrentFloor.getFloor_id())
                                .collection("WashingMachines")
                                .document(Common.currentMachine.getMachine_id())
                                .collection(bookDate);

                        date.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if(querySnapshot.isEmpty()){
                                        iTimeSlotLoadListener.onTimeSlotLoadEmpty();
                                    }
                                    else{
                                        List<TimeSlot> timeSlots = new ArrayList<>();
                                        for(QueryDocumentSnapshot document : task.getResult())
                                            timeSlots.add(document.toObject(TimeSlot.class));

                                        iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlots);
                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                iTimeSlotLoadListener.onTimeSlotLoadFailed(e.getMessage());
                            }
                        });
                    }
                }
            }
        });
    }

    static BookingStep3Fragment instance;
    public static BookingStep3Fragment getInstance() {
        if(instance==null){
            instance = new BookingStep3Fragment();
        }
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iTimeSlotLoadListener = this;
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(displayTimeSlot, new IntentFilter(Common.KEY_DISPLAY_TIME_SLOT));
        simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");
        dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(displayTimeSlot);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View itemView = inflater.inflate(R.layout.fragment_book_step_three, container, false);
        unbinder = ButterKnife.bind(this, itemView);
        init(itemView);
        return itemView;
    }

    private void init(View itemView) {
        recycler_time_slot.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recycler_time_slot.setLayoutManager(gridLayoutManager);
        recycler_time_slot.addItemDecoration(new spacesItemDecoration(8));

        //Calender
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE, 0);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DATE, 2);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(itemView, R.id.calenderView)
                .range(startDate, endDate)
                .datesNumberOnScreen(1)
                .mode(HorizontalCalendar.Mode.DAYS)
                .defaultSelectedDate(startDate)
                .build();
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                if(Common.currentDate.getTimeInMillis() != date.getTimeInMillis()){
                    Common.currentDate = date;
                    loadAvailableTimeSlotOfMachine(Common.currentMachine.getMachine_id(),
                            simpleDateFormat.format(date.getTime()));
                }
            }
        });
    }

    @Override
    public void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList) {
        MyTimeSlotAdapter myTimeSlotAdapter = new MyTimeSlotAdapter(getContext(), timeSlotList);
        recycler_time_slot.setAdapter(myTimeSlotAdapter);
        dialog.dismiss();
    }

    @Override
    public void onTimeSlotLoadFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    @Override
    public void onTimeSlotLoadEmpty() {
        MyTimeSlotAdapter myTimeSlotAdapter = new MyTimeSlotAdapter(getContext());
        recycler_time_slot.setAdapter(myTimeSlotAdapter);
        dialog.dismiss();
    }
}
