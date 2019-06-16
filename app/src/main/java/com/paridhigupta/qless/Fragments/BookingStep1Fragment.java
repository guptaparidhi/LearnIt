package com.paridhigupta.qless.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.paridhigupta.qless.Adapter.MyFloorAdapter;
import com.paridhigupta.qless.Common.Common;
import com.paridhigupta.qless.Common.spacesItemDecoration;
import com.paridhigupta.qless.Interface.IFloorLoadListener;
import com.paridhigupta.qless.Interface.IHostelLoadListener;
import com.paridhigupta.qless.Model.Floor;
import com.paridhigupta.qless.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class BookingStep1Fragment extends Fragment implements IHostelLoadListener, IFloorLoadListener {

    CollectionReference hostelRef;
    CollectionReference floorRef;
    IHostelLoadListener iHostelLoadListener;
    IFloorLoadListener iFloorLoadListener;

    @BindView(R.id.spinner)
    MaterialSpinner spinner;

    @BindView(R.id.recycler_hostel)
    RecyclerView recycler_hostel;

    Unbinder unbinder;
    AlertDialog alertDialog;

    static BookingStep1Fragment instance;
    public static BookingStep1Fragment getInstance() {
        if(instance==null){
            instance = new BookingStep1Fragment();
        }
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hostelRef = FirebaseFirestore.getInstance().collection("Hostel");
        iHostelLoadListener = this;
        iFloorLoadListener = this;
        alertDialog = new SpotsDialog.Builder().setContext(getActivity()).build();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View itemView = inflater.inflate(R.layout.fragment_book_step_one, container, false);
        unbinder = ButterKnife.bind(this, itemView);
        initView();
        loadHostel();
        return itemView;
    }

    private void initView() {
        recycler_hostel.setHasFixedSize(true);
        recycler_hostel.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recycler_hostel.addItemDecoration(new spacesItemDecoration(4));
    }

    private void loadHostel() {
        hostelRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            List<String> list = new ArrayList<>();
                            list.add("Please choose the Hostel");
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult())
                                list.add(documentSnapshot.getId());
                            iHostelLoadListener.onIHostelLoadSuccess(list);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iHostelLoadListener.onIHostelLoadFailure(e.getMessage());
            }
        });
    }

    @Override
    public void onIHostelLoadSuccess(List<String> hostelNameList) {
        spinner.setItems(hostelNameList);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(position>0){
                    loadFloorOfHostel(item.toString());
                }
                else {
                    recycler_hostel.setVisibility(View.GONE);
                }
            }
        });
    }

    private void loadFloorOfHostel(String hostelName) {
        alertDialog.show();
        Common.hostel = hostelName;
        floorRef = FirebaseFirestore.getInstance()
                .collection("Hostel")
                .document(hostelName)
                .collection("Floor");
        floorRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Floor> list = new ArrayList<>();
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        Floor floor = documentSnapshot.toObject(Floor.class);
                        floor.setFloor_id(documentSnapshot.getId());
                        list.add(floor);
                    }
                    iFloorLoadListener.onIFloorLoadSuccess(list);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iFloorLoadListener.onIFloorLoadFailure(e.getMessage());
            }
        });
    }

    @Override
    public void onIHostelLoadFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onIFloorLoadSuccess(List<Floor> floorList) {
        MyFloorAdapter adapter = new MyFloorAdapter(getActivity(), floorList);
        recycler_hostel.setAdapter(adapter);
        recycler_hostel.setVisibility(View.VISIBLE);
        alertDialog.dismiss();
    }

    @Override
    public void onIFloorLoadFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        alertDialog.dismiss();
    }
}
