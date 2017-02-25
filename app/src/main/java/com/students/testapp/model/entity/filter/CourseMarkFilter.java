package com.students.testapp.model.entity.filter;

/**
 * @author Andrii Chernysh.
 *         E-mail : itcherry97@gmail.com
 */
public class CourseMarkFilter {
    private long courseId;
    private int mark;

    public CourseMarkFilter(long courseId, int mark) {
        this.courseId = courseId;
        this.mark = mark;
    }

    public long getCourseId() {
        return courseId;
    }

    public int getMark() {
        return mark;
    }
}
