package com.example.testlabs;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FinishActivity extends AppCompatActivity {

    private TextView textViewResult;
    private ImageView imageViewResult;
    private Button buttonToTests;

    private TestLabsDatabaseHelper dbHelper;
    private int userId;
    private String testName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        textViewResult = findViewById(R.id.textViewResult);
        imageViewResult = findViewById(R.id.imageViewResult);
        buttonToTests = findViewById(R.id.buttonToTests);
        dbHelper = new TestLabsDatabaseHelper(this);

        int correctAnswers = getIntent().getIntExtra("correctAnswers", 0);
        int totalQuestions = getIntent().getIntExtra("totalQuestions", 1);
        testName = getIntent().getStringExtra("testName");

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);

        if (userId == -1) {
            Toast.makeText(this, "Ошибка: не удалось получить ID пользователя.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        int testId = getTestIdByName(testName);

        displayResults(correctAnswers, totalQuestions);

        saveUserResult(userId, correctAnswers);

        saveUserCompletion(userId, testId);

        buttonToTests.setOnClickListener(v -> finish());
    }

    private int getTestIdByName(String testName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        int testId = -1;

        try {
            cursor = db.rawQuery("SELECT TestID FROM Tests WHERE TestName = ?", new String[]{testName});
            if (cursor.moveToFirst()) {
                testId = cursor.getInt(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return testId;
    }

    private void displayResults(int correctAnswers, int totalQuestions) {
        String resultText;
        int imageResId;

        double percentage = ((double) correctAnswers / totalQuestions) * 100;

        if (percentage >= 80) {
            resultText = "Поздравляю, у вас отличный результат: " + correctAnswers + "/" + totalQuestions + " баллов!";
            imageResId = R.drawable.excellent;
        } else if (percentage >= 50) {
            resultText = "Неплохо, у вас хороший результат: " + correctAnswers + "/" + totalQuestions + " баллов!";
            imageResId = R.drawable.good;
        } else {
            resultText = "Стоит подучиться! Вы набрали " + correctAnswers + "/" + totalQuestions + " баллов.";
            imageResId = R.drawable.badly;
        }

        textViewResult.setText(resultText);
        imageViewResult.setImageResource(imageResId);
    }

    private void saveUserResult(int userId, int score) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT Score FROM Users WHERE UserID = ?", new String[]{String.valueOf(userId)});
            int currentScore = 0;

            if (cursor.moveToFirst()) {
                currentScore = cursor.getInt(0);
            }
            cursor.close();

            int newScore = currentScore + score;

            ContentValues values = new ContentValues();
            values.put("Score", newScore);
            db.update("Users", values, "UserID = ?", new String[]{String.valueOf(userId)});

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка сохранения результата: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            db.close();
        }
    }

    private void saveUserCompletion(int userId, int testId) {
        if (testId == -1) {
            Toast.makeText(this, "Ошибка: не удалось получить ID теста.", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            Cursor cursor = db.rawQuery(
                    "SELECT 1 FROM UserCompletedTests WHERE UserID = ? AND TestID = ?",
                    new String[]{String.valueOf(userId), String.valueOf(testId)}
            );

            if (!cursor.moveToFirst()) {
                ContentValues values = new ContentValues();
                values.put("UserID", userId);
                values.put("TestID", testId);
                db.insert("UserCompletedTests", null, values);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }
}
