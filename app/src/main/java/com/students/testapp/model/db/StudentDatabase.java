package com.students.testapp.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.students.testapp.R;
import com.students.testapp.exception.ApplicationException;
import com.students.testapp.model.entity.Course;
import com.students.testapp.model.entity.Student;
import com.students.testapp.model.entity.filter.CourseMarkFilter;
import com.students.testapp.model.exception.DatabaseException;
import com.students.testapp.util.Utility;

import java.util.ArrayList;
import java.util.List;

import static com.students.testapp.model.db.DatabaseContract.COURSE_ID_COLUMN;
import static com.students.testapp.model.db.DatabaseContract.COURSE_NAME_COLUMN;
import static com.students.testapp.model.db.DatabaseContract.COURSE_TABLE_NAME;
import static com.students.testapp.model.db.DatabaseContract.STUDENT_BIRTHDAY_COLUMN;
import static com.students.testapp.model.db.DatabaseContract.STUDENT_FIRST_NAME_COLUMN;
import static com.students.testapp.model.db.DatabaseContract.STUDENT_HAS_COURSE_COURSE_ID;
import static com.students.testapp.model.db.DatabaseContract.STUDENT_HAS_COURSE_MARK_COLUMN;
import static com.students.testapp.model.db.DatabaseContract.STUDENT_HAS_COURSE_STUDENT_ID;
import static com.students.testapp.model.db.DatabaseContract.STUDENT_HAS_COURSE_TABLE_NAME;
import static com.students.testapp.model.db.DatabaseContract.STUDENT_ID_COLUMN;
import static com.students.testapp.model.db.DatabaseContract.STUDENT_LAST_NAME_COLUMN;
import static com.students.testapp.model.db.DatabaseContract.STUDENT_TABLE_NAME;
import static com.students.testapp.model.db.DatabaseContract.STUDENT_TOKEN_ID_COLUMN;

/**
 * All working with database is implemented here.
 */
public class StudentDatabase {
    private SQLiteDatabase database;
    private Context mContext;
    private static SQLiteQueryBuilder sQueryJoinStudentCourse;

    public static final String SELECT_ALL_COURSES_FOR_PERSON =
            "SELECT " + COURSE_ID_COLUMN + "," + COURSE_NAME_COLUMN + "," +
                    STUDENT_HAS_COURSE_MARK_COLUMN +
                    " FROM " + COURSE_TABLE_NAME +
                    " INNER JOIN " + STUDENT_HAS_COURSE_TABLE_NAME +
                    " USING(" + COURSE_ID_COLUMN + ")" +
                    " WHERE " + STUDENT_ID_COLUMN + " = ?";
    public static final String SELECT_ALL_COURSES =
            "SELECT " + COURSE_ID_COLUMN + "," + COURSE_NAME_COLUMN +
                    " FROM " + COURSE_TABLE_NAME;

    static {
        sQueryJoinStudentCourse = new SQLiteQueryBuilder();
        sQueryJoinStudentCourse.setTables(
                STUDENT_TABLE_NAME + " LEFT JOIN "
                        + STUDENT_HAS_COURSE_TABLE_NAME +
                        " USING(" + STUDENT_ID_COLUMN + ")");
    }

    public StudentDatabase(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        mContext = context;
    }

    public StudentDatabase(Context mContext, SQLiteOpenHelper mDbHelper) {
        //this.mDbHelper = mDbHelper;
        this.mContext = mContext;
    }

    public List<Student> getCertainNumberOfStudents(long limit, long offset,
                                                    CourseMarkFilter filter)
            throws DatabaseException {
        String[] columns = new String[]{STUDENT_ID_COLUMN, STUDENT_FIRST_NAME_COLUMN,
                STUDENT_LAST_NAME_COLUMN, STUDENT_TOKEN_ID_COLUMN, STUDENT_BIRTHDAY_COLUMN};
        String selection = Utility.getStudentSelectionBasedOnFilters(filter);
        String[] selectionArgs = Utility.getStudentSelectionArgsBasedOnFilters(filter);
        List<Student> students = new ArrayList<>();

        try (Cursor retCursor = sQueryJoinStudentCourse.query(
                     database,
                     columns,
                     selection, selectionArgs,
                     STUDENT_ID_COLUMN, null,
                     STUDENT_ID_COLUMN + " ASC",
                     offset + "," + limit)) {

            while (retCursor.moveToNext()) {
                Student student = extractStudentFromCursor(retCursor);
                student.setCourses(getCoursesForStudent(student.getStudentId()));
                students.add(student);
            }
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

    public Student getStudentById(long studentId) throws DatabaseException {
        String[] columns = new String[]{STUDENT_ID_COLUMN, STUDENT_FIRST_NAME_COLUMN,
                STUDENT_LAST_NAME_COLUMN, STUDENT_TOKEN_ID_COLUMN, STUDENT_BIRTHDAY_COLUMN};
        String selection = STUDENT_ID_COLUMN + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(studentId)};

        try (Cursor retCursor = database.query(STUDENT_TABLE_NAME,
                     columns, selection, selectionArgs, null, null, null)) {

            retCursor.moveToFirst();
            Student student = extractStudentFromCursor(retCursor);
            student.setCourses(getCoursesForStudent(studentId));
            return student;
        } catch (NullPointerException | CursorIndexOutOfBoundsException | SQLException e) {
            throw createException(mContext.getString(R.string.error_getting_concrete_person_log));
        }
    }

    private List<Course> getCoursesForStudent(long studentId) throws DatabaseException {
        List<Course> courses = new ArrayList<>();
        String[] selectionArgs = new String[]{String.valueOf(studentId)};
        try (Cursor retCursor = database.rawQuery(
                     SELECT_ALL_COURSES_FOR_PERSON, selectionArgs)) {

            while (retCursor.moveToNext()) {
                Course course = extractCourseFromCursor(retCursor);
                courses.add(course);
            }
        } catch (NullPointerException | CursorIndexOutOfBoundsException | SQLException e) {
            throw createException(mContext.getString(
                    R.string.error_getting_courses_for_person_log));
        }
        return courses;
    }

    private Course extractCourseFromCursor(Cursor cursor) {
        int courseIdColIndex = cursor.getColumnIndex(COURSE_ID_COLUMN);
        int nameColIndex = cursor.getColumnIndex(COURSE_NAME_COLUMN);
        int markColIndex = cursor.getColumnIndex(STUDENT_HAS_COURSE_MARK_COLUMN);

        return new Course.Builder()
                .setCourseId(cursor.getInt(courseIdColIndex))
                .setName(cursor.getString(nameColIndex))
                .setMark(markColIndex == -1 ? 0 : cursor.getInt(markColIndex))
                .build();
    }

    public long insertStudent(Student student) {
        try {
            ContentValues studentCv = new ContentValues();
            studentCv.put(STUDENT_FIRST_NAME_COLUMN, student.getFirstName());
            studentCv.put(STUDENT_LAST_NAME_COLUMN, student.getLastName());
            studentCv.put(STUDENT_TOKEN_ID_COLUMN, student.getStudentTokenId());
            studentCv.put(STUDENT_BIRTHDAY_COLUMN, student.getBirthday());

            long studentId = database.insert(STUDENT_TABLE_NAME, null, studentCv);
            for (Course course : student.getCourses()) {
                insertStudentsCourse(course, studentId);
            }
            Log.d("LOG_TAG", "Row inserted");
            return studentId;
        } catch (SQLException e) {
            throw createException(mContext.getString(
                    R.string.error_getting_courses_for_person_log));
        }
    }

    public void bulkInsertStudents(List<Student> students) {
        try {
            database.beginTransaction();
            int i = 0;
            for (Student student : students) {
                insertStudent(student);
                Log.d("LOG_TAG", "Row inserted " + i);
                i++;
            }
            database.setTransactionSuccessful();
            database.endTransaction();
        } catch (SQLException e) {
            throw createException(mContext.getString(
                    R.string.error_getting_courses_for_person_log));
        }
    }

    private long insertStudentsCourse(Course course, long studentId) {
        try  {
            ContentValues studentCv = new ContentValues();
            studentCv.put(COURSE_NAME_COLUMN, course.getName());

            long courseId = database.insertWithOnConflict(COURSE_TABLE_NAME, null,
                    studentCv, SQLiteDatabase.CONFLICT_IGNORE);
            if (courseId == -1) {
                courseId = getIdForExistingCourse(course);
            }
            assignCourseToStudentWithMark(studentId, courseId, course.getMark());
            return courseId;
        } catch (SQLException e) {
            throw createException(mContext.getString(
                    R.string.error_getting_courses_for_person_log));
        }
    }

    private void assignCourseToStudentWithMark(long studentId,
                                               long courseId, int mark) {
        try  {
            ContentValues studentHasCourseCv = new ContentValues();
            studentHasCourseCv.put(STUDENT_HAS_COURSE_STUDENT_ID, studentId);
            studentHasCourseCv.put(STUDENT_HAS_COURSE_COURSE_ID, courseId);
            studentHasCourseCv.put(STUDENT_HAS_COURSE_MARK_COLUMN, mark);

            database.insertWithOnConflict(STUDENT_HAS_COURSE_TABLE_NAME, null,
                    studentHasCourseCv, SQLiteDatabase.CONFLICT_NONE);
        } catch (SQLException e) {
            throw createException(String.format(mContext.getString(
                    R.string.error_assign_person_to_course_log), studentId, courseId));
        }
    }

    private long getIdForExistingCourse(Course course) {
        String[] columns = new String[]{COURSE_ID_COLUMN};
        String selection = COURSE_NAME_COLUMN + " = ?";
        String[] selectionArgs = new String[]{course.getName()};

        try (Cursor cursor = database.query(COURSE_TABLE_NAME,
                     columns, selection, selectionArgs, null, null, null)) {

            cursor.moveToFirst();
            return cursor.getLong(cursor.getColumnIndex(COURSE_ID_COLUMN));
        } catch (NullPointerException | CursorIndexOutOfBoundsException | SQLException e) {
            throw createException(String.format(
                    mContext.getString(R.string.error_getting_course_id_log), course.getName()));
        }
    }

    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        try (Cursor retCursor = database.rawQuery(SELECT_ALL_COURSES, null)) {

            while (retCursor.moveToNext()) {
                Course course = extractCourseFromCursor(retCursor);
                courses.add(course);
            }
        } catch (NullPointerException | CursorIndexOutOfBoundsException | SQLException e) {
            throw createException(mContext.getString(
                    R.string.error_getting_all_courses));
        }
        return courses;
    }

    public void clearTable(String tableName) {
        try {
            // Delete all rows from table
            database.delete(tableName, null, null);

            // Reset autoincrement variable
            database.delete("SQLITE_SEQUENCE", "name = ?", new String[]{tableName});
        } catch (SQLException e) {
            throw createException(mContext.getString(R.string.error_deleting_rows_log));
        }
    }

    public void closeDatabaseConnection() {
        database.close();
    }
}
