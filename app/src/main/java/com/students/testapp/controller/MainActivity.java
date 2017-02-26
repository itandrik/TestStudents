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
import com.students.testapp.model.entity.Course;
import com.students.testapp.model.entity.filter.CourseMarkFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity that aggregates navigation drawer and
 * all methods to working with it.
 * Methods for filtering recycler view with students
 *
 * @author Andrii Chernysh.
 *         E-mail : itcherry97@gmail.com
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * Layout for navigation drawer
     */
    private DrawerLayout mDrawer;
    /**
     * Instance for working with database
     */
    private StudentDatabase mDatabase;
    private NavigationView mNavigationView;
    /**
     * Instance that contains all information,
     * needed for filter information
     */
    private CourseMarkFilter mFilter;

    /**
     * Creating main view, where contains navigation drawer and toolbar
     *
     * @param savedInstanceState state, which is saved after
     *                           rotating or something else
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = new StudentDatabase(this);
        mFilter = new CourseMarkFilter();

        initNavigationDrawer();
        initFilterButtonsListeners();

        if (savedInstanceState == null) {
            clearAllTablesInDatabase();
        } else {
            restoreCourseFilters();
        }
    }

    /**
     * Deleting all information from database
     */
    private void clearAllTablesInDatabase() {
        mDatabase.clearTable(DatabaseContract.STUDENT_TABLE_NAME);
        mDatabase.clearTable(DatabaseContract.COURSE_TABLE_NAME);
        mDatabase.clearTable(DatabaseContract.STUDENT_HAS_COURSE_TABLE_NAME);
    }

    /**
     * Instantiating navigation drawer
     */
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

    /**
     * Configuration of toolbar to set icon of
     * navigation drawer to the right side
     *
     * @return already configured toolbar
     */
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

    /**
     * Initialisation onClickListeners for buttons inside
     * navigation drawer
     */
    private void initFilterButtonsListeners() {
        View view = mNavigationView.getMenu().findItem(R.id.nav_buttons).getActionView();
        view.findViewById(R.id.clear_filter_bnt).setOnClickListener(this);
        view.findViewById(R.id.apply_filter_btn).setOnClickListener(this);
    }

    /**
     * Setting list of courses, that will be needed for
     * filtering rows inside main recycle view
     *
     * @param courses list of courses for filtering
     */
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

    /**
     * Getting list of all courses from database, that is needed
     * to restore after screen rotation or something like that
     */
    private void restoreCourseFilters() {
        List<Course> courses = mDatabase.getAllCourses();
        List<String> courseNames = new ArrayList<>();
        for (Course course : courses) {
            courseNames.add(course.getName());
        }
        setNavDrawerCourses(courseNames);
    }

    /**
     * OnClickListener for buttons within navigation drawer
     *
     * @param v button instance
     */
    @Override
    public void onClick(View v) {
        // If already loading from server or database, user can not filter information
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
            if (checkIsMarkFieldEmpty(markEt)) return;
        }

        applyFilter();
        mDrawer.closeDrawer(GravityCompat.END);
    }

    /**
     * If mark field inside navigation drawer is empty
     * user can not filter information
     *
     * @param markEt editText instance with mark
     * @return is mar field empty of not
     */
    private boolean checkIsMarkFieldEmpty(EditText markEt) {
        if (markEt.getText() == null || markEt.getText().length() == 0) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.enter_mark_filter), Toast.LENGTH_SHORT).show();
            return true;
        } else {
            mFilter.setMark(Integer.parseInt(markEt.getText().toString()));
        }
        return false;
    }

    private boolean isLoading() {
        FragmentManager fm = getSupportFragmentManager();
        ContentFragment fragment = (ContentFragment)
                fm.findFragmentById(R.id.content_fragment);
        return fragment.isLoading();
    }

    /**
     * Filter students using already initialized filter container
     */
    private void applyFilter() {
        FragmentManager fm = getSupportFragmentManager();
        ContentFragment fragment = (ContentFragment)
                fm.findFragmentById(R.id.content_fragment);
        fragment.filterStudents(mFilter);
    }
}
