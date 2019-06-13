package com.paridhigupta.qless;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.paridhigupta.qless.Adapter.MyViewPagerAdapter;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookingActivity extends AppCompatActivity {

    @BindView(R.id.step_view)
    StepView stepView;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.btn_previous_step)
    Button btn_previous_step;

    @BindView(R.id.btn_next_step)
    Button btn_next_step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        ButterKnife.bind(BookingActivity.this);
        setUpStepView();
        setColorButton();

        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if(i == 0)
                    btn_previous_step.setEnabled(false);
                else
                    btn_previous_step.setEnabled(true);

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
        stepList.add("Hotel");
        stepList.add("Floor");
        stepList.add("Time");
        stepList.add("Confirm");
        stepView.setSteps(stepList);
    }
    
}