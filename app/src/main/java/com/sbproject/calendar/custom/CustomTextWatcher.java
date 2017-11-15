package com.sbproject.calendar.custom;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.sbproject.calendar.listener.DataChangeListener;
import com.sbproject.calendar.model.ChildModel;

/**
 * Created by Administrator on 2017-11-10.
 */

public class CustomTextWatcher implements TextWatcher {
    private EditText edit;
    private DataChangeListener listener;

    public CustomTextWatcher(EditText edit, DataChangeListener listener) {
        this.edit = edit;
        this.listener = listener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        ChildModel model = (ChildModel) edit.getTag();
        listener.EditEvent(model.getGroupPosition(), model.getChildPosition(), s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

}
