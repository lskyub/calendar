package com.sbproject.calendar.view;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sbproject.calendar.R;
import com.sbproject.calendar.adapter.BaseExpandableAdapter;
import com.sbproject.calendar.database.AppDatabase;
import com.sbproject.calendar.database.Data;
import com.sbproject.calendar.database.User;
import com.sbproject.calendar.listener.AnimationListener;
import com.sbproject.calendar.listener.DataChangeListener;
import com.sbproject.calendar.model.ChildModel;
import com.sbproject.calendar.model.GroupModel;
import com.sbproject.calendar.utils.AnimationInOut;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends Activity implements View.OnClickListener, ExpandableListView.OnGroupClickListener, AdapterView.OnItemLongClickListener, View.OnTouchListener, DataChangeListener {
    private ExpandableListView elvMain;
    private TextView tvLeft;
    private LinearLayout llLeft;
    private TextView tvRigth;
    private LinearLayout llRight;
    private LinearLayout root;
    private LinearLayout llProgressbar;
    private LinearLayout llSetting;
    private LinearLayout llLanguage;
    private Button btnSetting;
    private ArrayList<GroupModel> mGroupList;
    private ArrayList<ArrayList<ChildModel>> mChildList;
    private Map<Integer, ArrayList<ChildModel>> mChildListContent;
    private BaseExpandableAdapter mBaseExpandableAdapter;
    /**
     * 데이터 삭제 후 up이벤트시 리스트 아이템 생성 이벤트 타는 문제는 해결하기위한 변수
     */
    private boolean isDelete = false;
    private Calendar mCalendar;
    private String[] weekArray;
    private AnimationInOut animation;
    private AppDatabase database;
    private int userSeq = -1;
    private List<Data> dataList;
    private boolean isUpdate = false;
    private int mWeekOfYear;
    private int mYear;
    private int mMonth;
    private int mDay;

    @Override
    public void onBackPressed() {
        if (isUpdate) {
            setDataChange(4, getData());
        } else if (llSetting.getVisibility() == View.VISIBLE) {
            animation.startAnimation(llSetting, AnimationInOut.BOTTOM_OUT);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setLayout();
        init();

        getUser();
    }

    @Override
    protected void onResume() {
        Calendar calendar = Calendar.getInstance();
        mWeekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DATE);
        super.onResume();
    }

    @Override
    protected void onPause() {
        setDataChange(4, getData());
        super.onPause();
    }

    /**
     * 데이터 초기화
     */
    private void init() {
        database = Room.databaseBuilder(this, AppDatabase.class, "sbproject").build();
        weekArray = getResources().getStringArray(R.array.week);
        mCalendar = Calendar.getInstance();
        mGroupList = new ArrayList<>();
        mChildList = new ArrayList<>();
        mChildListContent = new HashMap<>();

        for (int i = 0; i < 7; i++) {
            mChildListContent.put(i, new ArrayList<ChildModel>());
            mChildList.add(mChildListContent.get(i));
            mGroupList.add(new GroupModel());
        }
        mBaseExpandableAdapter = new BaseExpandableAdapter(this, mGroupList, mChildList, this);

        elvMain.setAdapter(mBaseExpandableAdapter);
        //리스트 화살표 아이콘 삭제
        elvMain.setGroupIndicator(null);
        // 처음 시작시 그룹 모두 열기 (expandGroup)
        final int groupCount = mBaseExpandableAdapter.getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            elvMain.expandGroup(i);
        }

        animation = new AnimationInOut(this, new AnimationListener() {
            @Override
            public void onStart(int directionIn) {
                if (directionIn == AnimationInOut.BOTTOM_OUT) {
                    btnSetting.setEnabled(false);
                } else if (directionIn == AnimationInOut.BOTTOM_IN) {
                    btnSetting.setEnabled(false);
                    setContentEnabled(false);
                } else {
                    llProgressbar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onEnd(int directionIn) {
                if (directionIn == AnimationInOut.BOTTOM_OUT) {
                    btnSetting.setEnabled(true);
                    llSetting.setVisibility(View.GONE);
                    setContentEnabled(true);
                } else if (directionIn == AnimationInOut.BOTTOM_IN) {
                    btnSetting.setEnabled(true);
                } else {
                    llProgressbar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onEvent(int directionIn) {
                if (directionIn == AnimationInOut.LEFT_IN) {
                    mCalendar.add(Calendar.DATE, -7);
                    setDataChange(3, getData());
                } else if (directionIn == AnimationInOut.RIGHT_IN) {
                    mCalendar.add(Calendar.DATE, 7);
                    setDataChange(3, getData());
                } else if (directionIn == AnimationInOut.BOTTOM_OUT) {
                    btnSetting.setBackground(getResources().getDrawable(R.drawable.setting_p));
                } else if (directionIn == AnimationInOut.BOTTOM_IN) {
                    btnSetting.setBackground(getResources().getDrawable(R.drawable.setting_w));
                }
            }

            private void setContentEnabled(boolean enabled) {
                elvMain.setEnabled(enabled);
                llLeft.setEnabled(enabled);
                llRight.setEnabled(enabled);
            }
        });
    }

    /**
     * 레이아웃 초기화
     */
    private void setLayout() {
        root = (LinearLayout) findViewById(R.id.root);
        llProgressbar = (LinearLayout) findViewById(R.id.ll_progressbar);
        elvMain = (ExpandableListView) findViewById(R.id.elv_main);
        llRight = (LinearLayout) findViewById(R.id.ll_right);
        tvRigth = (TextView) findViewById(R.id.tv_rigth);
        llLeft = (LinearLayout) findViewById(R.id.ll_left);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        llSetting = (LinearLayout) findViewById(R.id.ll_setting);
        btnSetting = (Button) findViewById(R.id.btn_setting);
        llLanguage = (LinearLayout) findViewById(R.id.ll_language);

        // 그룹 클릭 이벤트
        elvMain.setOnGroupClickListener(this);
        // 리스트 롱 클릭 이벤트
        elvMain.setOnItemLongClickListener(this);
        // 리스트 터치 이벤트
        elvMain.setOnTouchListener(this);
        //버튼 이벤트
        llLeft.setOnClickListener(this);
        llRight.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        llLanguage.setOnClickListener(this);
    }

    /**
     * 삭제 이벤트 통제 핸들러
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    isDelete = true;
                    break;
                case 1:
                    isDelete = false;
                    break;
                case 2:
                    setDateOrDataSetting();
                    break;
            }
        }
    };

    /**
     * 날짜 or 데이터 셋팅
     */
    private void setDateOrDataSetting() {
        for (int i = 0; i < mChildListContent.size(); i++) {
            mChildListContent.get(i).clear();
        }

        int weekOfYear = mCalendar.get(Calendar.WEEK_OF_YEAR);
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DATE);

        int dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK);

        int startDay = day;
        int endDay = day;
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                mCalendar.add(Calendar.DATE, -1);
                startDay = mCalendar.get(Calendar.DATE);
                mCalendar.add(Calendar.DATE, 6);
                endDay = mCalendar.get(Calendar.DATE);
                break;
            case Calendar.TUESDAY:
                mCalendar.add(Calendar.DATE, -2);
                startDay = mCalendar.get(Calendar.DATE);
                mCalendar.add(Calendar.DATE, 6);
                endDay = mCalendar.get(Calendar.DATE);
                break;
            case Calendar.WEDNESDAY:
                mCalendar.add(Calendar.DATE, -3);
                startDay = mCalendar.get(Calendar.DATE);
                mCalendar.add(Calendar.DATE, 6);
                endDay = mCalendar.get(Calendar.DATE);
                break;
            case Calendar.THURSDAY:
                mCalendar.add(Calendar.DATE, -4);
                startDay = mCalendar.get(Calendar.DATE);
                mCalendar.add(Calendar.DATE, 6);
                endDay = mCalendar.get(Calendar.DATE);
                break;
            case Calendar.FRIDAY:
                mCalendar.add(Calendar.DATE, -5);
                startDay = mCalendar.get(Calendar.DATE);
                mCalendar.add(Calendar.DATE, 6);
                endDay = mCalendar.get(Calendar.DATE);
                break;
            case Calendar.SATURDAY:
                mCalendar.add(Calendar.DATE, -6);
                startDay = mCalendar.get(Calendar.DATE);
                mCalendar.add(Calendar.DATE, 6);
                endDay = mCalendar.get(Calendar.DATE);
                break;
            case Calendar.SUNDAY:
                mCalendar.add(Calendar.DATE, 6);
                endDay = mCalendar.get(Calendar.DATE);
                break;
        }

        tvLeft.setText(year + " - " + weekOfYear);
        tvRigth.setText(startDay + " - " + endDay + " " + (month + 1) + getString(R.string.month_unit));

        for (int i = 0; i < 7; i++) {
            GroupModel groupModel = new GroupModel();
            if (weekOfYear == mWeekOfYear && year == mYear && month == mMonth && getDay(i) == mDay) {
                groupModel.setLine(true);
            } else {
                groupModel.setLine(false);
            }
            groupModel.setDay(weekArray[i] + " - " + getDay(i) + getString(R.string.day_unit));
            mGroupList.set(i, groupModel);
            if (dataList != null) {
                for (int j = 0; j < dataList.size(); j++) {
                    if (dataList.get(j).day == getDay(i)) {
                        ChildModel model = new ChildModel();
                        Data data = dataList.get(j);
                        model.setGroupPosition(i);
                        model.setChildPosition(mChildListContent.get(i).size());
                        model.setData(data);
                        mChildListContent.get(i).add(model);
                    }
                }
            }
        }
        mBaseExpandableAdapter.notifyDataSetChanged();
    }

    /**
     * 특정 날짜 가져옴
     * 하나의 캘런터로 반복하여 사용하기 때문에 주의 마지막날의 계산 한데이터라 -6을하여 일용일로 설정후 day씩 증가하면 날짜를 출력
     *
     * @param day - 진행일수(0~6 : 일~토)
     * @return 특정 날짜
     */
    private int getDay(int day) {
        Calendar calendar = (Calendar) mCalendar.clone();
        calendar.add(Calendar.DATE, -6);
        calendar.add(Calendar.DATE, day);
        return calendar.get(Calendar.DATE);
    }

    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(root.getWindowToken(), 0);
        switch (v.getId()) {
            case R.id.ll_left:
                animation.startAnimation(elvMain, AnimationInOut.LEFT_IN);
                break;
            case R.id.ll_right:
                animation.startAnimation(elvMain, AnimationInOut.RIGHT_IN);
                break;
            case R.id.btn_setting:
                if (llSetting.getVisibility() == View.VISIBLE) {
                    animation.startAnimation(llSetting, AnimationInOut.BOTTOM_OUT);
                } else if (llSetting.getVisibility() == View.GONE) {
                    animation.startAnimation(llSetting, AnimationInOut.BOTTOM_IN);
                }
                break;
            case R.id.ll_language:
                Toast.makeText(MainActivity.this, "아직 지원하지 않는 기능 입니다.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @NonNull
    private ChildModel getData() {
        ChildModel childModel = new ChildModel();
        Calendar calendar = (Calendar) mCalendar.clone();
        int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        Data data = new Data();
        data.userSeq = userSeq;
        data.weekOfYear = weekOfYear;
        data.year = year;
        data.month = month;
        data.day = day;
        data.dayOfWeek = dayOfWeek;
        childModel.setData(data);
        return childModel;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (MotionEvent.ACTION_UP == event.getAction()) {
            if (isDelete) {
                handler.sendEmptyMessageDelayed(1, 100);
            }
        } else if (MotionEvent.ACTION_DOWN == event.getAction()) {

        }
        return false;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP || ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            handler.sendEmptyMessage(0);
            if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                if (mChildListContent.get(groupPosition).size() != 0) {
                    setDataChange(2, mChildListContent.get(groupPosition).get(0));
                }
            } else if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                int childPosition = ExpandableListView.getPackedPositionChild(id);
                if (mChildListContent.get(groupPosition).size() != 0) {
                    setDataChange(1, mChildListContent.get(groupPosition).get(childPosition));
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        if (!isDelete) {
            ChildModel childModel = new ChildModel();
            childModel.setGroupPosition(groupPosition);
            Calendar calendar = (Calendar) mCalendar.clone();
            calendar.add(Calendar.DATE, (groupPosition - 6));
            int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DATE);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            Data data = new Data();
            data.userSeq = userSeq;
            data.weekOfYear = weekOfYear;
            data.year = year;
            data.month = month;
            data.day = day;
            data.dayOfWeek = dayOfWeek;

            childModel.setData(data);
            setDataChange(0, childModel);
        }
        return true;
    }

    @Override
    public void CheckEvent(int groupPosition, int childPosition, boolean isCheck) {
        isUpdate = true;
        ChildModel childModel = mChildListContent.get(groupPosition).get(childPosition);
        Data data = childModel.getData();
        data.isCheck = isCheck;
        mBaseExpandableAdapter.notifyDataSetChanged();
    }

    @Override
    public void EditEvent(int groupPosition, int childPosition, String msg) {
        isUpdate = true;
        ChildModel childModel = mChildListContent.get(groupPosition).get(childPosition);
        Data data = childModel.getData();
        data.message = msg;
    }

    private void getUser() {
        Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(ObservableEmitter<User> e) throws Exception {
                User user;
                List<User> userList = database.userDao().selectAllUser();
                if (userList.size() == 0) {
                    user = new User();
                    user.email = "email.test";
                    user.password = "password.test";
                    user.name = "test";
                    //테스트성 데이터
                    database.userDao().insert(user);
                }
                user = database.userDao().selectUserEmail("email.test");
                e.onNext(user);
            }
        }).subscribeOn(Schedulers.newThread()).subscribe(new Observer<User>() {
            private List<Data> tempList;

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(User user) {
                userSeq = user.seq;
                int weekOfYear = mCalendar.get(Calendar.WEEK_OF_YEAR);
                int year = mCalendar.get(Calendar.YEAR);
                int month = mCalendar.get(Calendar.MONTH);
                int day = mCalendar.get(Calendar.DATE);
                int dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK);

                tempList = database.dataDao().selectWeekOfYear(userSeq, year, weekOfYear);

                onComplete();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                dataList = tempList;
                setDateOrDataSetting();
            }
        });
    }

    private void setDataChange(final int type, final ChildModel childModel) {
        Observable.create(new ObservableOnSubscribe<Data>() {
            @Override
            public void subscribe(ObservableEmitter<Data> e) throws Exception {
                Data data = childModel.getData();
                if (isUpdate) {
                    database.dataDao().update(dataList);
                    isUpdate = false;
                }
                if (type == 4) {
                    e.onComplete();
                } else {
                    if (type == 0) {
                        database.dataDao().insert(data);
                    } else if (type == 1) {
                        database.dataDao().deleteDay(data.seq, data.year, data.weekOfYear, data.day);
                    } else if (type == 2) {
                        database.dataDao().deleteDay(data.year, data.weekOfYear, data.day);
                    }
                    e.onNext(data);
                }
            }
        }).subscribeOn(Schedulers.newThread()).subscribe(new Observer<Data>() {
            private List<Data> tempList;

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Data data) {
                tempList = database.dataDao().selectWeekOfYear(userSeq, data.year, data.weekOfYear);
                onComplete();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                if (type == 4) {
                    finish();
                } else {
                    dataList = tempList;
                    handler.sendEmptyMessage(2);
                }
            }
        });
    }
}
