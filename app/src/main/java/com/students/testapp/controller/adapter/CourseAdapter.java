package com.students.testapp.controller.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.students.testapp.R;
import com.students.testapp.controller.viewholder.CoursesViewHolder;
import com.students.testapp.model.entity.Course;

import java.util.List;

/**
 * Custom adapter, that perform all work with main
 * RecycleView. Class bind view holder with student
 * information.
 *
 * @author Andrii Chernysh.
 *         E-mail : itcherry97@gmail.com
 */
public class CourseAdapter extends RecyclerView.Adapter<CoursesViewHolder> {
    private List<Course> mCourses;

    /**
     * @param courses - courses from database
     */
    public CourseAdapter(List<Course> courses) {
        this.mCourses = courses;
    }

    /**
     * Inflating list item view and returning instance
     * of CourseViewHolder
     *
     * @param parent - parent view
     * @param viewType - parent type
     * @return ViewHolder with courses
     */
    @Override
    public CoursesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_recycler_item, parent, false);
        return new CoursesViewHolder(itemView);
    }

    /**
     * Binding course information with ViewHolder
     *
     * @param holder custom holder
     * @param position current position of item
     */
    @Override
    public void onBindViewHolder(CoursesViewHolder holder, int position) {
        holder.setCourseName(mCourses.get(position).getName());
        holder.setMark(mCourses.get(position).getMark());
    }

    @Override
    public int getItemCount() {
        return mCourses == null ? 0 : mCourses.size();
    }
}
