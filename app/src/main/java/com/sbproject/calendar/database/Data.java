package com.sbproject.calendar.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Administrator on 2017-11-10.
 */
@Entity
public class Data {
    @PrimaryKey(autoGenerate = true)
    public int seq;

    @ColumnInfo(name = "userseq")
    public int userSeq = 0;

    @ColumnInfo(name = "year")
    public int year = 0;

    @ColumnInfo(name = "month")
    public int month = 0;

    @ColumnInfo(name = "day")
    public int day = 0;

    @ColumnInfo(name = "weekofyear")
    public int weekOfYear = 0;

    @ColumnInfo(name = "dayOfWeek")
    public int dayOfWeek = 0;

    @ColumnInfo(name = "message")
    public String message = "";

    @ColumnInfo(name = "ischeck")
    public boolean isCheck = false;


    @Override
    public String toString() {
        return "Data{" +
                "seq=" + seq +
                ", userSeq='" + userSeq + '\'' +
                ", year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", day='" + day + '\'' +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", weekOfYear='" + weekOfYear + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
