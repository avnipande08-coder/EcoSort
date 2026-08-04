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

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {

    TextView textViewQuestion;

    RadioGroup radioGroupAnswers;

    RadioButton radioButton1,
            radioButton2,
            radioButton3;

    Button buttonNext;

    ArrayList<Question> questionArrayList =
            new ArrayList<>();

    int currentQuestion = 0;

    int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_activity);

        textViewQuestion =
                findViewById(R.id.textViewQuestion);

        radioGroupAnswers =
                findViewById(R.id.radioGroupAnswers);

        radioButton1 =
                findViewById(R.id.radioButton1);

        radioButton2 =
                findViewById(R.id.radioButton2);

        radioButton3 =
                findViewById(R.id.radioButton3);

        buttonNext =
                findViewById(R.id.buttonNext);

        loadQuestions();

        showQuestion();

        buttonNext.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        checkAnswer();
                    }
                }
        );
    }

    private void loadQuestions() {

        questionArrayList.add(
                new Question(
                        "Where should Banana Peel be thrown?",
                        "Green Bin",
                        "Blue Bin",
                        "Red Bin",
                        "Green Bin"
                )
        );

        questionArrayList.add(
                new Question(
                        "Where should Plastic Bottle be thrown?",
                        "Green Bin",
                        "Blue Bin",
                        "Red Bin",
                        "Blue Bin"
                )
        );

        questionArrayList.add(
                new Question(
                        "Where should Battery be thrown?",
                        "Green Bin",
                        "Blue Bin",
                        "Red Bin",
                        "Red Bin"
                )
        );

        questionArrayList.add(
                new Question(
                        "Where should Newspaper be thrown?",
                        "Green Bin",
                        "Blue Bin",
                        "Red Bin",
                        "Blue Bin"
                )
        );

        questionArrayList.add(
                new Question(
                        "Where should Vegetable Waste be thrown?",
                        "Green Bin",
                        "Blue Bin",
                        "Red Bin",
                        "Green Bin"
                )
        );
    }

    private void showQuestion() {

        Question question =
                questionArrayList.get(currentQuestion);

        textViewQuestion.setText(
                question.getQuestion()
        );

        radioButton1.setText(
                question.getOption1()
        );

        radioButton2.setText(
                question.getOption2()
        );

        radioButton3.setText(
                question.getOption3()
        );

        radioGroupAnswers.clearCheck();
    }

    private void checkAnswer() {

        int selectedId =
                radioGroupAnswers.getCheckedRadioButtonId();

        if (selectedId == -1) {

            Toast.makeText(
                    this,
                    "Select an answer",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        RadioButton selectedButton =
                findViewById(selectedId);

        if (selectedButton.getText().toString().equals(
                questionArrayList.get(currentQuestion).getAnswer()
        )) {

            score++;
        }

        currentQuestion++;

        if (currentQuestion < questionArrayList.size()) {

            showQuestion();

        } else {

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(
                            QuizActivity.this
                    );

            builder.setTitle("🌱 Quiz Finished");

            String message;

            if (score == questionArrayList.size()) {

                message =
                        "Excellent! 🎉\n\n"
                                + "Score : "
                                + score
                                + "/"
                                + questionArrayList.size();

            }
            else if (score >= 3) {

                message =
                        "Good Job! 😊\n\n"
                                + "Score : "
                                + score
                                + "/"
                                + questionArrayList.size();

            }
            else {

                message =
                        "Keep Practicing 🌱\n\n"
                                + "Score : "
                                + score
                                + "/"
                                + questionArrayList.size();

            }

            builder.setMessage(message);

            builder.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(
                                DialogInterface dialog,
                                int which) {

                            finish();
                        }
                    }
            );

            builder.show();
        }
    }
}