package com.sbproject.miribyul.listener;

/**
 * Created by Administrator on 2017-11-10.
 */

public interface DataChangeListener {

    void CheckEvent(int groupPosition, int childPosition, boolean isCheck);

    void EditEvent(int groupPosition, int childPosition, String msg);
}
