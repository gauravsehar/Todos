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
        String expensesSql = "CREATE TABLE " + Contract.Todo.TABLE_NAME + " ( " +
                Contract.Todo.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.Todo.NAME + " TEXT, " +
                Contract.Todo.DESCRIPTION + " TEXT, " +
                Contract.Todo.IS_DESCRIPTION_NULL + " INTEGER, " +
                Contract.Todo.PRIORITY + " INTEGER, " +
                Contract.Todo.DUE_TIME_IN_MILLIS + " INTEGER, " +
                Contract.Todo.IS_COMPLETED + " INTEGER, " +
                Contract.Todo.IS_TAGGED + " INTEGER, " +
                Contract.Todo.IS_ALARMSET + " INTEGER )";
        db.execSQL(expensesSql);

        expensesSql = "CREATE TABLE " + Contract.Tags.TABLE_NAME + " ( " +
                Contract.Tags.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.Tags.NAME + " TEXT )";
        db.execSQL(expensesSql);

        expensesSql = "CREATE TABLE " + Contract.TagsHolder.TABLE_NAME + " ( " +
                Contract.TagsHolder.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.TagsHolder.ID_OF_TODO + " INTEGER, " +
                Contract.TagsHolder.ID_OF_TAGS + " INTEGER) ";
        db.execSQL(expensesSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
