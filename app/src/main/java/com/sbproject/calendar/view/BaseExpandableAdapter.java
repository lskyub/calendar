package com.sbproject.calendar.view;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.sbproject.calendar.R;
import com.sbproject.calendar.custom.CustomTextWatcher;
import com.sbproject.calendar.listener.DataChangeListener;
import com.sbproject.calendar.model.ChildModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-11-09.
 */
public class BaseExpandableAdapter extends BaseExpandableListAdapter {
    private ArrayList<String> groupList = null;
    private ArrayList<ArrayList<ChildModel>> childList = null;
    private LayoutInflater inflater = null;
    private ViewHolder viewHolder = null;
    private DataChangeListener listener;
    private Context mContext;

    public BaseExpandableAdapter(Context c, ArrayList<String> groupList, ArrayList<ArrayList<ChildModel>> childList, DataChangeListener listener) {
        this.mContext = c;
        this.inflater = LayoutInflater.from(c);
        this.groupList = groupList;
        this.childList = childList;
        this.listener = listener;
    }

    // 그룹 포지션을 반환한다.
    @Override
    public String getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    // 그룹 사이즈를 반환한다.
    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    // 그룹 ID를 반환한다.
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    // 그룹뷰 각각의 ROW
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.list_row_group, parent, false);
            viewHolder.tv_group = (TextView) v.findViewById(R.id.tv_group);
            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();
        }
        viewHolder.tv_group.setText(getGroup(groupPosition));
        return v;
    }

    // 차일드뷰를 반환한다.
    @Override
    public ChildModel getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    // 차일드뷰 사이즈를 반환한다.
    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.get(groupPosition).size();
    }

    // 차일드뷰 ID를 반환한다.
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    // 차일드뷰 각각의 ROW
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View v = convertView;
        ChildModel model = getChild(groupPosition, childPosition);
        if (v == null) {
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.list_row_child, null);
            viewHolder.tv_child = (TextView) v.findViewById(R.id.tv_child);
            viewHolder.tv_child.setPaintFlags(viewHolder.tv_child.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.edt_child = (EditText) v.findViewById(R.id.edt_child);
            viewHolder.cb_child = (CheckBox) v.findViewById(R.id.cb_child);
            viewHolder.edt_child.addTextChangedListener(new CustomTextWatcher(viewHolder.edt_child, listener));
            viewHolder.edt_child.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        EditText editText = (EditText) v;
                        editText.setSelection(editText.getText().length());
                    }
                }
            });
            viewHolder.cb_child.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ChildModel model = (ChildModel) buttonView.getTag();
                    if (model.getData().isCheck) {
                        model.getTvChild().setVisibility(View.VISIBLE);
                        model.getEdtChild().setVisibility(View.GONE);
                    } else {
                        model.getTvChild().setVisibility(View.GONE);
                        model.getEdtChild().setVisibility(View.VISIBLE);
                    }
                    listener.CheckEvent(model.getGroupPosition(), model.getChildPosition(), isChecked);
                }
            });
            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();
        }
        model.setTvChild(viewHolder.tv_child);
        model.setEdtChild(viewHolder.edt_child);
        model.setCbChild(viewHolder.cb_child);
        model.setChildPosition(childPosition);
        model.setGroupPosition(groupPosition);

        viewHolder.edt_child.setTag(model);
        viewHolder.cb_child.setTag(model);

        if (model.getData().isCheck) {
            model.getTvChild().setVisibility(View.VISIBLE);
            model.getEdtChild().setVisibility(View.GONE);
        } else {
            model.getTvChild().setVisibility(View.GONE);
            model.getEdtChild().setVisibility(View.VISIBLE);
        }

        viewHolder.tv_child.setText(model.getData().message);
        viewHolder.edt_child.setText(model.getData().message);
        viewHolder.cb_child.setChecked(model.getData().isCheck);
        return v;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class ViewHolder {
        public TextView tv_group;
        public TextView tv_child;
        public EditText edt_child;
        public CheckBox cb_child;
    }
}