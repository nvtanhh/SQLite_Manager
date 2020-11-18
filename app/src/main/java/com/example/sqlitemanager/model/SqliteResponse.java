package com.example.sqlitemanager.model;

import android.database.Cursor;

public class SqliteResponse {
    private Cursor mCursor;
    private Throwable mThrowable;
    private boolean mQuerySuccess;

    Cursor getCursor() {
        return mCursor;
    }

    void setCursor(Cursor cursor) {
        this.mCursor = cursor;
    }

    public Throwable getThrowable() {
        return mThrowable;
    }

    void setThrowable(Throwable throwable) {
        this.mThrowable = throwable;
    }

    public boolean isQuerySuccess() {
        return mQuerySuccess;
    }

    void setQuerySuccess(boolean querySuccess) {
        this.mQuerySuccess = querySuccess;
    }
}