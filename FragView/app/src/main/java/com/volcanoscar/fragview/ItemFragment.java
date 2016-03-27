package com.volcanoscar.fragview;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.volcanoscar.fragview.adatper.ListviewAdapter;
import com.volcanoscar.fragview.dummy.DummyContent;
import com.volcanoscar.fragview.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ItemFragment extends BaseFragment implements View.OnClickListener, View.OnTouchListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ItemFragment newInstance(int columnCount) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        listView = (ListView) view.findViewById(R.id.listview);
        listView.setAdapter(new ListviewAdapter(getActivity(),DummyContent.ITEMS));
        listView.setOnTouchListener(this);
        view.findViewById(R.id.confirm_bt).setOnClickListener(this);
        view.findViewById(R.id.cancel_bt).setOnClickListener(this);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.confirm_bt:
                if (mListener!=null){
                    mListener.onListFragmentInteraction(mColumnCount,true);
                }
                break;
            case R.id.cancel_bt:
                if (mListener!=null){
                    mListener.onListFragmentInteraction(mColumnCount,false);
                }
        }
    }


    private enum DIRECTION{
        HORIZONTAL,
        VERTICAL,
        NONE
    }

    private DIRECTION mSlideDir = DIRECTION.NONE;
    private float mStartRawX,mStartRawY;
    private float mCurRawX,mCurRawY;
    private float mLastRawX,mLastRawY;
    private boolean lvScrollFirst = false;
    private boolean restart = false;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mCurRawX = event.getRawX();
        mCurRawY = event.getRawY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                restart = true;
                mStartRawX = event.getRawX();
                mStartRawY = event.getRawY();
                if ((listView.getChildCount() > 0
                        && listView.getFirstVisiblePosition() == 0
                        && listView.getChildAt(0).getTop() < 0)
                        ||(listView.getChildCount() > 0
                        && listView.getFirstVisiblePosition() > 0)){
                    lvScrollFirst = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mSlideDir==DIRECTION.NONE && (mCurRawX!=mStartRawX||mCurRawY!=mStartRawY)){
                    if (Math.abs(mCurRawX-mStartRawX) > Math.abs(mCurRawY-mStartRawY)*Math.sqrt(3)){
                        mSlideDir = DIRECTION.HORIZONTAL;
                    }else {
                        mSlideDir = DIRECTION.VERTICAL;
                    }
                }
                if (mSlideDir == DIRECTION.VERTICAL){
                    if (MainActivity.isExpand()){
                        if (lvScrollFirst && (listView.getChildCount() > 0
                                && listView.getFirstVisiblePosition() == 0
                                && listView.getChildAt(0).getTop() >= 0)){
                            lvScrollFirst = false;
                            restart = false;
                            return true;
                        }
                        if (!lvScrollFirst){
                            resetLayoutParam(v,mCurRawY-mLastRawY);
                        }
                    }else {
                        resetLayoutParam(v,mCurRawY-mLastRawY);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mSlideDir == DIRECTION.VERTICAL){
                    if (!restart){
                        restart = false;
                        break;
                    }
                    if (((mCurRawY - mStartRawY)>MainActivity.MOVE_BAUNDRY&&MainActivity.isExpand())
                            ||((mStartRawY-mCurRawY)<MainActivity.MOVE_BAUNDRY&&!MainActivity.isExpand())){
                        if (mListener!=null){
                            mListener.onListFragmentInteraction(mColumnCount,false);
                            MainActivity.setExpand(false);
                        }
                    }else if (((mCurRawY - mStartRawY)<=MainActivity.MOVE_BAUNDRY&&MainActivity.isExpand())
                            ||((mStartRawY - mCurRawY)>=MainActivity.MOVE_BAUNDRY&&!MainActivity.isExpand())){
                        if (mListener!=null){
                            mListener.onListFragmentInteraction(mColumnCount,true);
                            MainActivity.setExpand(true);
                        }
                    }
                }
                mSlideDir = DIRECTION.NONE;
                restart = false;
                break;
            default:
                break;
        }
        mLastRawX = mCurRawX;
        mLastRawY = mCurRawY;
        return false;
    }

    private void resetLayoutParam(View v, float delta) {
        FrameLayout.LayoutParams mParam = (FrameLayout.LayoutParams) v.getLayoutParams();
        mParam.topMargin += delta;
        if (mParam.topMargin > MainActivity.MARGINTOP_MAX||mParam.topMargin<MainActivity.MARGINTOP_MIN){
            return;
        }
        v.setLayoutParams(mParam);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(int position,boolean expand);
    }
}
