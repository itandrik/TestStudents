package com.students.testapp.controller;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.students.testapp.R;
import com.students.testapp.controller.adapter.CourseAdapter;
import com.students.testapp.model.entity.Course;
import com.students.testapp.model.entity.Student;

import java.util.List;

/**
 * @author Andrii Chernysh.
 *         E-mail : itcherry97@gmail.com
 */
public class StudentCoursesFragment extends DialogFragment implements View.OnClickListener {
    public static final String FRAGMENT_TAG = "courses_dialog";
    private EditText mAverageMarkEt;
    private RecyclerView mCoursesRecycler;
    private Student mStudent;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.student_info_fragment, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        mAverageMarkEt = (EditText) v.findViewById(R.id.average_mark_et);
        v.findViewById(R.id.ok_btn).setOnClickListener(this);
        getStudentFromArguments();

        initRecyclerView(v);
        initAverageMark();
        return v;
    }

    private void initAverageMark() {
        List<Course> courses = mStudent.getCourses();
        double averageMark = 0;
        for (Course course : courses) {
            averageMark += course.getMark();
        }
        averageMark /= courses.size();
        mAverageMarkEt.setText(String.valueOf(averageMark));
    }

    private void getStudentFromArguments() {
        mStudent = getArguments().getParcelable(getString(R.string.student_bundle_key));
    }

    private void initRecyclerView(View v) {
        mCoursesRecycler = (RecyclerView) v.findViewById(R.id.courses_container_recycler);
        CourseAdapter adapter = new CourseAdapter(mStudent.getCourses());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mCoursesRecycler.setLayoutManager(layoutManager);
        mCoursesRecycler.setItemAnimator(new DefaultItemAnimator());
        mCoursesRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;

        Window window = getDialog().getWindow();
        window.setLayout(width - 70, height - 100);
        window.setGravity(Gravity.CENTER);
    }
}
