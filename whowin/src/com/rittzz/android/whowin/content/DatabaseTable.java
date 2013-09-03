package com.rittzz.android.whowin.content;

import android.database.sqlite.SQLiteDatabase;

interface DatabaseTable {

    public abstract void onCreate(SQLiteDatabase database);

    public abstract void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion);

    public abstract void onReset(SQLiteDatabase database);

}