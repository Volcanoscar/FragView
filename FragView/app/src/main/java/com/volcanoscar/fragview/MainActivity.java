package com.volcanoscar.fragview;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.volcanoscar.fragview.adatper.ViewPagerAdatper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {

    private ViewPager mViewPager;
    private ViewPagerAdatper mAdapter;
    private List<BaseFragment> mFragments;

    public static int MARGINTOP_MAX;
    public static int MARGINTOP_MIN;
    public static int MOVE_BAUNDRY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createFragments();
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(3);
        mAdapter = new ViewPagerAdatper(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(mAdapter);
        MARGINTOP_MAX = getResources().getDimensionPixelSize(R.dimen.lv_margintop_max);
        MARGINTOP_MIN = getResources().getDimensionPixelSize(R.dimen.lv_margintop_min);
        MOVE_BAUNDRY = getResources().getDimensionPixelSize(R.dimen.lv_move_baundry);
    }

    private void createFragments() {
        mFragments = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            mFragments.add(ItemFragment.newInstance(i));
        }
    }

    @Override
    public void onListFragmentInteraction(int position, boolean expand) {
        for (int i = 0; i < 4; i++) {
            mAdapter.update(i, expand);
        }
    }
    private static boolean EXPAND = false;
    public static void setExpand(boolean expand){
        EXPAND = expand;
    }

    public static boolean isExpand(){
        return EXPAND;
    }
}
