package com.students.testapp.controller;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.students.testapp.R;
import com.students.testapp.model.db.DatabaseContract;
import com.students.testapp.model.db.StudentDatabase;
import com.students.testapp.model.entity.filter.CourseMarkFilter;

import java.util.List;

/**
 * @author Andrii Chernysh.
 *         E-mail : itcherry97@gmail.com
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private DrawerLayout mDrawer;
    private StudentDatabase mDatabase;
    private NavigationView mNavigationView;
    private CourseMarkFilter mFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = new StudentDatabase(this);
        mFilter = new CourseMarkFilter();

        if (savedInstanceState == null) {
            clearAllTablesInDatabase();
        }

        initNavigationDrawer();

        initFilterButtonsListeners();
    }

    private void clearAllTablesInDatabase() {
        mDatabase.clearTable(DatabaseContract.STUDENT_TABLE_NAME);
        mDatabase.clearTable(DatabaseContract.COURSE_TABLE_NAME);
        mDatabase.clearTable(DatabaseContract.STUDENT_HAS_COURSE_TABLE_NAME);
    }

    private void initNavigationDrawer() {
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = getConfiguredToolbar();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);

        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
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

    private void initFilterButtonsListeners() {
        View view = mNavigationView.getMenu().findItem(R.id.nav_buttons).getActionView();
        view.findViewById(R.id.clear_filter_bnt).setOnClickListener(this);
        view.findViewById(R.id.apply_filter_btn).setOnClickListener(this);
    }

    public void setNavDrawerCourses(List<String> courses) {
        Spinner spinner = (Spinner) mNavigationView.getMenu()
                .findItem(R.id.course_filter_menu_item).getActionView();
        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, courses));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mFilter.setCourseName(((TextView) view).getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabase.closeDatabaseConnection();
    }

    @Override
    public void onClick(View v) {
        if (isLoading()) {
            Toast.makeText(getApplicationContext(), getString(R.string.is_loading), Toast.LENGTH_SHORT).show();
            return;
        }

        if (v.getId() == R.id.clear_filter_bnt) {
            mFilter = null;
        } else if (v.getId() == R.id.apply_filter_btn) {
            EditText markEt = (EditText) mNavigationView.getMenu()
                    .findItem(R.id.nav_mark).getActionView()
                    .findViewById(R.id.mark_filter_et);
            if (markEt.getText() == null || markEt.getText().length() == 0) {
                Toast.makeText(getApplicationContext(), getString(R.string.enter_mark_filter), Toast.LENGTH_SHORT).show();
                return;
            } else {
                mFilter.setMark(Integer.parseInt(markEt.getText().toString()));
            }
        }

        applyFilter();
        mDrawer.closeDrawer(GravityCompat.END);
    }

    private boolean isLoading() {
        FragmentManager fm = getSupportFragmentManager();
        ContentFragment fragment = (ContentFragment) fm.findFragmentById(R.id.content_fragment);
        return fragment.isLoading();
    }

    private void applyFilter() {
        FragmentManager fm = getSupportFragmentManager();
        ContentFragment fragment = (ContentFragment) fm.findFragmentById(R.id.content_fragment);
        fragment.filterStudents(mFilter);
    }
}
