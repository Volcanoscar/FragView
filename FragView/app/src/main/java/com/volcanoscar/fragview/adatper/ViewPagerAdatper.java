package com.volcanoscar.fragview.adatper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.volcanoscar.fragview.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by volcanoscar on 16/3/27.
 */
public class ViewPagerAdatper extends FragmentPagerAdapter {

    private List<BaseFragment> mfragments;
    private List<String> mFragWithTags;
    private FragmentManager fm;

    public ViewPagerAdatper(FragmentManager fm , List<BaseFragment> mfragments) {
        super(fm);
        this.fm = fm;
        this.mfragments = mfragments;
        mFragWithTags = new ArrayList<>();
    }

    @Override
    public BaseFragment getItem(int position) {
        return mfragments.get(position);
    }

    @Override
    public int getCount() {
        return mfragments.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        mFragWithTags.add(makeFragmentName(container.getId(),position));
        return super.instantiateItem(container, position);
    }

    public void update(int item,boolean expand){
        BaseFragment fragment = (BaseFragment) fm.findFragmentByTag(mFragWithTags.get(item));
        if(fragment != null){
            fragment.resetLvHeight(expand);
        }
    }

    private String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }
}
