package com.sbproject.calendar.model;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.sbproject.calendar.database.Data;

/**
 * Created by Administrator on 2017-11-09.
 */

public class ChildModel {
    private int groupPosition;
    private int childPosition;
    private TextView tv_child;
    private EditText edt_child;
    private CheckBox cb_child;
    private Data data;

    public ChildModel() {
    }

    public int getGroupPosition() {
        return groupPosition;
    }

    public void setGroupPosition(int groupPosition) {
        this.groupPosition = groupPosition;
    }

    public int getChildPosition() {
        return childPosition;
    }

    public void setChildPosition(int childPosition) {
        this.childPosition = childPosition;
    }

    public TextView getTvChild() {
        return tv_child;
    }

    public void setTvChild(TextView tv_child) {
        this.tv_child = tv_child;
    }

    public EditText getEdtChild() {
        return edt_child;
    }

    public void setEdtChild(EditText edt_child) {
        this.edt_child = edt_child;
    }

    public CheckBox getCbChild() {
        return cb_child;
    }

    public void setCbChild(CheckBox cb_child) {
        this.cb_child = cb_child;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
