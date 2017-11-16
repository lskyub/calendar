package com.sbproject.calendar.model;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.sbproject.calendar.database.Data;

/**
 * Created by Administrator on 2017-11-09.
 */
public class GroupModel {
    private String day;
    private boolean isLine;

    public GroupModel() {
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public boolean isLine() {
        return isLine;
    }

    public void setLine(boolean line) {
        isLine = line;
    }
}
