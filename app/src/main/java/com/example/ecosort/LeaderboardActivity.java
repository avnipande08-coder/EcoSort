package com.example.ecosort;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ecosort.data.AppDatabase;
import com.example.ecosort.data.UserEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class LeaderboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView listView = new ListView(this);
        setContentView(listView);

        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            List<UserEntity> users = db.userDao().topUsers();
            List<String> lines = new ArrayList<>();
            int rank = 1;
            for (UserEntity u : users) {
                lines.add(rank++ + ". " + u.username + " — " + u.totalPoints + " pts");
            }
            runOnUiThread(() -> listView.setAdapter(
                    new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lines)));
        });
    }
}
