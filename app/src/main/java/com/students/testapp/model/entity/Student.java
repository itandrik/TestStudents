package com.students.testapp.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bohdan on 23.02.2017.
 */

public class Student {
    private int studentId;
    private String studentTokenId;
    private String firstName;
    private String lastName;
    private long birthday;
    private List<Course> courses;

    public Student() {
        courses = new ArrayList<>();
    }

    public int getStudentId() {
        return studentId;
    }

    public String getStudentTokenId() {
        return studentTokenId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public long getBirthday() {
        return birthday;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setStudentTokenId(String studentTokenId) {
        this.studentTokenId = studentTokenId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (studentId != student.studentId) return false;
        if (birthday != student.birthday) return false;
        if (!studentTokenId.equals(student.studentTokenId)) return false;
        if (!firstName.equals(student.firstName)) return false;
        if (!lastName.equals(student.lastName)) return false;
        if(courses == null || ((Student) o).getCourses() == null) return false;
        if(courses.size() != ((Student) o).getCourses().size()) return false;
        for (int i = 0; i < courses.size(); i++) {
            if(!courses.get(i).equals(((Student) o).getCourses().get(i)))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = studentId;
        result = 31 * result + studentTokenId.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + (int) (birthday ^ (birthday >>> 32));
        result = 31 * result + (courses != null ? courses.hashCode() : 0);
        return result;
    }

    public static class Builder{
        private Student student = new Student();

        public Builder setStudentId(int studentId) {
            student.setStudentId(studentId);
            return this;
        }

        public Builder setStudentTokenId(String studentTokenId) {
            student.setStudentTokenId(studentTokenId);
            return this;
        }

        public Builder setFirstName(String firstName) {
            student.setFirstName(firstName);
            return this;
        }

        public Builder setLastName(String lastName) {
            student.setLastName(lastName);
            return this;
        }

        public Builder setBirthday(long birthday) {
            student.setBirthday(birthday);
            return this;
        }

        public Builder setCourses(List<Course> courses) {
            student.setCourses(courses);
            return this;
        }

        public Student build(){
            return student;
        }
    }
}
