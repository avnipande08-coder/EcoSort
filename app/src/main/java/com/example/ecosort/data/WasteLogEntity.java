
package com.example.ecosort.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "waste_logs")
public class WasteLogEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int userId;
    public String category = "";
    public int points;
    public long timestamp;
}
