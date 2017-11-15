package com.sbproject.calendar.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Administrator on 2017-11-10.
 */
@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public int seq;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "language")
    public String language;

    @ColumnInfo(name = "pushkey")
    public String pushkey;

    @Override
    public String toString() {
        return "User{" +
                "seq=" + seq +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", language='" + language + '\'' +
                ", pushkey='" + pushkey + '\'' +
                '}';
    }
}
