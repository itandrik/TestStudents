package com.students.testapp.controller;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.students.testapp.controller.adapter.StudentsAdapter;
import com.students.testapp.controller.exception.ControllerException;
import com.students.testapp.model.db.StudentDatabase;
import com.students.testapp.model.entity.Student;
import com.students.testapp.model.manager.ApiManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.students.testapp.controller.MainActivity.THRESHOLD;

/**
 * @author Andrii Chernysh.
 *         E-mail : itcherry97@gmail.com
 */
public class FetchStudentsTask extends AsyncTask<Void, Void, Void> {
    private RecyclerView mRecyclerView;
    private LinearLayout mEmptyList;
    private StudentsAdapter mAdapter;
    private StudentDatabase mDatabase;

    public FetchStudentsTask(RecyclerView recyclerView, LinearLayout emptyList,
                             StudentsAdapter adapter, StudentDatabase database) {
        mRecyclerView = recyclerView;
        mEmptyList = emptyList;
        mAdapter = adapter;
        mDatabase = database;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mRecyclerView.setVisibility(View.GONE);
        mEmptyList.setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(Void... params) {
        Call<List<Student>> call = ApiManager.getInstance().fetchStudents();
        call.enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                List<Student> students = response.body();
                mDatabase.bulkInsertStudents(students);
                mAdapter.addStudents(students.subList(0, THRESHOLD));
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
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mRecyclerView.setVisibility(View.VISIBLE);
        mEmptyList.setVisibility(View.GONE);
    }
}
