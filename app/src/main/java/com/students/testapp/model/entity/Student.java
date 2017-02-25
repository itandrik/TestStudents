package com.students.testapp.model.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Andrii Chernysh.
 *         E-mail : itcherry97@gmail.com
 */
public class Student {
    private long studentId;
    @SerializedName("id")
    @Expose
    private String studentTokenId;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("birthday")
    @Expose
    private Long birthday;
    @SerializedName("courses")
    @Expose
    private List<Course> courses = null;

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public String getStudentTokenId() {
        return studentTokenId;
    }

    public void setStudentTokenId(String studentTokenId) {
        this.studentTokenId = studentTokenId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getBirthday() {
        return birthday;
    }

    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }

    public List<Course> getCourses() {
        return courses;
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
        if (!studentTokenId.equals(student.studentTokenId)) return false;
        if (!firstName.equals(student.firstName)) return false;
        if (!lastName.equals(student.lastName)) return false;
        if (!birthday.equals(student.birthday)) return false;
        if (courses == null || student.courses == null) return false;
        if (courses.size() != student.courses.size()) return false;
        for (int i = 0; i < courses.size(); i++) {
            if (!courses.get(i).equals(student.courses.get(i))) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (studentId ^ (studentId >>> 32));
        result = 31 * result + studentTokenId.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + birthday.hashCode();
        result = 31 * result + (courses != null ? courses.hashCode() : 0);
        return result;
    }

    public static class Builder {
        private Student student = new Student();

        public Builder setStudentId(long studentId) {
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

        public Builder setBirthday(Long birthday) {
            student.setBirthday(birthday);
            return this;
        }

        public Builder setCourses(List<Course> courses) {
            student.setCourses(courses);
            return this;
        }

        public Student build() {
            return student;
        }
    }
}
