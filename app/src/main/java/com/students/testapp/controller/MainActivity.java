package com.students.testapp.controller;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.students.testapp.R;
import com.students.testapp.controller.adapter.StudentsAdapter;
import com.students.testapp.controller.exception.ControllerException;
import com.students.testapp.controller.listener.PaginationScrollListener;
import com.students.testapp.model.db.DatabaseContract;
import com.students.testapp.model.db.StudentDatabase;
import com.students.testapp.model.entity.Student;
import com.students.testapp.model.manager.ApiManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Andrii Chernysh.
 *         E-mail : itcherry97@gmail.com
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawer;
    private RecyclerView mRecyclerView;
    private LinearLayout mEmptyList;
    private StudentsAdapter mAdapter;
    private StudentDatabase mDatabase;
    public static final int THRESHOLD = 20;
    private long rowsOffset = 0;
    private long totalStudentsCount;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = new StudentDatabase(this);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = getConfiguredToolbar();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);

        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /* Recycler view initialization*/
        mEmptyList = (LinearLayout) findViewById(R.id.empty_list);
        mAdapter = new StudentsAdapter(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.students_container_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new PaginationScrollListener(
                (LinearLayoutManager) layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                List<Student> newStudents =
                        mDatabase.getCertainNumberOfStudents(THRESHOLD, rowsOffset, null);
                isLoading = false;
                mAdapter.addStudents(newStudents);
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
        fetchStudentsFromServer();
    }

    private void fetchStudentsFromServer(){
        Call<List<Student>> call = ApiManager.getInstance().fetchStudents();
        call.enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                List<Student> studentsList = response.body();
                mAdapter.addStudents(studentsList.subList(0,THRESHOLD));
                new FetchStudentsTask().execute(studentsList);
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

    private Toolbar getConfiguredToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView filterIv = (ImageView) toolbar.findViewById(R.id.ivCustomDrawable);
        filterIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.openDrawer(GravityCompat.END);
            }
        });

        return toolbar;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /*if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabase.clearTable(DatabaseContract.STUDENT_TABLE_NAME);
        mDatabase.clearTable(DatabaseContract.COURSE_TABLE_NAME);
        mDatabase.clearTable(DatabaseContract.STUDENT_HAS_COURSE_TABLE_NAME);
        mDatabase.closeDatabaseConnection();
    }

    private class FetchStudentsTask extends AsyncTask<List<Student>, Void, Void> {
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
        }
    }
}
