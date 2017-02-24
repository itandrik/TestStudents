package com.students.testapp.model.db;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.students.testapp.R;
import com.students.testapp.exception.ApplicationException;
import com.students.testapp.model.entity.Course;
import com.students.testapp.model.entity.Student;
import com.students.testapp.model.exception.DatabaseException;

import java.util.ArrayList;
import java.util.List;

import static com.students.testapp.model.db.DatabaseContract.COURSE_ID_COLUMN;
import static com.students.testapp.model.db.DatabaseContract.COURSE_NAME_COLUMN;
import static com.students.testapp.model.db.DatabaseContract.COURSE_TABLE_NAME;
import static com.students.testapp.model.db.DatabaseContract.STUDENT_BIRTHDAY_COLUMN;
import static com.students.testapp.model.db.DatabaseContract.STUDENT_FIRST_NAME_COLUMN;
import static com.students.testapp.model.db.DatabaseContract.STUDENT_HAS_COURSE_MARK_COLUMN;
import static com.students.testapp.model.db.DatabaseContract.STUDENT_HAS_COURSE_TABLE_NAME;
import static com.students.testapp.model.db.DatabaseContract.STUDENT_ID_COLUMN;
import static com.students.testapp.model.db.DatabaseContract.STUDENT_LAST_NAME_COLUMN;
import static com.students.testapp.model.db.DatabaseContract.STUDENT_TABLE_NAME;
import static com.students.testapp.model.db.DatabaseContract.STUDENT_TOKEN_ID_COLUMN;

/**
 * All working with database is implemented here.
 */
public class StudentDatabase {
    private DatabaseHelper mDbHelper;
    private Context mContext;

    public static final String SELECT_ALL_COURSES_FOR_PERSON =
            "SELECT " + COURSE_ID_COLUMN + "," + COURSE_NAME_COLUMN + "," +
                    STUDENT_HAS_COURSE_MARK_COLUMN +
                    " FROM " + COURSE_TABLE_NAME +
                    " INNER JOIN " + STUDENT_HAS_COURSE_TABLE_NAME +
                    " USING(" + COURSE_ID_COLUMN + ")" +
                    " WHERE " + STUDENT_ID_COLUMN + " = ?";

    public StudentDatabase(Context context) {
        mDbHelper = new DatabaseHelper(context);
        mContext = context;
    }

    public List<Student> getCertainNumberOfStudents(int limit, int offset) throws DatabaseException {
        String[] columns = new String[]{STUDENT_ID_COLUMN, STUDENT_FIRST_NAME_COLUMN,
                STUDENT_LAST_NAME_COLUMN, STUDENT_TOKEN_ID_COLUMN, STUDENT_BIRTHDAY_COLUMN};
        List<Student> students = new ArrayList<>();

        try (SQLiteDatabase database = mDbHelper.getReadableDatabase();
             Cursor retCursor = database.query(
                     STUDENT_TABLE_NAME,
                     columns,
                     null, null, null, null,
                     "ASC", offset + "," + limit)) {

            retCursor.moveToFirst();
            do {
                Student student = extractStudentFromCursor(retCursor);
                students.add(student);
            } while (retCursor.moveToNext());
        } catch (NullPointerException | CursorIndexOutOfBoundsException | SQLException e) {
            throw createException(mContext.getString(R.string.error_getting_persons_log));
        }
        return students;
    }

    private Student extractStudentFromCursor(Cursor retCursor) {
        int studentIdColIndex = retCursor.getColumnIndex(STUDENT_ID_COLUMN);
        int studentTokenIdColIndex = retCursor.getColumnIndex(STUDENT_TOKEN_ID_COLUMN);
        int firstNameColIndex = retCursor.getColumnIndex(STUDENT_FIRST_NAME_COLUMN);
        int lastNameColIndex = retCursor.getColumnIndex(STUDENT_LAST_NAME_COLUMN);
        int birthdayColIndex = retCursor.getColumnIndex(STUDENT_BIRTHDAY_COLUMN);

        return new Student.Builder()
                .setStudentId(retCursor.getInt(studentIdColIndex))
                .setStudentTokenId(retCursor.getString(studentTokenIdColIndex))
                .setFirstName(retCursor.getString(firstNameColIndex))
                .setLastName(retCursor.getString(lastNameColIndex))
                .setBirthday(retCursor.getLong(birthdayColIndex))
                .build();
    }

    private ApplicationException createException(String logMessage) {
        throw new DatabaseException()
                .addMessage(mContext.getString(R.string.error_database))
                .addLogMessage(logMessage)
                .setClassThrowsException(StudentDatabase.class);
    }

    public Student getStudentById(int studentId) throws DatabaseException {
        String[] columns = new String[]{STUDENT_ID_COLUMN, STUDENT_FIRST_NAME_COLUMN,
                STUDENT_LAST_NAME_COLUMN, STUDENT_TOKEN_ID_COLUMN, STUDENT_BIRTHDAY_COLUMN};
        String selection = STUDENT_ID_COLUMN + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(studentId)};

        try (SQLiteDatabase database = mDbHelper.getReadableDatabase();
             Cursor retCursor = database.query(STUDENT_TABLE_NAME,
                     columns, selection, selectionArgs, null, null, null)) {

            retCursor.moveToFirst();
            return extractStudentFromCursor(retCursor);
        } catch (NullPointerException | CursorIndexOutOfBoundsException | SQLException e) {
            throw createException(mContext.getString(R.string.error_getting_concrete_person_log));
        }
    }

    public List<Course> getCoursesForStudent(int studentId) throws DatabaseException {
        List<Course> courses = new ArrayList<>();
        String[] selectionArgs = new String[]{String.valueOf(studentId)};
        try (SQLiteDatabase database = mDbHelper.getReadableDatabase();
             Cursor retCursor = database.rawQuery(
                     SELECT_ALL_COURSES_FOR_PERSON, selectionArgs)) {

            retCursor.moveToFirst();
            do {
                Course course = extractCourseFromCursor(retCursor);
                courses.add(course);
            } while (retCursor.moveToNext());
        } catch (NullPointerException | CursorIndexOutOfBoundsException | SQLException e) {
            throw createException(mContext.getString(
                    R.string.error_getting_courses_for_person_log));
        }
        return courses;
    }

    private Course extractCourseFromCursor(Cursor cursor) {
        int courseIdColIndex = cursor.getColumnIndex(COURSE_ID_COLUMN);
        int nameIdColIndex = cursor.getColumnIndex(COURSE_NAME_COLUMN);
        int markColIndex = cursor.getColumnIndex(STUDENT_HAS_COURSE_MARK_COLUMN);

        return new Course.Builder()
                .setCourseId(cursor.getInt(courseIdColIndex))
                .setName(cursor.getString(nameIdColIndex))
                .setMark(cursor.getInt(markColIndex))
                .build();
    }

    public int insertStudent(Student student) {
        int studentId = 0;
        try (SQLiteDatabase database = mDbHelper.getReadableDatabase()) {
            //TODO finish him
        } catch (SQLException e) {
            throw createException(mContext.getString(
                    R.string.error_getting_courses_for_person_log));
        }
        return studentId;
    }

    public void insertStudentsCourse(Course course) {

    }

    private void assignCourseToStudentWithMark(int studentId,
                                               int courseId, int mark) {

    }

    public void closeDatabaseConnection() {
        mDbHelper.close();
    }
}
