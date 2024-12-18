package com.example.testlabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TestsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<String> tests = new ArrayList<>();
    private TestLabsDatabaseHelper dbHelper;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests);

        recyclerView = findViewById(R.id.recyclerViewTests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        btnBack = findViewById(R.id.btnBack);

        dbHelper = new TestLabsDatabaseHelper(this);

        String selectedTopic = getIntent().getStringExtra("selectedTopic");

        loadTestsFromDatabase(selectedTopic);

        recyclerView.setAdapter(new TestsAdapter(tests, this));

        btnBack.setOnClickListener(v -> finish());
    }

    private void loadTestsFromDatabase(String topicName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT TestName FROM Tests " +
                    "INNER JOIN Topics ON Tests.TopicID = Topics.TopicID " +
                    "WHERE Topics.TopicName = ?";
            cursor = db.rawQuery(query, new String[]{topicName});

            while (cursor.moveToNext()) {
                tests.add(cursor.getString(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }

    private class TestsAdapter extends RecyclerView.Adapter<TestsAdapter.TestViewHolder> {

        private final List<String> tests;
        private final Context context;

        public TestsAdapter(List<String> tests, Context context) {
            this.tests = tests;
            this.context = context;
        }

        @Override
        public TestViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            android.view.View view = android.view.LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_test, parent, false);
            return new TestViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TestViewHolder holder, int position) {
            String testName = tests.get(position);
            holder.textViewTestName.setText(testName);

            holder.itemView.setOnClickListener(v -> {
                SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                int userId = sharedPreferences.getInt("userId", -1);

                if (userId == -1) {
                    Toast.makeText(context, "Ошибка: не удалось получить ID пользователя", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isTestCompleted(testName, userId)) {
                    Toast.makeText(context, "Вы уже прошли этот тест", Toast.LENGTH_SHORT).show();
                } else {
                    int testId = getTestIdByName(testName);
                    Intent intent = new Intent(context, QuestionsActivity.class);
                    intent.putExtra("selectedTest", testName);
                    intent.putExtra("testId", testId);
                    context.startActivity(intent);
                }
            });
        }

        private boolean isTestCompleted(String testName, int userId) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = null;
            boolean isCompleted = false;

            try {
                String query = "SELECT 1 FROM UserCompletedTests AS uct " +
                        "JOIN Tests AS t ON uct.TestID = t.TestID " +
                        "WHERE uct.UserID = ? AND t.TestName = ?";
                cursor = db.rawQuery(query, new String[]{String.valueOf(userId), testName});

                if (cursor != null && cursor.moveToFirst()) {
                    isCompleted = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) cursor.close();
                db.close();
            }
            return isCompleted;
        }

        private int getTestIdByName(String testName) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = null;
            int testId = -1;

            try {
                String query = "SELECT TestID FROM Tests WHERE TestName = ?";
                cursor = db.rawQuery(query, new String[]{testName});

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

        @Override
        public int getItemCount() {
            return tests.size();
        }

        public class TestViewHolder extends RecyclerView.ViewHolder {
            android.widget.TextView textViewTestName;

            public TestViewHolder(android.view.View itemView) {
                super(itemView);
                textViewTestName = itemView.findViewById(R.id.textViewTestName);
            }
        }
    }
}
