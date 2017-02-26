package com.students.testapp.model.entity.filter;

/**
 * Class, that defines field, which is needed for filtering
 *
 * @author Andrii Chernysh.
 *         E-mail : itcherry97@gmail.com
 */
public class CourseMarkFilter {
    private String courseName;
    private int mark;

    public CourseMarkFilter() {
    }

    public CourseMarkFilter(String courseName, int mark) {
        this.courseName = courseName;
        this.mark = mark;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }
}
