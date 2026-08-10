package com.example.ecosort;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ecosort.data.AppDatabase;
import com.example.ecosort.data.WasteLogEntity;
import com.example.ecosort.utils.SessionManager;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;

public class HistoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView listView = new ListView(this);
        setContentView(listView);
        SessionManager session = new SessionManager(this);
        SimpleDateFormat fmt = new SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault());

        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            List<WasteLogEntity> logs = db.wasteLogDao().historyForUser(session.getUserId());
            List<String> lines = new ArrayList<>();
            for (WasteLogEntity l : logs) {
                lines.add(l.category.toUpperCase() + " (+" + l.points + ") — " + fmt.format(new Date(l.timestamp)));
            }
            runOnUiThread(() -> listView.setAdapter(
                    new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lines)));
        });
    }
}
