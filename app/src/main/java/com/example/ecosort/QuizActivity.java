package com.example.ecosort;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecosort.data.AppDatabase;
import com.example.ecosort.data.UserEntity;
import com.example.ecosort.utils.SessionManager;

import java.util.ArrayList;
import java.util.concurrent.Executors;

public class QuizActivity extends AppCompatActivity {

    TextView textViewQuestion;
    RadioGroup radioGroupAnswers;
    RadioButton radioButton1, radioButton2, radioButton3;
    Button buttonNext;

    ArrayList<Question> questionArrayList = new ArrayList<>();
    int currentQuestion = 0;
    int score = 0;
    static final int POINTS_PER_CORRECT_ANSWER = 5;

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_activity);

        session = new SessionManager(this);

        textViewQuestion = findViewById(R.id.textViewQuestion);
        radioGroupAnswers = findViewById(R.id.radioGroupAnswers);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        buttonNext = findViewById(R.id.buttonNext);

        loadQuestions();
        showQuestion();

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });
    }

    private void loadQuestions() {
        questionArrayList.add(new Question("Where should Banana Peel be thrown?", "Green Bin", "Blue Bin", "Red Bin", "Green Bin"));
        questionArrayList.add(new Question("Where should Plastic Bottle be thrown?", "Green Bin", "Blue Bin", "Red Bin", "Blue Bin"));
        questionArrayList.add(new Question("Where should Battery be thrown?", "Green Bin", "Blue Bin", "Red Bin", "Red Bin"));
        questionArrayList.add(new Question("Where should Newspaper be thrown?", "Green Bin", "Blue Bin", "Red Bin", "Blue Bin"));
        questionArrayList.add(new Question("Where should Vegetable Waste be thrown?", "Green Bin", "Blue Bin", "Red Bin", "Green Bin"));
    }

    private void showQuestion() {
        Question question = questionArrayList.get(currentQuestion);
        textViewQuestion.setText(question.getQuestion());
        radioButton1.setText(question.getOption1());
        radioButton2.setText(question.getOption2());
        radioButton3.setText(question.getOption3());
        radioGroupAnswers.clearCheck();
    }

    private void checkAnswer() {
        int selectedId = radioGroupAnswers.getCheckedRadioButtonId();

        if (selectedId == -1) {
            Toast.makeText(this, "Select an answer", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedButton = findViewById(selectedId);

        if (selectedButton.getText().toString().equals(questionArrayList.get(currentQuestion).getAnswer())) {
            score++;
        }

        currentQuestion++;

        if (currentQuestion < questionArrayList.size()) {
            showQuestion();
        } else {
            finishQuiz();
        }
    }

    private void finishQuiz() {
        int pointsEarned = score * POINTS_PER_CORRECT_ANSWER;

        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            UserEntity user = db.userDao().findById(session.getUserId());
            if (user != null) {
                user.totalPoints += pointsEarned;
                db.userDao().update(user);
            }

            runOnUiThread(() -> showResultDialog(pointsEarned));
        });
    }

    private void showResultDialog(int pointsEarned) {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuizActivity.this);
        builder.setTitle("🌱 Quiz Finished");

        String message;
        if (score == questionArrayList.size()) {
            message = "Excellent! 🎉\n\nScore : " + score + "/" + questionArrayList.size();
        } else if (score >= 3) {
            message = "Good Job! 😊\n\nScore : " + score + "/" + questionArrayList.size();
        } else {
            message = "Keep Practicing 🌱\n\nScore : " + score + "/" + questionArrayList.size();
        }

        message += "\n\n+" + pointsEarned + " points!";

        builder.setMessage(message);
        builder.setPositiveButton("OK", (dialog, which) -> finish());
        builder.show();
    }
}