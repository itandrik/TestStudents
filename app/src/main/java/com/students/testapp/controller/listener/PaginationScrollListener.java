package com.students.testapp.controller.listener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * @author Andrii Chernysh.
 *         E-mail : itcherry97@gmail.com
 */
public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {
    private LinearLayoutManager mLayoutManager;
    private static final int LOADING_THRESHOLD = 5;

    public PaginationScrollListener(LinearLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = mLayoutManager.getChildCount();
        int totalItemCount = mLayoutManager.getItemCount();
        int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();
        Log.d("LOG_TAG", "visibleItemCount" + visibleItemCount + ", totalItemCount : " +
                totalItemCount + " firstVisible : " + firstVisibleItemPosition);
        if (!isLoading()) {
            if (((visibleItemCount + firstVisibleItemPosition) >=
                    totalItemCount - LOADING_THRESHOLD)
                    && (firstVisibleItemPosition >= 0)
                    && (totalItemCount <= getTotalStudentCount())) {
                loadMoreItems();
            }
        }
    }

    protected abstract void loadMoreItems();

    public abstract long getTotalStudentCount();

    public abstract boolean isLoading();
}
