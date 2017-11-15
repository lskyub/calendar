package com.sbproject.calendar.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Openit on 2017-09-04.
 * 데이터베이스 데이터 처리를 위한 인터페이스로 쿼리문 수행
 */

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> selectAllUser();

    @Query("select * from user where email like :email")
    User selectUserEmail(String email);

    @Insert
    void insert(User user);

    @Delete
    void delete(User user);
}
