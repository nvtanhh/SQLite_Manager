package com.example.sqlitemanager.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseEntry implements Serializable {
    public final String name;
    public final String description;

    public DatabaseEntry(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static List<DatabaseEntry> initProductEntryList(Context context) {
        List<DatabaseEntry> rs = new ArrayList<DatabaseEntry>();

        String[] databases = context.databaseList();
        for (int i = 0; i < databases.length; i++) {
            if (databases[i].endsWith(".db"))
                rs.add(new DatabaseEntry(databases[i].substring(0, databases[i].lastIndexOf(".")), ""));
        }
        System.out.println(Arrays.toString(rs.toArray()));

        if (rs.size() == 0) {
            createSampleDatabase(context);
            rs.add(new DatabaseEntry("Example", "Just a example database"));
        }
        System.out.println(Arrays.toString(rs.toArray()));
        return rs;
    }

    private static void createSampleDatabase(Context context) {
        SqliteDAO sampleDB = new SqliteDAO(context).open("Example.db");
        sampleDB.close();
    }


    @Override
    public String toString() {
        return "DatabaseEntry{" +
                "name='" + name + '\'' +
                '}';
    }
}
