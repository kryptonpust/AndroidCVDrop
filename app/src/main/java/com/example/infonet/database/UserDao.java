package com.example.infonet.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.infonet.database.entity.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(User user);

    @Delete
    void delete(User user);

    @Query("DELETE FROM user_info")
    void deleteAllUser();


    @Query("SELECT * FROM user_info")
    LiveData<List<User>> getAllUser();


    @Update
    void update(User user);
}
