package com.example.testlabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TestLabsDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TestLabs.db";
    private static final int DATABASE_VERSION = 3;

    private static final String CREATE_TABLE_USERS = "CREATE TABLE Users (" +
            "UserID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "Username TEXT NOT NULL UNIQUE, " +
            "PasswordHash TEXT NOT NULL, " +
            "Email TEXT NOT NULL UNIQUE, " +
            "Score INTEGER DEFAULT 0" +
            ");";

    private static final String CREATE_TABLE_TOPICS = "CREATE TABLE Topics (" +
            "TopicID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "TopicName TEXT NOT NULL" +
            ");";

    private static final String CREATE_TABLE_TESTS = "CREATE TABLE Tests (" +
            "TestID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "TestName TEXT NOT NULL, " +
            "TopicID INTEGER, " +
            "FOREIGN KEY (TopicID) REFERENCES Topics(TopicID)" +
            ");";

    private static final String CREATE_TABLE_QUESTIONS = "CREATE TABLE Questions (" +
            "QuestionID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "QuestionText TEXT NOT NULL, " +
            "TestID INTEGER, " +
            "FOREIGN KEY (TestID) REFERENCES Tests(TestID)" +
            ");";

    private static final String CREATE_TABLE_ANSWERS = "CREATE TABLE Answers (" +
            "AnswerID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "AnswerText TEXT NOT NULL, " +
            "QuestionID INTEGER, " +
            "IsCorrect INTEGER NOT NULL, " +
            "FOREIGN KEY (QuestionID) REFERENCES Questions(QuestionID)" +
            ");";

    private static final String CREATE_TABLE_COMPLETED_TESTS = "CREATE TABLE UserCompletedTests (" +
            "UserID INTEGER, " +
            "TestID INTEGER, " +
            "PRIMARY KEY (UserID, TestID), " +
            "FOREIGN KEY (UserID) REFERENCES Users(UserID), " +
            "FOREIGN KEY (TestID) REFERENCES Tests(TestID)" +
            ");";

    public TestLabsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_TOPICS);
        db.execSQL(CREATE_TABLE_TESTS);
        db.execSQL(CREATE_TABLE_QUESTIONS);
        db.execSQL(CREATE_TABLE_ANSWERS);
        db.execSQL(CREATE_TABLE_COMPLETED_TESTS);

        db.execSQL("INSERT INTO Users (Username, PasswordHash, Email) VALUES " +
                "('valenok_ivanov', 'hashedpassword1', 'ivanivanov11@gmail.com')," +
                "('maria_petrovna', 'hashedpassword2', 'mariapetrovna7@gmail.com')," +
                "('sergey_sidorov', 'hashedpassword3', 'sergeysidorov65@gmail.com');");

        db.execSQL("INSERT INTO Topics (TopicName) VALUES " +
                "('Математика')," +
                "('История')," +
                "('География')," +
                "('Астрономия')," +
                "('Английский')," +
                "('Химия')," +
                "('Спорт')," +
                "('Физика');");

        db.execSQL("INSERT INTO Tests (TestName, TopicID) VALUES " +
                "('Основы алгебры', 1)," +
                "('Линейные уравнения', 1)," +
                "('Древняя история', 2)," +
                "('Средневековье', 2)," +
                "('Континенты и океаны', 3)," +
                "('Географические рекорды', 3)," +
                "('Звёзды и галактики', 4)," +
                "('Планеты солнечной системы', 4)," +
                "('Базовый английский', 5)," +
                "('Грамматика английского', 5)," +
                "('Основы химии', 6)," +
                "('Органическая химия', 6)," +
                "('История спорта', 7)," +
                "('Виды спорта', 7)," +
                "('Классическая механика', 8)," +
                "('Термодинамика', 8)," +
                "('Великие правители', 2);");

        db.execSQL("INSERT INTO Questions (QuestionText, TestID) VALUES " +
                "('Чему равен квадрат числа 6?', 1)," +
                "('Решите уравнение: 3x = 9. Найдите x.', 1)," +
                "('Как называется сторона квадрата?', 1);");

        db.execSQL("INSERT INTO Answers (AnswerText, QuestionID, IsCorrect) VALUES " +
                "('36', 1, 1)," +
                "('30', 1, 0)," +
                "('12', 1, 0)," +
                "('18', 1, 0)," +

                "('1', 2, 0)," +
                "('3', 2, 1)," +
                "('6', 2, 0)," +
                "('9', 2, 0)," +

                "('Гипотенуза', 3, 0)," +
                "('Сторона', 3, 1)," +
                "('Периметр', 3, 0)," +
                "('Диагональ', 3, 0);");

        db.execSQL("INSERT INTO Questions (QuestionText, TestID) VALUES " +
                "('Решите уравнение: x + 5 = 10.', 2)," +
                "('Чему равна сумма углов треугольника?', 2)," +
                "('Как называется число, делимое на 2 без остатка?', 2);");

        db.execSQL("INSERT INTO Answers (AnswerText, QuestionID, IsCorrect) VALUES " +
                "('2', 4, 0)," +
                "('5', 4, 1)," +
                "('10', 4, 0)," +
                "('15', 4, 0)," +

                "('90°', 5, 0)," +
                "('180°', 5, 1)," +
                "('360°', 5, 0)," +
                "('45°', 5, 0)," +

                "('Четное', 6, 1)," +
                "('Простое', 6, 0)," +
                "('Нечетное', 6, 0)," +
                "('Составное', 6, 0);");

        db.execSQL("INSERT INTO Questions (QuestionText, TestID) VALUES " +
                "('Когда был подписан Магна Карта?', 4)," +
                "('Какой город был столицей Византии?', 4)," +
                "('Кто правил Россией в 1613 году?', 4);");

        db.execSQL("INSERT INTO Answers (AnswerText, QuestionID, IsCorrect) VALUES " +
                "('1215', 7, 1)," +
                "('1492', 7, 0)," +
                "('1066', 7, 0)," +
                "('1776', 7, 0)," +

                "('Рим', 8, 0)," +
                "('Константинополь', 8, 1)," +
                "('Афины', 8, 0)," +
                "('Каир', 8, 0)," +

                "('Иван Грозный', 9, 0)," +
                "('Михаил Романов', 9, 1)," +
                "('Петр I', 9, 0)," +
                "('Борис Годунов', 9, 0);");

        db.execSQL("INSERT INTO Questions (QuestionText, TestID) VALUES " +
                "('Какой океан самый большой?', 5)," +
                "('Какой континент самый жаркий?', 5)," +
                "('Какая река самая длинная?', 5);");

        db.execSQL("INSERT INTO Answers (AnswerText, QuestionID, IsCorrect) VALUES " +
                "('Атлантический', 10, 0)," +
                "('Тихий', 10, 1)," +
                "('Индийский', 10, 0)," +
                "('Северный Ледовитый', 10, 0)," +

                "('Африка', 11, 1)," +
                "('Азия', 11, 0)," +
                "('Австралия', 11, 0)," +
                "('Южная Америка', 11, 0)," +

                "('Амазонка', 12, 1)," +
                "('Нил', 12, 0)," +
                "('Миссисипи', 12, 0)," +
                "('Янцзы', 12, 0);");

        db.execSQL("INSERT INTO Questions (QuestionText, TestID) VALUES " +
                "('Какая планета является самой большой в Солнечной системе?', 7)," +
                "('Как называется ближайшая к Земле звезда?', 7)," +
                "('Сколько планет в Солнечной системе?', 7);");

        db.execSQL("INSERT INTO Answers (AnswerText, QuestionID, IsCorrect) VALUES " +
                "('Земля', 13, 0)," +
                "('Юпитер', 13, 1)," +
                "('Сатурн', 13, 0)," +
                "('Марс', 13, 0)," +

                "('Полярная звезда', 14, 0)," +
                "('Солнце', 14, 1)," +
                "('Сириус', 14, 0)," +
                "('Бетельгейзе', 14, 0)," +

                "('8', 15, 1)," +
                "('9', 15, 0)," +
                "('7', 15, 0)," +
                "('6', 15, 0);");

        db.execSQL("INSERT INTO Questions (QuestionText, TestID) VALUES " +
                "('Какая планета имеет кольца?', 8)," +
                "('Какое небесное тело вращается вокруг планеты?', 8)," +
                "('Где находится пояс астероидов?', 8);");

        db.execSQL("INSERT INTO Answers (AnswerText, QuestionID, IsCorrect) VALUES " +
                "('Венера', 16, 0)," +
                "('Сатурн', 16, 1)," +
                "('Марс', 16, 0)," +
                "('Меркурий', 16, 0)," +

                "('Комета', 17, 0)," +
                "('Луна', 17, 1)," +
                "('Звезда', 17, 0)," +
                "('Метеорит', 17, 0)," +

                "('Между Марсом и Юпитером', 18, 1)," +
                "('За орбитой Земли', 18, 0)," +
                "('Между Венерой и Землёй', 18, 0)," +
                "('На орбите Сатурна', 18, 0);");

        db.execSQL("INSERT INTO Questions (QuestionText, TestID) VALUES " +
                "('Как переводится слово \"apple\"?', 9)," +
                "('Какое время используется для действия в будущем?', 9)," +
                "('Какой артикль используется с существительным \"apple\"?', 9);");

        db.execSQL("INSERT INTO Answers (AnswerText, QuestionID, IsCorrect) VALUES " +
                "('Апельсин', 19, 0)," +
                "('Яблоко', 19, 1)," +
                "('Груша', 19, 0)," +
                "('Лимон', 19, 0)," +

                "('Present Simple', 20, 0)," +
                "('Future Simple', 20, 1)," +
                "('Past Perfect', 20, 0)," +
                "('Present Continuous', 20, 0)," +

                "('A', 21, 1)," +
                "('An', 21, 0)," +
                "('The', 21, 0)," +
                "('No article', 21, 0);");

        db.execSQL("INSERT INTO Questions (QuestionText, TestID) VALUES " +
                "('Какой артикль ставится перед словом \"hour\"?', 10)," +
                "('Как переводится слово \"book\"?', 10)," +
                "('Какая форма глагола используется в Past Simple?', 10);");

        db.execSQL("INSERT INTO Answers (AnswerText, QuestionID, IsCorrect) VALUES " +
                "('A', 22, 0)," +
                "('An', 22, 1)," +
                "('The', 22, 0)," +
                "('No article', 22, 0)," +

                "('Букет', 23, 0)," +
                "('Книга', 23, 1)," +
                "('Тетрадь', 23, 0)," +
                "('Газета', 23, 0)," +

                "('V1', 24, 0)," +
                "('V2', 24, 1)," +
                "('V-ing', 24, 0)," +
                "('V3', 24, 0);");

        db.execSQL("INSERT INTO Questions (QuestionText, TestID) VALUES " +
                "('Как обозначается водород?', 11)," +
                "('Какое вещество имеет химическую формулу H2O?', 11)," +
                "('Что такое CO2?', 11);");

        db.execSQL("INSERT INTO Answers (AnswerText, QuestionID, IsCorrect) VALUES " +
                "('H', 25, 1)," +
                "('O', 25, 0)," +
                "('C', 25, 0)," +
                "('N', 25, 0)," +

                "('Водород', 26, 0)," +
                "('Вода', 26, 1)," +
                "('Кислород', 26, 0)," +
                "('Азот', 26, 0)," +

                "('Углекислый газ', 27, 1)," +
                "('Вода', 27, 0)," +
                "('Аммиак', 27, 0)," +
                "('Азот', 27, 0);");

        db.execSQL("INSERT INTO Questions (QuestionText, TestID) VALUES " +
                "('Какая формула кислорода?', 12)," +
                "('Что такое NaCl?', 12)," +
                "('Какая химическая формула азота?', 12);");

        db.execSQL("INSERT INTO Answers (AnswerText, QuestionID, IsCorrect) VALUES " +
                "('O2', 28, 1)," +
                "('CO2', 28, 0)," +
                "('H2O', 28, 0)," +
                "('O3', 28, 0)," +

                "('Соль', 29, 1)," +
                "('Вода', 29, 0)," +
                "('Кислород', 29, 0)," +
                "('Углекислый газ', 29, 0)," +

                "('N2', 30, 1)," +
                "('NH3', 30, 0)," +
                "('NO2', 30, 0)," +
                "('CO2', 30, 0);");

        db.execSQL("INSERT INTO Questions (QuestionText, TestID) VALUES " +
                "('Какой спорт считается самым популярным в мире?', 13)," +
                "('Сколько игроков в футбольной команде на поле?', 13)," +
                "('Как называется мяч в бейсболе?', 13);");

        db.execSQL("INSERT INTO Answers (AnswerText, QuestionID, IsCorrect) VALUES " +
                "('Баскетбол', 31, 0)," +
                "('Футбол', 31, 1)," +
                "('Теннис', 31, 0)," +
                "('Хоккей', 31, 0)," +

                "('11', 32, 1)," +
                "('10', 32, 0)," +
                "('9', 32, 0)," +
                "('12', 32, 0)," +

                "('Мяч', 33, 0)," +
                "('Бейсбол', 33, 0)," +
                "('Питчер', 33, 0)," +
                "('Бол', 33, 1);");

        db.execSQL("INSERT INTO Questions (QuestionText, TestID) VALUES " +
                "('Как называется олимпийский символ?', 14)," +
                "('Сколько колец на олимпийском флаге?', 14)," +
                "('Какая страна провела первые Олимпийские игры?', 14);");

        db.execSQL("INSERT INTO Answers (AnswerText, QuestionID, IsCorrect) VALUES " +
                "('Факел', 34, 0)," +
                "('Пять колец', 34, 1)," +
                "('Кубок', 34, 0)," +
                "('Гиря', 34, 0)," +

                "('5', 35, 1)," +
                "('4', 35, 0)," +
                "('6', 35, 0)," +
                "('3', 35, 0)," +

                "('Греция', 36, 1)," +
                "('Италия', 36, 0)," +
                "('Франция', 36, 0)," +
                "('США', 36, 0);");

        db.execSQL("INSERT INTO Questions (QuestionText, TestID) VALUES " +
                "('Как называется сила, которая притягивает предметы к земле?', 15)," +
                "('Кто сформулировал три закона движения?', 15)," +
                "('Какая единица измерения силы?', 15);");

        db.execSQL("INSERT INTO Answers (AnswerText, QuestionID, IsCorrect) VALUES " +
                "('Энергия', 37, 0)," +
                "('Гравитация', 37, 1)," +
                "('Магнетизм', 37, 0)," +
                "('Импульс', 37, 0)," +

                "('Эйнштейн', 38, 0)," +
                "('Ньютон', 38, 1)," +
                "('Галилей', 38, 0)," +
                "('Кеплер', 38, 0)," +

                "('Ньютон', 39, 1)," +
                "('Джоуль', 39, 0)," +
                "('Паскаль', 39, 0)," +
                "('Ампер', 39, 0);");

        db.execSQL("INSERT INTO Questions (QuestionText, TestID) VALUES " +
                "('Какое состояние материи не имеет формы?', 16)," +
                "('Что такое напряжение в электричестве?', 16)," +
                "('Как называется скорость света?', 16);");

        db.execSQL("INSERT INTO Answers (AnswerText, QuestionID, IsCorrect) VALUES " +
                "('Твердое', 40, 0)," +
                "('Жидкое', 40, 0)," +
                "('Газообразное', 40, 1)," +
                "('Плазменное', 40, 0)," +

                "('Энергия', 41, 0)," +
                "('Сила', 41, 0)," +
                "('Разность потенциалов', 41, 1)," +
                "('Ток', 41, 0)," +

                "('300 000 км/с', 42, 1)," +
                "('150 000 км/с', 42, 0)," +
                "('400 000 км/с', 42, 0)," +
                "('250 000 км/с', 42, 0);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            db.execSQL(CREATE_TABLE_COMPLETED_TESTS);
            db.execSQL("INSERT INTO Tests (TestName, TopicID) VALUES ('Великие правители', 2);");
        }
    }
}
