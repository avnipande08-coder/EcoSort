package com.example.ecosort.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "users")
public class UserEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String username = "";

    @NonNull
    public String email = "";

    @NonNull
    public String passwordHash = "";

    public int totalPoints = 0;
}
