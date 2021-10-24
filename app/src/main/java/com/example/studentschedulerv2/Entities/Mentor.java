package com.example.studentschedulerv2.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "mentors",
        foreignKeys = {@ForeignKey(entity = Course.class,
                        parentColumns = "id",
                        childColumns = "course_id",
                        onDelete = ForeignKey.CASCADE)})

public class Mentor implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "mentor_ID")
    private int id;
    private String name;

    @ColumnInfo(name = "phone_number")
    private String phoneNumber;
    private String email;
    private int course_id;

    public Mentor(int id, String name, String phoneNumber, String email, int course_id) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.course_id = course_id;
    }

    protected Mentor(Parcel in) {
        id = in.readInt();
        name = in.readString();
        phoneNumber = in.readString();
        email = in.readString();
        course_id = in.readInt();
    }

    public static final Creator<Mentor> CREATOR = new Creator<Mentor>() {
        @Override
        public Mentor createFromParcel(Parcel in) {
            return new Mentor(in);
        }

        @Override
        public Mentor[] newArray(int size) {
            return new Mentor[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(name);
        out.writeString(phoneNumber);
        out.writeString(email);
        out.writeInt(course_id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
