package com.students.testapp.controller.listener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Listener for pagination of main list with students
 *
 * @author Andrii Chernysh.
 *         E-mail : itcherry97@gmail.com
 */
public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {
    private LinearLayoutManager mLayoutManager;
    /**
     * New data will load when the screen will
     * show (e.g) 20 - 5 items. This constant shows
     * how early will start loading from database
     */
    private static final int LOADING_THRESHOLD = 5;

    public PaginationScrollListener(LinearLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

    /**
     * Calculating quantity of items on the screen.
     * Taking the moment when to load students from database
     *
     * @param recyclerView
     * @param dx
     * @param dy
     */
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = mLayoutManager.getChildCount();
        int totalItemCount = mLayoutManager.getItemCount();
        int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

        if (!isLoading()) {
            if (((visibleItemCount + firstVisibleItemPosition) >=
                    totalItemCount - LOADING_THRESHOLD)
                    && (firstVisibleItemPosition >= 0)
                    && (totalItemCount <= getTotalStudentCount())) {
                loadMoreItems();
            }
        }
    }

    /**
     * Loading from database
     */
    protected abstract void loadMoreItems();

    /**
     * @return total students count in the database
     */
    public abstract long getTotalStudentCount();

    /**
     * @return boolean variable is already loading
     * from database or server
     */
    public abstract boolean isLoading();
}
