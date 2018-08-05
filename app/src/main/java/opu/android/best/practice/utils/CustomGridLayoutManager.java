package opu.android.best.practice.utils;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

import android.support.v7.widget.RecyclerView;

public class CustomGridLayoutManager extends GridLayoutManager {


    public CustomGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (ArrayIndexOutOfBoundsException ex) {

        }

    }
}
