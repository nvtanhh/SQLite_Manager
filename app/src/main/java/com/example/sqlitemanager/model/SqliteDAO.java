package com.example.sqlitemanager.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;


public class SqliteDAO {
    private DatabaseHelper dbHelper;

    private Context context;
    public SQLiteDatabase database;
    public String dbName;

    public SqliteDAO(Context c) {
        context = c;
    }

    public SqliteDAO open(String dbName) throws SQLException {
        this.dbName = dbName.substring(0, dbName.lastIndexOf("."));
        dbHelper = new DatabaseHelper(this.context, dbName);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String name, ContentValues insertValues) throws Exception {  // insert value
        if (database.isOpen()) {

            try {
                database.insert(name, null, insertValues);
            } catch (Exception e) {
                throw new Exception();
            }

        }
    }


    public boolean checkForTableExists(String table) {
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + table + "'";
        Cursor mCursor = database.rawQuery(sql, null);
        if (mCursor.getCount() > 0) {
            return true;
        }
        mCursor.close();
        return false;
    }


    public void delete(String tableName) {
        if (isOpen()) {
            String deleteQuery = "DROP TABLE IF EXISTS " + tableName;
            System.out.println(deleteQuery);
            execQuery(deleteQuery);
        }
    }

    private boolean isOpen() {
        return database.isOpen();
    }

    public Cursor rawQuery(String query) {
        return database.rawQuery(query, null);
    }

    public void execQuery(String query) {
        database.execSQL(query);
    }
}
