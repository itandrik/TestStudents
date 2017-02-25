package com.students.testapp.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
    private SQLiteOpenHelper mDbHelper;
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

    public StudentDatabase(Context mContext,SQLiteOpenHelper mDbHelper) {
        this.mDbHelper = mDbHelper;
        this.mContext = mContext;
    }

    public List<Student> getCertainNumberOfStudents(long limit, long offset) throws DatabaseException {
        String[] columns = new String[]{STUDENT_ID_COLUMN, STUDENT_FIRST_NAME_COLUMN,
                STUDENT_LAST_NAME_COLUMN, STUDENT_TOKEN_ID_COLUMN, STUDENT_BIRTHDAY_COLUMN};
        List<Student> students = new ArrayList<>();

        try (SQLiteDatabase database = mDbHelper.getReadableDatabase();
             Cursor retCursor = database.query(
                     STUDENT_TABLE_NAME,
                     columns,
                     null, null, null, null,
                     STUDENT_ID_COLUMN + " ASC",
                     offset + "," + limit)) {

            while(retCursor.moveToNext()){
                Student student = extractStudentFromCursor(retCursor);
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

        try (SQLiteDatabase database = mDbHelper.getReadableDatabase();
             Cursor retCursor = database.query(STUDENT_TABLE_NAME,
                     columns, selection, selectionArgs, null, null, null)) {

            retCursor.moveToFirst();
            return extractStudentFromCursor(retCursor);
        } catch (NullPointerException | CursorIndexOutOfBoundsException | SQLException e) {
            throw createException(mContext.getString(R.string.error_getting_concrete_person_log));
        }
    }

    public List<Course> getCoursesForStudent(long studentId) throws DatabaseException {
        List<Course> courses = new ArrayList<>();
        String[] selectionArgs = new String[]{String.valueOf(studentId)};
        try (SQLiteDatabase database = mDbHelper.getReadableDatabase();
             Cursor retCursor = database.rawQuery(
                     SELECT_ALL_COURSES_FOR_PERSON, selectionArgs)) {

            while(retCursor.moveToNext()){
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
                .setMark(cursor.getInt(markColIndex))
                .build();
    }

    public long insertStudent(Student student) {
        try (SQLiteDatabase database = mDbHelper.getReadableDatabase()) {
            ContentValues studentCv = new ContentValues();
            studentCv.put(STUDENT_FIRST_NAME_COLUMN, student.getFirstName());
            studentCv.put(STUDENT_LAST_NAME_COLUMN, student.getLastName());
            studentCv.put(STUDENT_TOKEN_ID_COLUMN, student.getStudentTokenId());
            studentCv.put(STUDENT_BIRTHDAY_COLUMN, student.getBirthday());

            return database.insert(STUDENT_TABLE_NAME, null, studentCv);
        } catch (SQLException e) {
            throw createException(mContext.getString(
                    R.string.error_getting_courses_for_person_log));
        }
    }

    public long insertStudentsCourse(Course course, long studentId) {
        try (SQLiteDatabase database = mDbHelper.getReadableDatabase()) {
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
        try (SQLiteDatabase database = mDbHelper.getReadableDatabase()) {
            ContentValues studentHasCourseCv = new ContentValues();
            studentHasCourseCv.put(STUDENT_HAS_COURSE_STUDENT_ID, studentId);
            studentHasCourseCv.put(STUDENT_HAS_COURSE_COURSE_ID, courseId);
            studentHasCourseCv.put(STUDENT_HAS_COURSE_MARK_COLUMN, mark);

            database.insertWithOnConflict(STUDENT_HAS_COURSE_TABLE_NAME, null,
                    studentHasCourseCv,SQLiteDatabase.CONFLICT_NONE);
        } catch (SQLException e) {
            throw createException(String.format(mContext.getString(
                    R.string.error_assign_person_to_course_log), studentId,courseId));
        }
    }

    private long getIdForExistingCourse(Course course) {
        String[] columns = new String[]{COURSE_ID_COLUMN};
        String selection = COURSE_NAME_COLUMN + " = ?";
        String[] selectionArgs = new String[]{course.getName()};

        try (SQLiteDatabase database = mDbHelper.getReadableDatabase();
             Cursor cursor = database.query(COURSE_TABLE_NAME,
                     columns, selection, selectionArgs, null, null, null)) {

            cursor.moveToFirst();
            return cursor.getLong(cursor.getColumnIndex(COURSE_ID_COLUMN));
        } catch (NullPointerException | CursorIndexOutOfBoundsException | SQLException e) {
            throw createException(String.format(
                    mContext.getString(R.string.error_getting_course_id_log), course.getName()));
        }
    }

    public void clearTable(String tableName) {
        try (SQLiteDatabase database = mDbHelper.getWritableDatabase()) {
            // Delete all rows from table
            database.delete(tableName, null, null);

            // Reset autoincrement variable
            database.delete("SQLITE_SEQUENCE", "name = ?", new String[]{tableName});
        } catch (SQLException e) {
            throw createException(mContext.getString(R.string.error_deleting_rows_log));
        }
    }

    public void closeDatabaseConnection() {
        mDbHelper.close();
    }
}
