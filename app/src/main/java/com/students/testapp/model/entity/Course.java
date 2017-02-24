package com.students.testapp.model.entity;

/**
 * Created by Bohdan on 23.02.2017.
 */

public class Course {
    private int courseId;
    private String name;
    private int mark;

    public int getCourseId() {
        return courseId;
    }

    public String getName() {
        return name;
    }

    public int getMark() {
        return mark;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course course = (Course) o;

        if (courseId != course.courseId) return false;
        if (mark != course.mark) return false;
        return name.equals(course.name);

    }

    @Override
    public int hashCode() {
        int result = courseId;
        result = 31 * result + name.hashCode();
        result = 31 * result + mark;
        return result;
    }

    public static class Builder {
        private Course course = new Course();

        public Builder setCourseId(int courseId) {
            course.setCourseId(courseId);
            return this;
        }

        public Builder setName(String name) {
            course.setName(name);
            return this;
        }

        public Builder setMark(int mark) {
            course.setMark(mark);
            return this;
        }

        public Course build() {
            return course;
        }
    }
}
