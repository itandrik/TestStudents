package com.students.testapp.controller.async;

import android.view.View;

import com.students.testapp.R;
import com.students.testapp.model.entity.Student;

import java.util.List;

import static com.students.testapp.controller.ContentFragment.THRESHOLD;

/**
 * Selecting data from database asynchronously
 *
 * @author Andrii Chernysh.
 *         E-mail : itcherry97@gmail.com
 */
public class GetStudentsAsyncTask extends DatabaseService<Void, List<Student>> {
    /**
     * Prepare UI before asynchronous task
     */
    @Override
    protected void onPreExecute() {
        fragment.setLoading(true);
    }

    /**
     * Selecting students from database asynchronously and
     * setting rows offset value
     *
     * @param params parameters to do smth with
     * @return list with students
     */
    @Override
    protected List<Student> doInBackground(Void... params) {
        List<Student> students = database
                .getCertainNumberOfStudents(THRESHOLD, rowsOffset, filter);

        rowsOffset += THRESHOLD;
        fragment.setRowsOffset(rowsOffset);
        return students;
    }

    /**
     * If there are no records, show special view
     *
     * @param students list with students
     */
    @Override
    protected void onPostExecute(List<Student> students) {
        adapter.addStudents(students);
        if (adapter.isEmpty()) {
            fragment.getView().findViewById(R.id.empty_list_container).setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            fragment.getView().findViewById(R.id.empty_list_container).setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        fragment.setLoading(false);
    }
}
