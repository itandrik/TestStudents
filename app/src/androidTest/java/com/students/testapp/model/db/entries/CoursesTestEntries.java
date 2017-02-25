package com.students.testapp.model.db.entries;

import com.students.testapp.model.entity.Course;

/**
 * @author Andrii Chernysh.
 *         E-mail : itcherry97@gmail.com
 */
public enum CoursesTestEntries {
    COURSE_1(1,"Course-1",1),
    COURSE_2(2,"Course-2",2),
    COURSE_3(3,"Course-3",3),
    COURSE_4(4,"Course-4",4);

    private int id;
    private String name;
    private int mark;

    CoursesTestEntries(int id, String name, int mark) {
        this.id = id;
        this.name = name;
        this.mark = mark;
    }

    public Course getCourseInstance(){
        return new Course.Builder()
                .setCourseId(this.id)
                .setName(this.name)
                .setMark(this.mark)
                .build();
    }
}
