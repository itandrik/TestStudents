package com.students.testapp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Course, that passed concrete student
 * Student passed course with defined mark.
 * Course has name and id from database
 *
 * @author Andrii Chernysh.
 *         E-mail : itcherry97@gmail.com
 */
public class Course implements Parcelable {
    private long courseId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mark")
    @Expose
    private int mark;

    public Course() {
    }

    protected Course(Parcel in) {
        courseId = in.readLong();
        name = in.readString();
        mark = in.readInt();
    }

    /**
     * Creator, that is needed for instance to be parcelable
     */
    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(courseId);
        dest.writeString(name);
        dest.writeInt(mark);
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
