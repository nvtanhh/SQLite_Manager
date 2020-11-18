package com.example.sqlitemanager.model;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class DBTableEntry implements Comparable<DBTableEntry> {
    public String name;
    public String[] columnNames;
    public ArrayList<String[]> rows;
    public boolean haveData = false;

    public DBTableEntry(String name) {
        this.name = name;
    }

    public static List<DBTableEntry> initTableEntryList(SqliteDAO dao) {
        ArrayList<DBTableEntry> rs = new ArrayList<>();

        Cursor c = dao.rawQuery("SELECT name FROM sqlite_master WHERE type='table'");
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                rs.add(new DBTableEntry(c.getString(c.getColumnIndex("name"))));
                c.moveToNext();
            }
        }
        return rs;
    }

    public void fetchAllData(SqliteDAO dao) {
        rows = new ArrayList<>();
        Cursor dbCursor = dao.database.query(name, null, null, null, null, null, null);
        columnNames = dbCursor.getColumnNames();


        Cursor cur = dao.rawQuery("SELECT * FROM " + name);
        if (cur.getCount() != 0) {
            int length = cur.getColumnCount();
            cur.moveToFirst();

            if (cur.moveToFirst()) {
                do {
                    String[] row = new String[length];

                    for (int i = 0; i < length; i++) {
                        row[i] = cur.getString(i);
                    }
                    rows.add(row);
                } while (cur.moveToNext());
            }
        }
        haveData = true;
    }

    @Override
    public int compareTo(DBTableEntry o) {
        return this.name.compareTo(o.name);
    }

    public SqliteResponse runQuery(SqliteDAO dao, String sql) {
        SqliteResponse sqliteResponse = new SqliteResponse();
        try {
            Cursor cursor = dao.rawQuery(sql);
            sqliteResponse.setCursor(cursor);
            sqliteResponse.setQuerySuccess(true);
        } catch (Throwable throwable) {
            sqliteResponse.setThrowable(throwable);
            sqliteResponse.setQuerySuccess(false);
        }

        if (sqliteResponse.isQuerySuccess()) {
            Cursor cur = sqliteResponse.getCursor();
            rows = new ArrayList<>();
            columnNames = cur.getColumnNames();
            int length = columnNames.length;

            if (cur.moveToFirst()) {
                do {
                    String[] row = new String[length];

                    for (int i = 0; i < length; i++) {
                        row[i] = cur.getString(i);
                    }
                    rows.add(row);
                } while (cur.moveToNext());
            }
        }
        return sqliteResponse;
    }
}
