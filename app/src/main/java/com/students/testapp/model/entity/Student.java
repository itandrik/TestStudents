package com.students.testapp.model.entity;

/**
 * @author Andrii Chernysh.
 *         E-mail : itcherry97@gmail.com
 */
public class Student {
    private long studentId;
    private String studentTokenId;
    private String firstName;
    private String lastName;
    private long birthday;

    public long getStudentId() {
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

    public void setStudentId(long studentId) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (studentId != student.studentId) return false;
        if (birthday != student.birthday) return false;
        if (!studentTokenId.equals(student.studentTokenId)) return false;
        if (!firstName.equals(student.firstName)) return false;
        return lastName.equals(student.lastName);

    }

    @Override
    public int hashCode() {
        int result = (int) (studentId ^ (studentId >>> 32));
        result = 31 * result + studentTokenId.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + (int) (birthday ^ (birthday >>> 32));
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

        public Builder setBirthday(long birthday) {
            student.setBirthday(birthday);
            return this;
        }

        public Student build() {
            return student;
        }
    }
}
