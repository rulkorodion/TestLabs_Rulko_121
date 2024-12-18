package com.example.testlabs;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class QuestionsActivity extends AppCompatActivity {

    private TestLabsDatabaseHelper dbHelper;
    private int currentQuestionIndex = 0;
    private int correctAnswerCount = 0;
    private String correctAnswer;
    private List<Question> questions = new ArrayList<>();
    private TextView textViewQuestion;
    private RadioGroup radioGroupAnswers;
    private Button buttonAnswer;
    private ImageButton btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        dbHelper = new TestLabsDatabaseHelper(this);

        String selectedTestName = getIntent().getStringExtra("selectedTest");

        textViewQuestion = findViewById(R.id.textViewQuestion);
        radioGroupAnswers = findViewById(R.id.radioGroupAnswers);
        buttonAnswer = findViewById(R.id.buttonAnswer);
        btnCancel = findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(v -> finish());

        loadQuestionsFromDatabase(selectedTestName);

        if (!questions.isEmpty()) {
            displayQuestion();
        } else {
            Toast.makeText(this, "Вопросы для выбранного теста не найдены", Toast.LENGTH_SHORT).show();
            finish();
        }

        buttonAnswer.setOnClickListener(v -> {
            int selectedAnswerId = radioGroupAnswers.getCheckedRadioButtonId();

            if (selectedAnswerId != -1) {
                RadioButton selectedAnswer = findViewById(selectedAnswerId);
                String selectedText = selectedAnswer.getText().toString();

                if (selectedText.equals(correctAnswer)) {
                    correctAnswerCount++;
                }

                currentQuestionIndex++;
                if (currentQuestionIndex < questions.size()) {
                    displayQuestion();
                } else {
                    Intent intent = new Intent(QuestionsActivity.this, FinishActivity.class);
                    intent.putExtra("correctAnswers", correctAnswerCount);
                    intent.putExtra("totalQuestions", questions.size());
                    intent.putExtra("testName", selectedTestName);
                    startActivity(intent);
                    finish();
                }
            } else {
                Toast.makeText(this, "Пожалуйста, выберите ответ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadQuestionsFromDatabase(String testName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            String questionQuery = "SELECT Questions.QuestionID, Questions.QuestionText " +
                    "FROM Questions " +
                    "INNER JOIN Tests ON Questions.TestID = Tests.TestID " +
                    "WHERE Tests.TestName = ?";
            cursor = db.rawQuery(questionQuery, new String[]{testName});

            while (cursor.moveToNext()) {
                int questionId = cursor.getInt(0);
                String questionText = cursor.getString(1);

                List<String> answers = new ArrayList<>();
                String localCorrectAnswer = null;

                Cursor answerCursor = db.rawQuery("SELECT AnswerText, IsCorrect FROM Answers WHERE QuestionID = ?",
                        new String[]{String.valueOf(questionId)});

                while (answerCursor.moveToNext()) {
                    String answerText = answerCursor.getString(0);
                    int isCorrect = answerCursor.getInt(1);

                    answers.add(answerText);

                    if (isCorrect == 1) {
                        localCorrectAnswer = answerText;
                    }
                }
                answerCursor.close();

                questions.add(new Question(questionText, answers, localCorrectAnswer));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }

    private void displayQuestion() {
        Question currentQuestion = questions.get(currentQuestionIndex);

        correctAnswer = currentQuestion.getCorrectAnswer();
        TextView textViewTitle = findViewById(R.id.textViewTitle);
        String questionCounter = (currentQuestionIndex + 1) + "/" + questions.size();
        textViewTitle.setText(questionCounter);

        textViewQuestion.setText(currentQuestion.getText());

        radioGroupAnswers.removeAllViews();
        radioGroupAnswers.clearCheck();

        for (String answer : currentQuestion.getAnswers()) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(answer);
            radioButton.setTextSize(20);
            radioGroupAnswers.addView(radioButton);
        }
    }

    private static class Question {
        private final String text;
        private final List<String> answers;
        private final String correctAnswer;

        public Question(String text, List<String> answers, String correctAnswer) {
            this.text = text;
            this.answers = answers;
            this.correctAnswer = correctAnswer;
        }

        public String getText() {
            return text;
        }

        public List<String> getAnswers() {
            return answers;
        }

        public String getCorrectAnswer() {
            return correctAnswer;
        }
    }
}
