package com.paridhigupta.qless.Interface;

import com.paridhigupta.qless.Model.Banner;

import java.util.List;

public interface IBannerLoadListener {
    void onBannerLoadSuccess(List<Banner> banners);
    void onBannerLoadFailure(String message);
}
