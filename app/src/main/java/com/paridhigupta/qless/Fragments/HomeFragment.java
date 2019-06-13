package com.paridhigupta.qless.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.accountkit.AccountKit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.paridhigupta.qless.Adapter.HomeSliderAdapter;
import com.paridhigupta.qless.Adapter.QuotesAdapter;
import com.paridhigupta.qless.BookingActivity;
import com.paridhigupta.qless.Common.Common;
import com.paridhigupta.qless.Interface.IBannerLoadListener;
import com.paridhigupta.qless.Interface.IQuotesLoadListener;
import com.paridhigupta.qless.Model.Banner;
import com.paridhigupta.qless.R;
import com.paridhigupta.qless.Service.PicassoImageLoadingService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ss.com.bannerslider.Slider;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements IBannerLoadListener, IQuotesLoadListener {

    private Unbinder unbinder;

    @BindView(R.id.layout_user_information)
    LinearLayout layout_user_information;

    @BindView(R.id.txt_user_name)
    TextView txt_user_name;

    @BindView(R.id.banner_slidder)
    Slider banner_slider;

    @BindView(R.id.recycler_quotes)
    RecyclerView recycler_quotes;

    @OnClick(R.id.card_view_booking)
    void booking(){
        startActivity(new Intent(getActivity(), BookingActivity.class));
    }

    CollectionReference bannerRef, quotesRef;

    IBannerLoadListener iBannerLoadListener;
    IQuotesLoadListener iQuotesLoadListener;

    public HomeFragment() {
        bannerRef = FirebaseFirestore.getInstance().collection("Banner");
        quotesRef = FirebaseFirestore.getInstance().collection("Quotes");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);

        Slider.init(new PicassoImageLoadingService());

        iBannerLoadListener = this;
        iQuotesLoadListener = this;

        if(AccountKit.getCurrentAccessToken()!=null){
            setUserInformation();
            loadBanner();
//            loadQuotes();
        }
        return view;
    }

    private void loadQuotes() {
        quotesRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Banner> quotes = new ArrayList<>();
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot bannerSnapShots : task.getResult()){
                                Banner banner = bannerSnapShots.toObject(Banner.class);
                                quotes.add(banner);
                            }
                            iQuotesLoadListener.onQuotesLoadSuccess(quotes);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iQuotesLoadListener.onQuotesLoadFailure(e.getMessage());
            }
        });
    }

    private void loadBanner() {
        bannerRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Banner> banners = new ArrayList<>();
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot bannerSnapShots : task.getResult()){
                                Banner banner = bannerSnapShots.toObject(Banner.class);
                                banners.add(banner);
                            }
                            iBannerLoadListener.onBannerLoadSuccess(banners);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iBannerLoadListener.onBannerLoadFailure(e.getMessage());
            }
        });
    }

    private void setUserInformation() {
        layout_user_information.setVisibility(View.VISIBLE);
        txt_user_name.setText(Common.currentUser.getName());
    }

    @Override
    public void onBannerLoadSuccess(List<Banner> banners) {
        banner_slider.setAdapter(new HomeSliderAdapter(banners));
    }

    @Override
    public void onBannerLoadFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onQuotesLoadSuccess(List<Banner> banners) {
        recycler_quotes.setHasFixedSize(true);
        recycler_quotes.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_quotes.setAdapter(new QuotesAdapter(getActivity(), banners));
    }

    @Override
    public void onQuotesLoadFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

    }
}
