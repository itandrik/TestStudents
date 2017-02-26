package com.students.testapp.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.students.testapp.R;
import com.students.testapp.controller.adapter.StudentsAdapter;
import com.students.testapp.controller.async.GetStudentsAsyncTask;
import com.students.testapp.controller.async.InsertStudentsAsyncTask;
import com.students.testapp.controller.exception.ControllerException;
import com.students.testapp.controller.listener.PaginationScrollListener;
import com.students.testapp.model.db.StudentDatabase;
import com.students.testapp.model.entity.Student;
import com.students.testapp.model.entity.filter.CourseMarkFilter;
import com.students.testapp.model.manager.ApiManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Andrii Chernysh.
 *         E-mail : itcherry97@gmail.com
 */
public class ContentFragment extends Fragment {
    public static final int THRESHOLD = 20;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private LinearLayout mEmptyList;
    private StudentsAdapter mAdapter;
    private StudentDatabase mDatabase;
    private CourseMarkFilter mFilter = null;
    private long rowsOffset = (long) THRESHOLD;
    private long totalStudentsCount;
    private boolean isLoading = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_main, container);
        mDatabase = new StudentDatabase(getActivity());

        initRecyclerView(v);
        processRecyclerScrolling();

        if (savedInstanceState == null) {
            fetchStudentsFromServer();
        }

        return v;
    }

    private void initRecyclerView(View v) {
        mEmptyList = (LinearLayout) v.findViewById(R.id.empty_list);
        mAdapter = new StudentsAdapter(getActivity());
        mRecyclerView = (RecyclerView) v.findViewById(R.id.students_container_recycler);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
    }

    private void processRecyclerScrolling() {
        mRecyclerView.addOnScrollListener(new PaginationScrollListener(mLayoutManager) {
            @Override
            protected void loadMoreItems() {
                getStudentsFromDbAsync();
            }

            @Override
            public long getTotalStudentCount() {
                return totalStudentsCount;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    private void fetchStudentsFromServer() {
        Call<List<Student>> call = ApiManager.getInstance().fetchStudents();
        call.enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                List<Student> studentsList = response.body();
                mAdapter.addStudents(studentsList.subList(0, THRESHOLD));
                insertDataToDatabaseAsync(studentsList);
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                throw new ControllerException()
                        .addMessage(getString(R.string.error_fetching_from_server))
                        .addLogMessage(getString(R.string.error_fetching_from_server_log)
                                + t.getLocalizedMessage())
                        .setClassThrowsException(MainActivity.class);
            }
        });
    }

    private void insertDataToDatabaseAsync(List<Student> studentsList) {
        InsertStudentsAsyncTask insertTask = new InsertStudentsAsyncTask();
        insertTask.setAdapter(mAdapter)
                .setDatabase(mDatabase)
                .setEmptyList(mEmptyList)
                .setFilter(mFilter)
                .setLayoutManager(mLayoutManager)
                .setLoading(isLoading)
                .setRecyclerView(mRecyclerView)
                .setRowsOffset(rowsOffset)
                .setActivity(getActivity())
                .setFragment(this);
        insertTask.execute(studentsList);
    }

    public void filterStudents(CourseMarkFilter filter) {
        this.mFilter = filter;
        mAdapter.clearStudents();
        rowsOffset = 0L;

        getStudentsFromDbAsync();
    }

    private void getStudentsFromDbAsync() {
        GetStudentsAsyncTask getStudentsTask = new GetStudentsAsyncTask();
        getStudentsTask.setAdapter(mAdapter)
                .setDatabase(mDatabase)
                .setFilter(mFilter)
                .setLoading(isLoading)
                .setRecyclerView(mRecyclerView)
                .setRowsOffset(rowsOffset)
                .setTotalStudentsCount(totalStudentsCount)
                .setFragment(this);

        getStudentsTask.execute();
    }

    public void setRowsOffset(Long rowsOffset) {
        this.rowsOffset = rowsOffset;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public void setTotalStudentsCount(Long totalStudentsCount) {
        this.totalStudentsCount = totalStudentsCount;
    }

    public boolean isLoading() {
        return isLoading;
    }
}
