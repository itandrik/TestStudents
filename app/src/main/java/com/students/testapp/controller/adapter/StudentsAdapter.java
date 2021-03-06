package com.students.testapp.controller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.students.testapp.R;
import com.students.testapp.controller.viewholder.StudentsViewHolder;
import com.students.testapp.model.entity.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrii Chernysh.
 *         E-mail : itcherry97@gmail.com
 */
public class StudentsAdapter extends RecyclerView.Adapter<StudentsViewHolder> {
    private List<Student> mStudents;
    private Context mContext;

    /**
     * Initializing context and students
     *
     * @param context context
     */
    public StudentsAdapter(Context context) {
        this.mContext = context;
        mStudents = new ArrayList<>();
    }

    /**
     * Inflating list item view and returning instance
     * of StudentViewHolder
     *
     * @param parent - parent view
     * @param viewType - parent type
     * @return ViewHolder with students
     */
    @Override
    public StudentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_recycler_item, parent, false);
        return new StudentsViewHolder(itemView);
    }

    /**
     * Binding student information with ViewHolder
     *
     * @param holder custom holder
     * @param position current position of item
     */
    @Override
    public void onBindViewHolder(StudentsViewHolder holder, int position) {
        Student student = mStudents.get(position);
        holder.bindStudent(mContext, student);
    }

    @Override
    public int getItemCount() {
        return mStudents == null ? 0 : mStudents.size();
    }

    /**
     * Adding list of students to inner ArrayList
     *
     * @param students list with student information
     */
    public void addStudents(final List<Student> students) {
        mStudents.addAll(students);
        notifyItemRangeInserted(mStudents.size(),
                mStudents.size() + students.size() - 1);
    }

    /**
     * @return is empty inner list with students
     */
    public boolean isEmpty() {
        return mStudents.isEmpty();
    }

    /**
     * Remove all records from inner list with students
     */
    public void clearStudents() {
        mStudents.clear();
        notifyDataSetChanged();
    }

    /**
     * @return inner ArrayList with students
     */
    public List<Student> getAll() {
        return mStudents;
    }
}
