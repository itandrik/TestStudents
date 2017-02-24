package com.students.testapp.model.db;

/**
 * Created by Bohdan on 23.02.2017.
 */

public interface DatabaseContract {
    /* Name of database */
    String DATABASE_NAME = "StudentsTestApp.db";
    String TEST_DATABASE_NAME = "TestStudentsApp.db";

    /* Student table name and column names */
    String STUDENT_TABLE_NAME = "Student";
    String STUDENT_ID_COLUMN = "student_id";
    String STUDENT_TOKEN_ID_COLUMN = "student_token_id";
    String STUDENT_FIRST_NAME_COLUMN = "first_name";
    String STUDENT_LAST_NAME_COLUMN = "second_name";
    String STUDENT_BIRTHDAY_COLUMN = "birthday";

    /* Course table name and column names*/
    String COURSE_TABLE_NAME = "Course";
    String COURSE_ID_COLUMN = "course_id";
    String COURSE_NAME_COLUMN = "name";

    /* Student's courses table name and column names*/
    String STUDENT_HAS_COURSE_TABLE_NAME = "M2m_student_course";
    String STUDENT_HAS_COURSE_MARK_COLUMN = "mark";
    String STUDENT_HAS_COURSE_STUDENT_ID = STUDENT_ID_COLUMN;
    String STUDENT_HAS_COURSE_COURSE_ID = COURSE_ID_COLUMN;
}
