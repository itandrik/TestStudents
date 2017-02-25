package com.students.testapp.model.entity;

/**
 * @author Andrii Chernysh.
 *         E-mail : itcherry97@gmail.com
 */
public class Course {
    private long courseId;
    private String name;
    private int mark;

    public long getCourseId() {
        return courseId;
    }

    public String getName() {
        return name;
    }

    public int getMark() {
        return mark;
    }

    public void setCourseId(long courseId) {
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
        int result = (int) (courseId ^ (courseId >>> 32));
        result = 31 * result + name.hashCode();
        result = 31 * result + mark;
        return result;
    }

    public static class Builder {
        private Course course = new Course();

        public Builder setCourseId(long courseId) {
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
