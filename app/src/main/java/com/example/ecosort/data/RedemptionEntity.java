package com.example.ecosort.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "redemptions")
public class RedemptionEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int userId;
    public String rewardTitle = "";
    public int pointsSpent;
    public long timestamp;
}