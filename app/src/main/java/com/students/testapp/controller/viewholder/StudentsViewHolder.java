package com.students.testapp.controller.viewholder;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.students.testapp.R;
import com.students.testapp.controller.StudentCoursesFragment;
import com.students.testapp.model.entity.Student;
import com.students.testapp.util.Utility;

/**
 * Custom view holder for student, that is needed for
 * recycle view in the screen with content
 *
 * @author Andrii Chernysh.
 *         E-mail : itcherry97@gmail.com
 */
public class StudentsViewHolder extends RecyclerView.ViewHolder {
    private TextView studentNameTv;
    private TextView studentBirthdayTv;
    private ImageView studentInfoIv;

    public StudentsViewHolder(View itemView) {
        super(itemView);
        studentNameTv = (TextView) itemView.findViewById(R.id.student_name_tv);
        studentBirthdayTv = (TextView) itemView.findViewById(R.id.student_birthday_tv);
        studentInfoIv = (ImageView) itemView.findViewById(R.id.student_info_btn);
    }

    /**
     * Binding student instance to item in the recycler view
     * Also initializing onClickListener for "info button"
     * @param context application context
     * @param student student instance
     */
    public void bindStudent(final Context context, final Student student) {
        studentNameTv.setText(student.getFirstName() + " " + student.getLastName());
        studentBirthdayTv.setText(Utility.formatBirthdayDate(student.getBirthday()));
        studentInfoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudentCoursesFragment dialogFragment = new StudentCoursesFragment();
                Bundle fragmentBundle = new Bundle();
                fragmentBundle.putParcelable(context.getString(R.string.student_bundle_key),student);
                dialogFragment.setArguments(fragmentBundle);
                dialogFragment.show(((Activity) context).getFragmentManager(),
                        StudentCoursesFragment.FRAGMENT_TAG);
            }
        });
    }


}
