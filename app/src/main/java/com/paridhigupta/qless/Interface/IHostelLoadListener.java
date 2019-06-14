package com.paridhigupta.qless.Interface;

import java.util.List;

public interface IHostelLoadListener {
    void onIHostelLoadSuccess(List<String> hostelNameList);
    void onIHostelLoadFailure(String message);
}
