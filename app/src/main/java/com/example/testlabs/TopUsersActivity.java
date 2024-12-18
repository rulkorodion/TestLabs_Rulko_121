package com.example.testlabs;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TopUsersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TestLabsDatabaseHelper dbHelper;
    private List<User> topUsers = new ArrayList<>();

    private ImageButton btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_users);

        recyclerView = findViewById(R.id.recyclerViewTopUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new TestLabsDatabaseHelper(this);

        loadTopUsersFromDatabase();

        recyclerView.setAdapter(new TopUsersAdapter(topUsers));

        btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(this::showPopupMenu);
    }

    private void loadTopUsersFromDatabase() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT Username, Score FROM Users ORDER BY Score DESC";

            cursor = db.rawQuery(query, null);

            while (cursor.moveToNext()) {
                String username = cursor.getString(0);
                int score = cursor.getInt(1);
                topUsers.add(new User(username, score));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }


    private static class User {
        String username;
        int score;

        User(String username, int score) {
            this.username = username;
            this.score = score;
        }
    }

    private class TopUsersAdapter extends RecyclerView.Adapter<TopUsersAdapter.TopUsersViewHolder> {

        private final List<User> users;

        TopUsersAdapter(List<User> users) {
            this.users = users;
        }

        @Override
        public TopUsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_top_user, parent, false);
            return new TopUsersViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TopUsersViewHolder holder, int position) {
            User user = users.get(position);
            holder.textViewUsername.setText((position + 1) + ". " + user.username);
            holder.textViewScore.setText("Баллы: " + user.score);
        }

        @Override
        public int getItemCount() {
            return users.size();
        }

        class TopUsersViewHolder extends RecyclerView.ViewHolder {
            TextView textViewUsername, textViewScore;

            TopUsersViewHolder(View itemView) {
                super(itemView);
                textViewUsername = itemView.findViewById(R.id.textViewUsername);
                textViewScore = itemView.findViewById(R.id.textViewScore);
            }
        }
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenu().add("Темы");
        popupMenu.getMenu().add("Топ пользователей");

        popupMenu.setOnMenuItemClickListener(item -> {
            String title = item.getTitle().toString();
            if (title.equals("Топ пользователей")) {
                Toast.makeText(this, "Вы уже на странице топа пользователей", Toast.LENGTH_SHORT).show();
            } else if (title.equals("Темы")) {
                Intent intent = new Intent(TopUsersActivity.this, TopicsActivity.class);
                startActivity(intent);
            }
            return true;
        });

        popupMenu.show();
    }
}