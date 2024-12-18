package com.example.testlabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TestLabsDatabaseHelper extends SQLiteOpenHelper {

    // Название базы данных и версия
    private static final String DATABASE_NAME = "TestLabs.db";
    private static final int DATABASE_VERSION = 1;

    // Запросы для создания таблиц

    // Таблица Users
    private static final String CREATE_TABLE_USERS = "CREATE TABLE Users (" +
            "UserID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "Username TEXT NOT NULL UNIQUE, " +
            "FIO TEXT, " +
            "PasswordHash TEXT NOT NULL, " +
            "Email TEXT NOT NULL UNIQUE, " +
            "ProfilePicture TEXT" +
            ");";

    // Таблица Topics
    private static final String CREATE_TABLE_TOPICS = "CREATE TABLE Topics (" +
            "TopicID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "TopicName TEXT NOT NULL" +
            ");";

    // Таблица Tests
    private static final String CREATE_TABLE_TESTS = "CREATE TABLE Tests (" +
            "TestID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "TestName TEXT NOT NULL, " +
            "TopicID INTEGER, " +
            "Description TEXT, " +
            "FOREIGN KEY (TopicID) REFERENCES Topics(TopicID)" +
            ");";

    // Таблица Questions
    private static final String CREATE_TABLE_QUESTIONS = "CREATE TABLE Questions (" +
            "QuestionID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "QuestionText TEXT NOT NULL, " +
            "TestID INTEGER, " +
            "FOREIGN KEY (TestID) REFERENCES Tests(TestID)" +
            ");";

    // Таблица Answers
    private static final String CREATE_TABLE_ANSWERS = "CREATE TABLE Answers (" +
            "AnswerID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "AnswerText TEXT NOT NULL, " +
            "QuestionID INTEGER, " +
            "IsCorrect INTEGER NOT NULL, " +
            "FOREIGN KEY (QuestionID) REFERENCES Questions(QuestionID)" +
            ");";

    // Таблица UserResults
    private static final String CREATE_TABLE_USERRESULTS = "CREATE TABLE UserResults (" +
            "ResultID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "UserID INTEGER, " +
            "TestID INTEGER, " +
            "Score INTEGER NOT NULL, " +
            "UNIQUE (UserID, TestID), " +
            "FOREIGN KEY (UserID) REFERENCES Users(UserID), " +
            "FOREIGN KEY (TestID) REFERENCES Tests(TestID)" +
            ");";

    // Таблица Leaderboard
    private static final String CREATE_TABLE_LEADERBOARD = "CREATE TABLE Leaderboard (" +
            "LeaderboardID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "UserID INTEGER, " +
            "TotalScore INTEGER NOT NULL, " +
            "Rank INTEGER NOT NULL, " +
            "FOREIGN KEY (UserID) REFERENCES Users(UserID)" +
            ");";

    // Таблица TopicLeaderboard
    private static final String CREATE_TABLE_TOPICLEADERBOARD = "CREATE TABLE TopicLeaderboard (" +
            "TopicLeaderboardID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "TopicID INTEGER, " +
            "UserID INTEGER, " +
            "TopicScore INTEGER NOT NULL, " +
            "Rank INTEGER NOT NULL, " +
            "FOREIGN KEY (TopicID) REFERENCES Topics(TopicID), " +
            "FOREIGN KEY (UserID) REFERENCES Users(UserID)" +
            ");";

    public TestLabsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Создание всех таблиц
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_TOPICS);
        db.execSQL(CREATE_TABLE_TESTS);
        db.execSQL(CREATE_TABLE_QUESTIONS);
        db.execSQL(CREATE_TABLE_ANSWERS);
        db.execSQL(CREATE_TABLE_USERRESULTS);
        db.execSQL(CREATE_TABLE_LEADERBOARD);
        db.execSQL(CREATE_TABLE_TOPICLEADERBOARD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Удаляем старые таблицы и создаём заново при обновлении
        db.execSQL("DROP TABLE IF EXISTS TopicLeaderboard");
        db.execSQL("DROP TABLE IF EXISTS Leaderboard");
        db.execSQL("DROP TABLE IF EXISTS UserResults");
        db.execSQL("DROP TABLE IF EXISTS Answers");
        db.execSQL("DROP TABLE IF EXISTS Questions");
        db.execSQL("DROP TABLE IF EXISTS Tests");
        db.execSQL("DROP TABLE IF EXISTS Topics");
        db.execSQL("DROP TABLE IF EXISTS Users");
        onCreate(db);
    }
}