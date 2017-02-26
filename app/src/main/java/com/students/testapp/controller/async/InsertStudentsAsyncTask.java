package com.students.testapp.controller.async;

import android.view.View;

import com.students.testapp.controller.MainActivity;
import com.students.testapp.model.entity.Course;
import com.students.testapp.model.entity.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrii Chernysh.
 *         E-mail : itcherry97@gmail.com
 */
public class InsertStudentsAsyncTask extends DatabaseService<List<Student>,Void>{
    @Override
    protected void onPreExecute() {
        recyclerView.setVisibility(View.GONE);
        emptyList.setVisibility(View.VISIBLE);
        fragment.setLoading(true);
    }

    @SafeVarargs
    @Override
    protected final Void doInBackground(List<Student>... studentsList) {
        for (List<Student> param : studentsList) {
            database.bulkInsertStudents(param);
            fragment.setTotalStudentsCount((long)param.size());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        recyclerView.setVisibility(View.VISIBLE);
        emptyList.setVisibility(View.GONE);
        fragment.setLoading(false);
        setCoursesToNavigationDrawer();
    }

    private void setCoursesToNavigationDrawer() {
        List<Course> courses = database.getAllCourses();
        List<String> courseNames = new ArrayList<>();
        for (Course course: courses) {
            courseNames.add(course.getName());
        }
        ((MainActivity)activity).setNavDrawerCourses(courseNames);
    }
}
