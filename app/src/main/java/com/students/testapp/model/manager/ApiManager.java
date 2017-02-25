package com.students.testapp.model.manager;

import com.students.testapp.model.entity.Student;
import com.students.testapp.model.service.RestfulWebService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Manager to perform all REST-related work.
 *
 * @author Andrii Chernysh.
 *         E-mail : itcherry97@gmail.com
 */
public class ApiManager {
    private Retrofit mClient;
    private RestfulWebService mService;

    private static final class Holder{
        static final ApiManager INSTANCE = new ApiManager();
    }

    private ApiManager() {
        initRetrofit();
        initService();
    }

    public static ApiManager getInstance(){
        return Holder.INSTANCE;
    }

    /**
     * Initialises Retrofit with a BASE_URL and GSON converter.
     */
    private void initRetrofit() {

        final String BASE_URL = "https://ddapp-sfa-api-dev.azurewebsites.net/api/";

        mClient = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void initService() {
        mService = mClient.create(RestfulWebService.class);
    }

    public Call<List<Student>> fetchStudents() {
        return mService.getStudents();
    }
}
