package com.students.testapp.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.students.testapp.model.db.DatabaseContract.*;

/**
 * @author Andrii Chernysh.
 *         E-mail : itcherry97@gmail.com
 */
public class DatabaseHelperTest extends SQLiteOpenHelper{
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_STUDENT_TABLE =
            "CREATE TABLE IF NOT EXISTS " + STUDENT_TABLE_NAME + "(" +
                    STUDENT_ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    STUDENT_FIRST_NAME_COLUMN + " INTEGER NOT NULL," +
                    STUDENT_TOKEN_ID_COLUMN + " TEXT NOT NULL," +
                    STUDENT_LAST_NAME_COLUMN + " TEXT NOT NULL," +
                    STUDENT_BIRTHDAY_COLUMN + " INTEGER NOT NULL);";
    private static final String SQL_CREATE_COURSE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + COURSE_TABLE_NAME + "(" +
                    COURSE_ID_COLUMN + " INTEGER PRIMARY KEY," +
                    COURSE_NAME_COLUMN + " TEXT NOT NULL UNIQUE);";
    private static final String SQL_CREATE_STUDENT_HAS_COURSE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + STUDENT_HAS_COURSE_TABLE_NAME + "(" +
                    STUDENT_HAS_COURSE_STUDENT_ID + " INTEGER NOT NULL," +
                    STUDENT_HAS_COURSE_COURSE_ID + " INTEGER NOT NULL," +
                    STUDENT_HAS_COURSE_MARK_COLUMN + " INTEGER NOT NULL," +
                    "FOREIGN KEY(" + STUDENT_HAS_COURSE_STUDENT_ID + ") REFERENCES " +
                    STUDENT_TABLE_NAME + "(" + STUDENT_ID_COLUMN + ")," +
                    "FOREIGN KEY(" + STUDENT_HAS_COURSE_COURSE_ID + ") REFERENCES " +
                    COURSE_TABLE_NAME + "(" + COURSE_ID_COLUMN + "));";

    private static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS ";

    public DatabaseHelperTest(Context context) {
        super(context, TEST_DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_STUDENT_TABLE);
        db.execSQL(SQL_CREATE_COURSE_TABLE);
        db.execSQL(SQL_CREATE_STUDENT_HAS_COURSE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE + STUDENT_TABLE_NAME);
        db.execSQL(SQL_DELETE_TABLE + COURSE_TABLE_NAME);
        db.execSQL(SQL_DELETE_TABLE + STUDENT_HAS_COURSE_TABLE_NAME);
        onCreate(db);
    }
}