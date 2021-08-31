package com.dhill.ineptamazon.db.User;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.lifecycle.LiveData;
import java.util.List;

@Dao
public interface IneptDAO {

    @Insert
    void insert(User...users);

    @Update
    void update(User...users);

    @Delete
    void delete(User...users);

    @Query(" SELECT * FROM user_table" )
    LiveData<List<User>> getAllUsers();

    @Query(" SELECT * FROM " + DataBase.USER_TABLE )
    List<User> ineptGetAllUsers();

    @Query(" SELECT * FROM " + DataBase.USER_TABLE + "  WHERE mUserName= :username")
    User getUserByUsername(String username);

    @Query(" SELECT * FROM " + DataBase.USER_TABLE + "  WHERE mUserId= :userId")
    User getUserByUserId(int userId);

}
