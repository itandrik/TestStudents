package com.students.testapp.controller;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.students.testapp.R;
import com.students.testapp.controller.adapter.StudentsAdapter;
import com.students.testapp.controller.exception.ControllerException;
import com.students.testapp.controller.listener.PaginationScrollListener;
import com.students.testapp.model.db.StudentDatabase;
import com.students.testapp.model.entity.Course;
import com.students.testapp.model.entity.Student;
import com.students.testapp.model.entity.filter.CourseMarkFilter;
import com.students.testapp.model.manager.ApiManager;

import java.util.ArrayList;
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
    private long rowsOffset = THRESHOLD;
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
        if(savedInstanceState == null) {
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
                isLoading = true;

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        List<Student> newStudents =
                                mDatabase.getCertainNumberOfStudents(THRESHOLD, rowsOffset, mFilter);
                        mAdapter.addStudents(newStudents);
                    }
                });

                isLoading = false;
                rowsOffset += THRESHOLD;
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
                new StudentsInsertDbTask().execute(studentsList);
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                Log.e("LOG_TAG", t.getMessage());
                throw new ControllerException()
                        .addMessage("Problems with fetching data from server")
                        .addLogMessage("Problems with fetching data from server" + t.getLocalizedMessage())
                        .setClassThrowsException(MainActivity.class);
            }
        });
    }

    public void filterStudents(CourseMarkFilter filter){
        this.mFilter = filter;
        mAdapter.clearStudents();
        rowsOffset = 0;
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                isLoading = true;
                List<Student> newStudents =
                        mDatabase.getCertainNumberOfStudents(THRESHOLD, rowsOffset, mFilter);
                mAdapter.addStudents(newStudents);
                rowsOffset += THRESHOLD;
                isLoading = false;
            }
        });
    }

    public boolean isLoading(){
        return isLoading;
    }

    private class StudentsInsertDbTask extends AsyncTask<List<Student>, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mRecyclerView.setVisibility(View.GONE);
            mEmptyList.setVisibility(View.VISIBLE);
            isLoading = true;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<Student>... studentsList) {
            for (List<Student> param : studentsList) {
                mDatabase.bulkInsertStudents(param);
                totalStudentsCount = param.size();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyList.setVisibility(View.GONE);
            isLoading = false;
            mDatabase.selectCountOfStudents();
            setCoursesToNavigationDrawer();
        }

        private void setCoursesToNavigationDrawer() {
            List<Course> courses = mDatabase.getAllCourses();
            List<String> courseNames = new ArrayList<>();
            for (Course course: courses) {
                courseNames.add(course.getName());
            }
            ((MainActivity)getActivity()).setNavDrawerCourses(courseNames);
        }
    }
}
