package com.volcanoscar.fragview;

import android.support.v4.app.Fragment;
import android.widget.FrameLayout;
import android.widget.ListView;

/**
 * Created by volcanoscar on 16/3/27.
 */
public class BaseFragment extends Fragment {
    protected ListView listView;

    public void resetLvHeight(boolean expand){
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) listView.getLayoutParams();
        if (expand){
            layoutParams.topMargin = (int) getActivity().getResources().getDimension(R.dimen.lv_margintop_min);
        }else {
            layoutParams.topMargin = (int) getActivity().getResources().getDimension(R.dimen.lv_margintop_max);
        }
        listView.setLayoutParams(layoutParams);
    }
}
