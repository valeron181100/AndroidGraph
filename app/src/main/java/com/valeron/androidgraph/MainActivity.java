package com.valeron.androidgraph;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class MainActivity extends AppCompatActivity {

    ImageView mBottomBarIV;
    LinearLayout mBottomSheetLL;
    BottomSheetBehavior<LinearLayout> mBottomSheetBehavior;
    RelativeLayout mBottomFrameLayout;
    TabLayout mTabLayout;
    ViewPager mControlPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomBarIV = findViewById(R.id.bottomBarIV);
        mBottomSheetLL = findViewById(R.id.bottom_sheet);
        mBottomFrameLayout = findViewById(R.id.bottomFrame);
        mTabLayout = findViewById(R.id.bottomTabLayout);
        mControlPager = findViewById(R.id.controlViewPager);
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheetLL);

        mBottomFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                else
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        mBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float v) {
                mBottomBarIV.setRotation(v * 180);
            }
        });

        FragmentManager fm = getSupportFragmentManager();
        Fragment graphFragment = fm.findFragmentById(R.id.graphFragment);
        if(graphFragment == null){
            graphFragment = new GraphFragment();
        }
        fm.beginTransaction().replace(R.id.graphFragment, graphFragment).commit();
        mControlPager.setAdapter(new SrollControlFragmentsPagerAdapter(this, fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));
        mControlPager.setCurrentItem(0);
        mTabLayout.setupWithViewPager(mControlPager);
    }


    private class SrollControlFragmentsPagerAdapter extends FragmentStatePagerAdapter {
        private Context mContext;

        public SrollControlFragmentsPagerAdapter(Context context, @NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
            mContext = context;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                return new ControlPanelFragment();
            }else{
                return new ControlCompPanelFragment();
            }

        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0){
                return mContext.getResources().getString(R.string.one_exp_str);
            }else{
                return mContext.getResources().getString(R.string.comp_exp_str);
            }
        }
    }

}
