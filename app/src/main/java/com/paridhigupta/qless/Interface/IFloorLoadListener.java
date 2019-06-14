package com.paridhigupta.qless.Interface;

import com.paridhigupta.qless.Model.Floor;

import java.util.List;

public interface IFloorLoadListener {
    void onIFloorLoadSuccess(List<Floor> floorList);
    void onIFloorLoadFailure(String message);
}
