package com.students.testapp.controller.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.students.testapp.R;
import com.students.testapp.model.entity.Student;
import com.students.testapp.util.Utility;

/**
 * @author Andrii Chernysh.
 *         E-mail : itcherry97@gmail.com
 */
public class StudentsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView studentNameTv;
    private TextView studentBirthdayTv;
    private ImageView studentInfoIv;
    private Student student;

    public StudentsViewHolder(View itemView) {
        super(itemView);
        studentNameTv = (TextView) itemView.findViewById(R.id.student_name_tv);
        studentBirthdayTv = (TextView) itemView.findViewById(R.id.student_birthday_tv);
        studentInfoIv = (ImageView) itemView.findViewById(R.id.student_info_btn);
        studentInfoIv.setOnClickListener(this);
    }

    public void bindStudent(Context context, Student student) {
        this.student = student;
        studentNameTv.setText(student.getFirstName() + " " + student.getLastName());
        studentBirthdayTv.setText(Utility.formatBirthdayDate(context,student.getBirthday()));
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.student_info_btn){
            //TODO show dialog fragment
            Log.d("LOG_TAG", "LAter here will be dialog fragment");
        }
    }
}
