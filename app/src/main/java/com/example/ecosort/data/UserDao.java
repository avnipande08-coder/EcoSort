package com.example.ecosort.data;

import androidx.room.*;
import java.util.List;

@Dao
public interface UserDao {

    @Insert
    long insert(UserEntity user);

    @Update
    void update(UserEntity user);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    UserEntity findByUsername(String username);

    @Query("SELECT * FROM users WHERE username = :username OR email = :email LIMIT 1")
    UserEntity findByUsernameOrEmail(String username, String email);

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    UserEntity findById(int id);

    @Query("SELECT * FROM users ORDER BY totalPoints DESC LIMIT 20")
    List<UserEntity> topUsers();
}
