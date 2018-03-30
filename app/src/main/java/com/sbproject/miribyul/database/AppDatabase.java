package com.sbproject.miribyul.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by Openit on 2017-09-04.
 * 사용자 정보를 저장하기위한 데이터베이스 Room사용
 */

@Database(entities = {User.class, Data.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    public abstract DataDao dataDao();
}