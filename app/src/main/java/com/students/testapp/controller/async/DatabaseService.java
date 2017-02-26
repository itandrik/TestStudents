package com.students.testapp.controller.async;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.students.testapp.controller.ContentFragment;
import com.students.testapp.controller.adapter.StudentsAdapter;
import com.students.testapp.model.db.StudentDatabase;
import com.students.testapp.model.entity.filter.CourseMarkFilter;

/**
 * @author Andrii Chernysh.
 *         E-mail : itcherry97@gmail.com
 */
public abstract class DatabaseService<T,E> extends AsyncTask<T,Void,E>{
    protected RecyclerView recyclerView;
    protected LinearLayoutManager layoutManager;
    protected LinearLayout emptyList;
    protected StudentsAdapter adapter;
    protected StudentDatabase database;
    protected CourseMarkFilter filter = null;
    protected Long rowsOffset;
    protected Long totalStudentsCount;
    protected Boolean isLoading;
    protected Activity activity;
    protected ContentFragment fragment;

    public DatabaseService setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        return this;
    }

    public DatabaseService setLayoutManager(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        return this;
    }

    public DatabaseService setEmptyList(LinearLayout emptyList) {
        this.emptyList = emptyList;
        return this;
    }

    public DatabaseService setAdapter(StudentsAdapter adapter) {
        this.adapter = adapter;
        return this;
    }

    public DatabaseService setDatabase(StudentDatabase database) {
        this.database = database;
        return this;
    }

    public DatabaseService setFilter(CourseMarkFilter filter) {
        this.filter = filter;
        return this;
    }

    public DatabaseService setRowsOffset(long rowsOffset) {
        this.rowsOffset = rowsOffset;
        return this;
    }

    public DatabaseService setTotalStudentsCount(long totalStudentsCount) {
        this.totalStudentsCount = totalStudentsCount;
        return this;
    }

    public DatabaseService setLoading(boolean loading) {
        isLoading = loading;
        return this;
    }

    public DatabaseService setActivity(Activity activity) {
        this.activity = activity;
        return this;
    }

    public DatabaseService setFragment(ContentFragment fragment) {
        this.fragment = fragment;
        return this;
    }

    @Override
    protected abstract void onPreExecute();

    @Override
    protected abstract E doInBackground(T... params);

    @Override
    protected abstract void onPostExecute(E e);
}
