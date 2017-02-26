package com.students.testapp.controller.adapter;

import android.content.Context;
import android.os.Handler;
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

    public StudentsAdapter(Context context) {
        this.mContext = context;
        mStudents = new ArrayList<>();
    }

    @Override
    public StudentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_recycler_item, parent, false);
        return new StudentsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StudentsViewHolder holder, int position) {
        Student student = mStudents.get(position);
        holder.bindStudent(mContext, student);
    }

    @Override
    public int getItemCount() {
        return mStudents == null ? 0 : mStudents.size();
    }

    public void addStudents(final List<Student> students) {
        mStudents.addAll(students);

        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                notifyItemRangeInserted(mStudents.size(),
                        mStudents.size() + students.size() - 1);
            }
        };
        handler.post(r);
    }

    public boolean isEmpty() {
        return mStudents.isEmpty();
    }

    public void clearStudents() {
        mStudents.clear();
        notifyDataSetChanged();
    }
}
