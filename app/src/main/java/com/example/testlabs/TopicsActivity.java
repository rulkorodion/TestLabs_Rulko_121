package com.example.testlabs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TopicsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<String> topics = new ArrayList<>();
    private TestLabsDatabaseHelper dbHelper;
    private ImageButton btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);

        recyclerView = findViewById(R.id.recyclerViewTopics);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        dbHelper = new TestLabsDatabaseHelper(this);
        loadTopicsFromDatabase();

        recyclerView.setAdapter(new TopicAdapter(topics, this));

        btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(this::showPopupMenu);
    }

    private void loadTopicsFromDatabase() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try (Cursor cursor = db.rawQuery("SELECT TopicName FROM Topics", null)) {
            while (cursor.moveToNext()) {
                topics.add(cursor.getString(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenu().add("Темы");
        popupMenu.getMenu().add("Топ пользователей");

        popupMenu.setOnMenuItemClickListener(item -> {
            String title = item.getTitle().toString();
            if (title.equals("Темы")) {
                Toast.makeText(this, "Вы уже на странице тем", Toast.LENGTH_SHORT).show();
            } else if (title.equals("Топ пользователей")) {
                Intent intent = new Intent(TopicsActivity.this, TopUsersActivity.class);
                startActivity(intent);
            }
            return true;
        });

        popupMenu.show();
    }

    private static class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

        private final List<String> topics;
        private final android.content.Context context;

        public TopicAdapter(List<String> topics, android.content.Context context) {
            this.topics = topics;
            this.context = context;
        }

        @Override
        public TopicViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            android.view.View view = android.view.LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_topic, parent, false);
            return new TopicViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TopicViewHolder holder, int position) {
            String topicName = topics.get(position);
            holder.textViewName.setText(topicName);

            int imageResId = getImageResourceId(topicName);
            holder.imageViewIcon.setImageResource(imageResId);

            holder.itemView.setOnClickListener(v -> {
                SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", android.content.Context.MODE_PRIVATE);
                int userId = sharedPreferences.getInt("userId", -1);

                if (userId != -1) {
                    Intent intent = new Intent(context, TestsActivity.class);
                    intent.putExtra("selectedTopic", topicName);
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Ошибка: не удалось получить ID пользователя", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return topics.size();
        }

        private int getImageResourceId(String topicName) {
            switch (topicName) {
                case "Математика": return R.drawable.math;
                case "История": return R.drawable.history;
                case "География": return R.drawable.geography;
                case "Астрономия": return R.drawable.astronomy;
                case "Английский": return R.drawable.english;
                case "Химия": return R.drawable.chemistry;
                case "Физика": return R.drawable.physics;
                case "Спорт": return R.drawable.sport;
                default: return R.drawable.placeholder;
            }
        }

        public static class TopicViewHolder extends RecyclerView.ViewHolder {
            android.widget.ImageView imageViewIcon;
            android.widget.TextView textViewName;

            public TopicViewHolder(android.view.View itemView) {
                super(itemView);
                imageViewIcon = itemView.findViewById(R.id.imageViewIcon);
                textViewName = itemView.findViewById(R.id.textViewName);
            }
        }
    }
}
