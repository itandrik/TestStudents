package com.students.testapp.model.db;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.students.testapp.model.db.entries.CoursesTestEntries;
import com.students.testapp.model.db.entries.StudentsTestEntries;
import com.students.testapp.model.entity.Course;
import com.students.testapp.model.entity.Student;
import com.students.testapp.model.entity.filter.CourseMarkFilter;
import com.students.testapp.model.exception.DatabaseException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.students.testapp.model.db.DatabaseContract.COURSE_TABLE_NAME;
import static com.students.testapp.model.db.DatabaseContract.STUDENT_HAS_COURSE_TABLE_NAME;
import static com.students.testapp.model.db.DatabaseContract.STUDENT_TABLE_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Testing main functions for working with database
 *
 * @author Andrii Chernysh
 */
@RunWith(AndroidJUnit4.class)
public class StudentDatabaseTest {
    private StudentDatabase mStudentDatabase;

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        DatabaseHelperTest mDbHelper = new DatabaseHelperTest(context);
        mStudentDatabase = new StudentDatabase(context, mDbHelper);

        // Clear tables
        mStudentDatabase.clearTable(STUDENT_TABLE_NAME);
        mStudentDatabase.clearTable(COURSE_TABLE_NAME);
        mStudentDatabase.clearTable(STUDENT_HAS_COURSE_TABLE_NAME);

        // Insert persons and assign all courses for him
        for (StudentsTestEntries student : StudentsTestEntries.values()) {
            mStudentDatabase.insertStudent(student.getStudentInstance());
        }
    }

    @Test
    public void testGetCertainNumberOfStudentsSize() throws Exception {
        List<Student> students = mStudentDatabase.getCertainNumberOfStudents(3, 2, null);
        assertEquals("Incorrect getting students with limit, list size is different", 3, students.size());
    }

    @Test
    public void testGetCertainNumberOfStudentsList() throws Exception {
        List<Student> actualStudents = mStudentDatabase.getCertainNumberOfStudents(3, 2, null);
        List<Student> expectedStudents = Arrays.asList(
                StudentsTestEntries.STUDENT_3.getStudentInstance(),
                StudentsTestEntries.STUDENT_4.getStudentInstance(),
                StudentsTestEntries.STUDENT_5.getStudentInstance());
        assertEquals("Incorrect students selected", expectedStudents, actualStudents);
    }

    @Test
    public void testGetCertainNumberOfStudentsWithFilter() throws Exception {
        CourseMarkFilter filter = new CourseMarkFilter(3, 3);
        List<Student> actualStudents = mStudentDatabase.getCertainNumberOfStudents(3, 2, filter);
        List<Student> expectedStudents = Arrays.asList(
                StudentsTestEntries.STUDENT_3.getStudentInstance(),
                StudentsTestEntries.STUDENT_4.getStudentInstance(),
                StudentsTestEntries.STUDENT_5.getStudentInstance());
        assertEquals("Incorrect students selected with filter", expectedStudents, actualStudents);
    }

    @Test
    public void testGetStudentById() throws Exception {
        Student actualStudent = mStudentDatabase.getStudentById(1);
        Student expectedStudent = StudentsTestEntries.STUDENT_1.getStudentInstance();
        assertEquals("Selected not correct student with id 1", expectedStudent, actualStudent);
    }

    @Test(expected = DatabaseException.class)
    public void testGetStudentByIncorrectId() throws Exception {
        mStudentDatabase.getStudentById(100500);
    }

    @Test
    public void testGetCoursesForStudent() throws Exception {
        Method method = mStudentDatabase.getClass().getDeclaredMethod("getCoursesForStudent", long.class);
        method.setAccessible(true);
        List<Course> actualCourses = (List<Course>) method.invoke(mStudentDatabase, 1);
        List<Course> expectedCourses = new ArrayList<>();
        for (CoursesTestEntries course : CoursesTestEntries.values()) {
            expectedCourses.add(course.getCourseInstance());
        }
        assertEquals("Incorrect course list selected from database for student with id 1",
                expectedCourses, actualCourses);
    }

    @Test
    public void testInsertStudent() throws Exception {
        Course course1 = new Course.Builder()
                .setCourseId(5)
                .setMark(10)
                .setName("Math")
                .build();
        Course course2 = new Course.Builder()
                .setCourseId(6)
                .setMark(11)
                .setName("English")
                .build();
        Student expectedStudent = new Student.Builder()
                .setStudentTokenId("94bdb4f7-f415-4fa7-baeb-16dae373d12d")
                .setFirstName("Ivan-7")
                .setLastName("Petrov-7")
                .setBirthday(858729600000L)
                .setCourses(Arrays.asList(course1, course2))
                .build();

        long actualLastInsertedId = mStudentDatabase.insertStudent(expectedStudent);
        assertEquals("Incorrect last inserted id", 7, actualLastInsertedId);

        expectedStudent.setStudentId(actualLastInsertedId);
        Student actualStudent = mStudentDatabase.getStudentById(actualLastInsertedId);
        assertEquals("Incorrect selected student", expectedStudent, actualStudent);
    }

    @Test
    public void testInsertNonExistingStudentsCourse() throws Exception {
        Method insertStudentsCourseMethod = mStudentDatabase.getClass()
                .getDeclaredMethod("insertStudentsCourse", Course.class, long.class);
        Method getCoursesForStudentMethod = mStudentDatabase.getClass()
                .getDeclaredMethod("getCoursesForStudent", long.class);
        insertStudentsCourseMethod.setAccessible(true);
        getCoursesForStudentMethod.setAccessible(true);

        Course course = new Course.Builder()
                .setName("Course-5")
                .setMark(10)
                .build();
        long actualInsertedId =
                (long) insertStudentsCourseMethod.invoke(mStudentDatabase, course, 1);
        assertEquals("Incorrect course last inserted id", 5, actualInsertedId);

        course.setCourseId(actualInsertedId);
        List<Course> actualCoursesForFirstStudent =
                (List<Course>) getCoursesForStudentMethod.invoke(mStudentDatabase, 1);
        assertTrue("First student doesn't have inserted course. Course insert failure",
                actualCoursesForFirstStudent.contains(course));
    }

    @Test
    public void testInsertExistingStudentsCourse() throws Exception {
        Method insertStudentsCourseMethod = mStudentDatabase.getClass()
                .getDeclaredMethod("insertStudentsCourse", Course.class, long.class);
        Method getCoursesForStudentMethod = mStudentDatabase.getClass()
                .getDeclaredMethod("getCoursesForStudent", long.class);
        insertStudentsCourseMethod.setAccessible(true);
        getCoursesForStudentMethod.setAccessible(true);

        Course course = CoursesTestEntries.COURSE_1.getCourseInstance();
        long actualInsertedId =
                (long) insertStudentsCourseMethod.invoke(mStudentDatabase, course, 1);
        assertEquals("Incorrect existing course last inserted id", 1, actualInsertedId);

        List<Course> actualCoursesForFirstStudent =
                (List<Course>) getCoursesForStudentMethod.invoke(mStudentDatabase, 1);
        assertTrue("First student doesn't have inserted course. Course insert failure",
                actualCoursesForFirstStudent.contains(course));
    }

    @Test
    public void testAssignCourseToStudentWithMark() throws Exception {

    }

    @Test
    public void getAllCourses() throws Exception {
        List<Course> actualCourses = mStudentDatabase.getAllCourses();
        List<Course> expectedCourses = new ArrayList<>();
        for (CoursesTestEntries course: CoursesTestEntries.values()) {
            Course courseInstance = course.getCourseInstance();
            courseInstance.setMark(0);
            expectedCourses.add(courseInstance);
        }
        assertEquals("Incorrect select all courses from database," +
                " or check inserting course with conflict",
                expectedCourses, actualCourses);
    }

    @Test
    public void testClearTable() throws Exception {
        mStudentDatabase.clearTable(STUDENT_TABLE_NAME);
        int actualCountOfRows = mStudentDatabase.getCertainNumberOfStudents(6, 0, null).size();
        assertEquals("Table" + STUDENT_TABLE_NAME + " is not empty after deleting rows",
                0, actualCountOfRows);
    }

    @After
    public void closeDatabaseConnection() throws Exception {
        mStudentDatabase.closeDatabaseConnection();
    }
}