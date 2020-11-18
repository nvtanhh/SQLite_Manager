package com.example.sqlitemanager.model;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sqlitemanager.DatabaseDetail;

public class DatabaseHelper extends SQLiteOpenHelper {

//    // Table Name
//    public static final String TABLE_NAME = "STUDENTS";
//
//    // Table columns
//    public static final String _ID = "id";
//    public static final String NAME = "Nguyen Van Tanh";
//    public static final String CLASS = "DH17DTC";
//
//    // Database Information
//    static final String DB_NAME = "Example";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
//    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "('" + _ID + "' INTEGER PRIMARY KEY AUTOINCREMENT, '" + NAME + "' TEXT , '" + CLASS + "' TEXT);";
//
    public DatabaseHelper(Context context, String dbName) {
        super(context, dbName, null, DB_VERSION);
    }

    public DatabaseHelper(Context context) {
        super(context, "", null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}