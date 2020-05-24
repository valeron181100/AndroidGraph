package com.valeron.androidgraph;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class MainActivity extends AppCompatActivity {

    ImageView mBottomBarIV;
    LinearLayout mBottomSheetLL;
    BottomSheetBehavior<LinearLayout> mBottomSheetBehavior;
    FrameLayout mBottomFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomBarIV = findViewById(R.id.bottomBarIV);
        mBottomSheetLL = findViewById(R.id.bottom_sheet);
        mBottomFrameLayout = findViewById(R.id.bottomFrame);
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
        Fragment controlFragment = fm.findFragmentById(R.id.controlFragment);
        if(graphFragment == null){
            graphFragment = new GraphFragment();
        }
        if(controlFragment == null){
            controlFragment = new ControlPanelFragment();
        }
        fm.beginTransaction().replace(R.id.graphFragment, graphFragment)
                .replace(R.id.controlFragment, controlFragment).commit();

    }
}
