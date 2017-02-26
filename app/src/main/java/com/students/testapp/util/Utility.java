package com.students.testapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.students.testapp.model.entity.filter.CourseMarkFilter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.students.testapp.model.db.DatabaseContract.COURSE_NAME_COLUMN;
import static com.students.testapp.model.db.DatabaseContract.STUDENT_HAS_COURSE_MARK_COLUMN;

/**
 * Some util methods for application
 *
 * @author Andrii Chernysh.
 *         E-mail : itcherry97@gmail.com
 */
public class Utility {
    /**
     * Formatting timestamp date to pretty UI view
     * @param birthday timestamp date(long value)
     * @return date of birth in pretty way
     */
    public static String formatBirthdayDate(Long birthday) {
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        Date birthdayDate = new Date(birthday);
        return simpleDateFormat.format(birthdayDate);
    }

    public static String getStudentSelectionBasedOnFilters(
            CourseMarkFilter filter) {
        String result = null;
        if (filter != null) {
            result = COURSE_NAME_COLUMN + " = ? AND " +
                    STUDENT_HAS_COURSE_MARK_COLUMN + " = ?";
        }
        return result;
    }

    public static String[] getStudentSelectionArgsBasedOnFilters(
            CourseMarkFilter courseFilter) {
        String[] result = null;
        if (courseFilter != null) {
            result = new String[]{String.valueOf(courseFilter.getCourseName()),
                    String.valueOf(courseFilter.getMark())};
        }
        return result;
    }

    /**
     * Util method that is checking is device connected to network or not
     *
     * @param context application context
     * @return boolean value is device connected to network
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }
}
