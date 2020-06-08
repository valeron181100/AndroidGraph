package com.valeron.androidgraph;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.valeron.androidgraph.logic.SolveMethod;
import com.valeron.androidgraph.model.InputValuesModel;
import com.valeron.androidgraph.model.ObservableBoolean;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageView mBottomBarIV;
    LinearLayout mBottomSheetLL;
    BottomSheetBehavior<LinearLayout> mBottomSheetBehavior;
    RelativeLayout mBottomFrameLayout;
    TabLayout mTabLayout;
    ViewPager mControlPager;
    ViewPager mMethodTypePager;
    MethodTypePagerAdapter mMethodTypePagerAdapter;
    InputValuesModel mValuesModel;
    private int mSolveWaysNumber = 2;
    private ObservableBoolean isWaysNumChanged = new ObservableBoolean(false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
                if(e instanceof RuntimeException){
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBottomBarIV = findViewById(R.id.bottomBarIV);
        mBottomSheetLL = findViewById(R.id.bottom_sheet);
        mBottomFrameLayout = findViewById(R.id.bottomFrame);
        mTabLayout = findViewById(R.id.bottomTabLayout);
        mControlPager = findViewById(R.id.controlViewPager);
        mMethodTypePager = findViewById(R.id.methodTypePager);
        mMethodTypePagerAdapter = new MethodTypePagerAdapter(this);
        mValuesModel = InputValuesModel.getInstance();


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

        mMethodTypePager.setAdapter(mMethodTypePagerAdapter);
        mMethodTypePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(mSolveWaysNumber == 2 && position == 0)
                    mValuesModel.setSolveMethod(SolveMethod.Chords);
                if(mSolveWaysNumber == 2 && position == 1)
                    mValuesModel.setSolveMethod(SolveMethod.Touch);
                if(mSolveWaysNumber == 1 && position == 0)
                    mValuesModel.setSolveMethod(SolveMethod.Newton);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    mSolveWaysNumber = 2;
                    isWaysNumChanged.setVal(true);
                }else{
                    mSolveWaysNumber = 1;
                    isWaysNumChanged.setVal(true);
                    mValuesModel.setSolveMethod(SolveMethod.Newton);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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


    private class MethodTypePagerAdapter extends PagerAdapter {
        private Context mContext;
        private ArrayList<String> mData;

        public MethodTypePagerAdapter(Context context){
            mContext = context;
            mData = new ArrayList<>();
            if(mSolveWaysNumber == 2){
                mData.clear();
                mData.add("Метод хорд");
                mData.add("Метод касательных");

            }else{
                mData.clear();
                mData.add("Метод Ньютона");
            }

            isWaysNumChanged.addValueChangedListener(new ObservableBoolean.ValueChangedListener() {
                @Override
                public void changed(boolean oldVal, boolean newVal) {
                    if(mData != null) {
                        if (mSolveWaysNumber == 2) {
                            mData.clear();
                            mData.add("Метод хорд");
                            mData.add("Метод касательных");
                            MethodTypePagerAdapter.this.notifyDataSetChanged();
                        } else {
                            mData.clear();
                            mData.add("Метод Ньютона");
                            MethodTypePagerAdapter.this.notifyDataSetChanged();
                        }
                    }
                }
            });

        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
                TextView tv = new TextView(mContext);
                tv.setTextSize(22);
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tv.setText(mData.get(position));
                ViewGroup.LayoutParams params = new ViewPager.LayoutParams();
                params.height = ViewPager.LayoutParams.MATCH_PARENT;
                params.width = ViewPager.LayoutParams.MATCH_PARENT;
                tv.setGravity(Gravity.CENTER);
                tv.setLayoutParams(params);
                container.addView(tv);
                return tv;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View)object);
        }

        @Override
        public int getCount() {
            return mSolveWaysNumber;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }

}
