package com.students.testapp.model.service;

import com.students.testapp.model.entity.Student;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author Andrii Chernysh.
 *         E-mail : itcherry97@gmail.com
 */
public interface RestfulWebService {
    @GET("test/students")
    Call<List<Student>> getStudents();
}
