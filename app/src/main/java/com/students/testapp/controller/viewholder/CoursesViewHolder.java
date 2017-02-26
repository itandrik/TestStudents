package com.students.testapp.controller.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.students.testapp.R;

/**
 * Custom view holder for course, that is needed for
 * recycle view in the screen with content
 *
 * @author Andrii Chernysh.
 *         E-mail : itcherry97@gmail.com
 */
public class CoursesViewHolder extends RecyclerView.ViewHolder {
    private TextView mCourseNameTv;
    private TextView mMarkTv;

    public CoursesViewHolder(View itemView) {
        super(itemView);
        mCourseNameTv = (TextView) itemView.findViewById(R.id.course_name_tv);
        mMarkTv = (TextView) itemView.findViewById(R.id.course_mark_tv);
    }

    public void setCourseName(String courseName){
        mCourseNameTv.setText(courseName);
    }

    public void setMark(int mark){
        mMarkTv.setText(String.valueOf(mark));
    }
}
