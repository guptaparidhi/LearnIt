package com.paridhigupta.qless.Interface;

import com.paridhigupta.qless.Model.Banner;

import java.util.List;

public interface IQuotesLoadListener {
    void onQuotesLoadSuccess(List<Banner> banners);
    void onQuotesLoadFailure(String message);
}
