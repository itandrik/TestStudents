package com.students.testapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.students.testapp.model.entity.filter.CourseMarkFilter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.students.testapp.model.db.DatabaseContract.COURSE_ID_COLUMN;
import static com.students.testapp.model.db.DatabaseContract.STUDENT_HAS_COURSE_MARK_COLUMN;

/**
 * @author Andrii Chernysh.
 *         E-mail : itcherry97@gmail.com
 */
public class Utility {
    public static String formatBirthdayDate(Context context, Long birthday) {
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("dd MMMMM yyyy", Locale.getDefault());
        Date birthdayDate = new Date(birthday);
        return simpleDateFormat.format(birthdayDate);
    }

    public static String getStudentSelectionBasedOnFilters(
            CourseMarkFilter filter) {
        String result = null;
        if (filter != null) {
            result = COURSE_ID_COLUMN + " = ? AND " +
                    STUDENT_HAS_COURSE_MARK_COLUMN + " = ?";
        }
        return result;
    }

    public static String[] getStudentSelectionArgsBasedOnFilters(
            CourseMarkFilter courseFilter) {
        String[] result = null;
        if (courseFilter != null) {
            result = new String[]{String.valueOf(courseFilter.getCourseId()),
                    String.valueOf(courseFilter.getMark())};
        }
        return result;
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }
}
