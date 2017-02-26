package com.students.testapp.controller;

import android.os.Bundle;
import android.os.Parcelable;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragment, that aggregates main content (students
 * from database). It controls fetching data from server,
 * loading it to the database and show all data in the
 * recycle view.
 *
 * @author Andrii Chernysh.
 *         E-mail : itcherry97@gmail.com
 */
public class ContentFragment extends Fragment {
    /**
     * Quantity of rows, that will load from database
     */
    public static final int THRESHOLD = 20;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private LinearLayout mEmptyList;
    private StudentsAdapter mAdapter;
    private StudentDatabase mDatabase;
    private CourseMarkFilter mFilter = null;
    /**
     * Offset variable, that takes number of last shown student
     */
    private long rowsOffset = (long) THRESHOLD;
    /**
     * Total quantity of students in the database
     */
    private long totalStudentsCount;
    private boolean isLoading = false;

    /**
     * Creating view after instantiating main activity
     *
     * @param inflater inflater instance
     * @param container parent view group
     * @param savedInstanceState state, which is saved after
     *                           rotating or something else
     * @return inflated view instance
     */
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
        } else {
            restoreApplicationParametersFromBundle(savedInstanceState);
        }

        return v;
    }

    /**
     *
     * @param outState bundle, which aggregates all
     *                 necessary data to save
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(getString(R.string.totalStudentsCount_key), totalStudentsCount);
        outState.putLong(getString(R.string.rowsOffset_Key), rowsOffset);
        outState.putBoolean(getString(R.string.isLoading_Key), isLoading);
        outState.putParcelableArrayList(getString(R.string.students_key),
                (ArrayList<? extends Parcelable>) mAdapter.getAll());

    }

    /**
     * Initialisation recycler view.
     * Creating student adapter, layout manager and animator
     *
     * @param v inflated view instance
     */
    private void initRecyclerView(View v) {
        mEmptyList = (LinearLayout) v.findViewById(R.id.empty_list);
        mAdapter = new StudentsAdapter(getActivity());
        mRecyclerView = (RecyclerView) v.findViewById(R.id.students_container_recycler);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * Setting scroll listener for recycler view
     */
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

    /**
     * Performing fetching data from server using API with Retrofit
     */
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

    /**
     * Inserting data to database asynchronously
     *
     * @param studentsList list of students from server
     */
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

    /**
     * Getting students from database with filters from navigation drawer
     * and setting them to the adapter.
     *
     * @param filter instance of class, that contains filter information
     */
    public void filterStudents(CourseMarkFilter filter) {
        this.mFilter = filter;
        mAdapter.clearStudents();
        rowsOffset = 0L;

        getStudentsFromDbAsync();
    }

    /**
     * Selecting data from the database asynchronously
     */
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

    /**
     * Restoring data from saved instance state
     *
     * @param savedInstanceState state, which is saved after
     *                           rotating or something else
     */
    private void restoreApplicationParametersFromBundle(@Nullable Bundle savedInstanceState) {
        totalStudentsCount = savedInstanceState
                .getLong(getString(R.string.totalStudentsCount_key));
        rowsOffset = savedInstanceState
                .getLong(getString(R.string.rowsOffset_Key));
        isLoading = savedInstanceState
                .getBoolean(getString(R.string.isLoading_Key));
        ArrayList<Student> students = savedInstanceState.getParcelableArrayList(
                getString(R.string.students_key));
        mAdapter.addStudents(students);
    }
}
