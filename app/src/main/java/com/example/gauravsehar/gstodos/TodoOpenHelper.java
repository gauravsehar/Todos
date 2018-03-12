package com.example.gauravsehar.gstodos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Gaurav Sehar on 10-Mar-18.
 */

public class TodoOpenHelper extends SQLiteOpenHelper {

    private static TodoOpenHelper todoOpenHelper;

    private TodoOpenHelper(Context applicationContext) {
        super(applicationContext, Contract.DATABASE_NAME, null, Contract.VERSION);
    }

    public static TodoOpenHelper getInstance(Context context) {
        if (todoOpenHelper == null)
            todoOpenHelper = new TodoOpenHelper(context.getApplicationContext());
        return todoOpenHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + Contract.Todo.TABLE_NAME + " ( " +
                Contract.Todo.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.Todo.NAME + " TEXT NOT NULL, " +
                Contract.Todo.DESCRIPTION + " TEXT DEFAULT '', " +
                Contract.Todo.IS_DESCRIPTION_NULL + " INTEGER DEFAULT 0, " +
                Contract.Todo.PRIORITY + " INTEGER DEFAULT 0, " +
                Contract.Todo.DUE_TIME_IN_MILLIS + " INTEGER DEFAULT 0, " +
                Contract.Todo.IS_COMPLETED + " INTEGER DEFAULT 0, " +
                Contract.Todo.IS_TAGGED + " INTEGER DEFAULT 0, " +
                Contract.Todo.IS_ALARMSET + " INTEGER DEFAULT 0 )";
        db.execSQL(query);

        query = "CREATE TABLE " + Contract.Tags.TABLE_NAME + " ( " +
                Contract.Tags.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.Tags.NAME + " TEXT NOT NULL )";
        db.execSQL(query);

        query = "CREATE TABLE " + Contract.TagsHolder.TABLE_NAME + " ( " +
                Contract.TagsHolder.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.TagsHolder.ID_OF_TODO + " INTEGER, " +
                Contract.TagsHolder.ID_OF_TAGS + " INTEGER, " +
                "FOREIGN KEY (" + Contract.TagsHolder.ID_OF_TODO + ") " +
                "REFERENCES " + Contract.Todo.TABLE_NAME + "(" + Contract.Todo.ID + "), " +
                "FOREIGN KEY (" + Contract.TagsHolder.ID_OF_TAGS + ") " +
                "REFERENCES " + Contract.Tags.TABLE_NAME + "(" + Contract.Tags.ID + ") ) ";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
