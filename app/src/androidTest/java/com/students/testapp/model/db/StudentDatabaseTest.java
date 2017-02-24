package com.students.testapp.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.students.testapp.model.entity.Course;
import com.students.testapp.model.entity.Student;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.List;

import static com.students.testapp.model.db.DatabaseContract.COURSE_NAME_COLUMN;
import static com.students.testapp.model.db.DatabaseContract.COURSE_TABLE_NAME;
import static com.students.testapp.model.db.DatabaseContract.STUDENT_BIRTHDAY_COLUMN;
import static com.students.testapp.model.db.DatabaseContract.STUDENT_FIRST_NAME_COLUMN;
import static com.students.testapp.model.db.DatabaseContract.STUDENT_HAS_COURSE_COURSE_ID;
import static com.students.testapp.model.db.DatabaseContract.STUDENT_HAS_COURSE_MARK_COLUMN;
import static com.students.testapp.model.db.DatabaseContract.STUDENT_HAS_COURSE_STUDENT_ID;
import static com.students.testapp.model.db.DatabaseContract.STUDENT_HAS_COURSE_TABLE_NAME;
import static com.students.testapp.model.db.DatabaseContract.STUDENT_LAST_NAME_COLUMN;
import static com.students.testapp.model.db.DatabaseContract.STUDENT_TABLE_NAME;
import static com.students.testapp.model.db.DatabaseContract.STUDENT_TOKEN_ID_COLUMN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Testing main functions for working with database
 *
 * @author Andrii Chernysh
 */
@RunWith(AndroidJUnit4.class)
public class StudentDatabaseTest {
    private static StudentDatabase sStudentDatabase;
    private SQLiteDatabase mDatabase;
    private static DatabaseHelperTest sDbHelper;
    private Student student = new Student.Builder()
            .setStudentTokenId("6a38f863-8c62-4b4a-a39d-60bcca195f0d")
            .setFirstName("Vasya")
            .setLastName("Pupkin")
            .setBirthday(844387200)
            .build();
    private Course course = new Course.Builder()
            .setMark(5)
            .setName("Math")
            .build();

    @BeforeClass
    public static void init() {
        Context context = InstrumentationRegistry.getTargetContext();
        sStudentDatabase = new StudentDatabase(context);
        sDbHelper = new DatabaseHelperTest(context);
    }

    @Before
    public void setUp() throws Exception {
        mDatabase = sDbHelper.getWritableDatabase();

        // Clear tables
        mDatabase.delete(STUDENT_TABLE_NAME, null, null);
        mDatabase.delete(COURSE_TABLE_NAME, null, null);
        mDatabase.delete(STUDENT_HAS_COURSE_TABLE_NAME, null, null);

        // Insert person
        ContentValues studentCv = new ContentValues();
        studentCv.put(STUDENT_TOKEN_ID_COLUMN, student.getStudentTokenId());
        studentCv.put(STUDENT_FIRST_NAME_COLUMN, student.getFirstName());
        studentCv.put(STUDENT_LAST_NAME_COLUMN, student.getLastName());
        studentCv.put(STUDENT_BIRTHDAY_COLUMN, student.getBirthday());
        long studentId = mDatabase.insert(STUDENT_TABLE_NAME, null, studentCv);

        // Insert course
        ContentValues courseCv = new ContentValues();
        courseCv.put(COURSE_NAME_COLUMN, course.getName());
        long courseId = mDatabase.insert(COURSE_TABLE_NAME, null, courseCv);

        // Assign course to person
        ContentValues m2mStudentCourseCv = new ContentValues();
        m2mStudentCourseCv.put(STUDENT_HAS_COURSE_STUDENT_ID, studentId);
        m2mStudentCourseCv.put(STUDENT_HAS_COURSE_COURSE_ID, courseId);
        m2mStudentCourseCv.put(STUDENT_HAS_COURSE_MARK_COLUMN, course.getMark());
        mDatabase.insert(STUDENT_HAS_COURSE_TABLE_NAME, null, m2mStudentCourseCv);

        student.setCourses(Collections.singletonList(course));
    }

    @Test
    public void testGetCertainNumberOfStudents() throws Exception {
        List<Student> students = sStudentDatabase.getCertainNumberOfStudents(1,100);
        assertEquals("Inserted not 1 element", 1, students.size());
        assertEquals("Incorrect getting students", Collections.singletonList(student), students);
    }

    @Test
    public void testGetStudentById() throws Exception {
        Student selectedStudent = sStudentDatabase.getStudentById(1);
        assertEquals("Selected not correct student", student, selectedStudent);
    }

    @Test
    public void testGetStudentByIncorrectId() throws Exception {
        Student selectedStudent = sStudentDatabase.getStudentById(100500);
        assertNotEquals("Should be error", student, selectedStudent);
    }

    @Test
    public void testGetCoursesForStudent() throws Exception {

    }

    @Test
    public void testInsertStudent() throws Exception {

    }

    @Test
    public void testInsertStudentsCourse() {

    }

    @Test
    public void testAssignCourseToStudentWithMark() {

    }
}