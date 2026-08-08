package com.example.ecosort.data;

import androidx.room.*;
import java.util.List;

@Dao
public interface WasteLogDao {

    @Insert
    void insert(WasteLogEntity log);

    @Query("SELECT * FROM waste_logs WHERE userId = :userId ORDER BY timestamp DESC")
    List<WasteLogEntity> historyForUser(int userId);
}
