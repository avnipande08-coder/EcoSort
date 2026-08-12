package com.example.ecosort.data;

import androidx.room.*;
import java.util.List;

@Dao
public interface RedemptionDao {

    @Insert
    void insert(RedemptionEntity redemption);

    @Query("SELECT * FROM redemptions WHERE userId = :userId ORDER BY timestamp DESC")
    List<RedemptionEntity> historyForUser(int userId);
}