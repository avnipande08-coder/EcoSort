package com.example.ecosort;

import android.content.ClipData;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.ecosort.data.AppDatabase;
import com.example.ecosort.data.UserEntity;
import com.example.ecosort.utils.SessionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Executors;

public class SortingGameActivity extends AppCompatActivity {

    TextView tvDraggableItem, tvItemName, tvScore, tvFeedback;
    CardView itemCard;
    LinearLayout binGreen, binBlue, binRed;

    static class GameItem {
        String emoji, name, correctBin;
        GameItem(String emoji, String name, String correctBin) {
            this.emoji = emoji; this.name = name; this.correctBin = correctBin;
        }
    }

    ArrayList<GameItem> items = new ArrayList<>();
    int currentIndex = 0;
    int score = 0;
    static final int POINTS_PER_CORRECT = 3;

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorting_game);

        session = new SessionManager(this);

        tvDraggableItem = findViewById(R.id.tvDraggableItem);
        tvItemName = findViewById(R.id.tvItemName);
        tvScore = findViewById(R.id.tvScore);
        tvFeedback = findViewById(R.id.tvFeedback);
        itemCard = findViewById(R.id.itemCard);
        binGreen = findViewById(R.id.binGreen);
        binBlue = findViewById(R.id.binBlue);
        binRed = findViewById(R.id.binRed);

        loadItems();
        showItem();

        itemCard.setOnLongClickListener(v -> {
            ClipData data = ClipData.newPlainText("item", "waste_item");
            View.DragShadowBuilder shadow = new View.DragShadowBuilder(v);
            v.startDragAndDrop(data, shadow, v, 0);
            return true;
        });

        setupBinDropTarget(binGreen, "Green");
        setupBinDropTarget(binBlue, "Blue");
        setupBinDropTarget(binRed, "Red");
    }

    private void loadItems() {
        items.add(new GameItem("🍌", "Banana Peel", "Green"));
        items.add(new GameItem("🧴", "Plastic Bottle", "Blue"));
        items.add(new GameItem("🔋", "Battery", "Red"));
        items.add(new GameItem("📰", "Newspaper", "Blue"));
        items.add(new GameItem("🥕", "Vegetable Waste", "Green"));
        items.add(new GameItem("💊", "Expired Medicine", "Red"));
        items.add(new GameItem("🍾", "Glass Bottle", "Blue"));
        Collections.shuffle(items);
    }

    private void showItem() {
        if (currentIndex >= items.size()) {
            finishGame();
            return;
        }
        GameItem item = items.get(currentIndex);
        tvDraggableItem.setText(item.emoji);
        tvItemName.setText(item.name);
        tvFeedback.setText("");
        itemCard.setVisibility(View.VISIBLE);
        tvItemName.setVisibility(View.VISIBLE);
    }

    private void setupBinDropTarget(LinearLayout bin, String binColor) {
        bin.setOnDragListener((v, event) -> {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setAlpha(0.7f);
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setAlpha(1f);
                    return true;
                case DragEvent.ACTION_DROP:
                    v.setAlpha(1f);
                    handleDrop(binColor);
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setAlpha(1f);
                    return true;
            }
            return false;
        });
    }

    private void handleDrop(String droppedBin) {
        GameItem item = items.get(currentIndex);
        boolean correct = item.correctBin.equals(droppedBin);

        if (correct) {
            score++;
            tvFeedback.setText("✅ Correct!");
            tvFeedback.setTextColor(getColor(R.color.green_primary));
        } else {
            tvFeedback.setText("❌ Oops! That goes in " + item.correctBin + " bin");
            tvFeedback.setTextColor(getColor(android.R.color.holo_red_dark));
        }

        tvScore.setText("Score: " + score);
        itemCard.setVisibility(View.INVISIBLE);
        tvItemName.setVisibility(View.INVISIBLE);

        itemCard.postDelayed(() -> {
            currentIndex++;
            showItem();
        }, 900);
    }

    private void finishGame() {
        int pointsEarned = score * POINTS_PER_CORRECT;

        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            UserEntity user = db.userDao().findById(session.getUserId());
            if (user != null) {
                user.totalPoints += pointsEarned;
                db.userDao().update(user);
            }
            runOnUiThread(() -> {
                tvItemName.setText("🎉 Game Over! Score: " + score + "/" + items.size());
                tvItemName.setVisibility(View.VISIBLE);
                tvFeedback.setText("+" + pointsEarned + " points earned!");
                tvFeedback.setTextColor(getColor(R.color.green_dark));
                itemCard.setVisibility(View.GONE);
            });
        });
    }
}