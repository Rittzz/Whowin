package com.rittzz.android.whowin.content;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.rittzz.android.whowin.util.Logging;

public final class SportTable implements DatabaseTable {

    private static final String LOG_TAG = Logging.makeLogTag(SportTable.class);

	// Database table
	public static final String TABLE_NAME = "sports";

	public static final String COLUMN_ID = BaseColumns._ID;
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";

	// Database creation SQL statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_NAME
			+ "(" + COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_NAME + " text not null, "
			+ COLUMN_DESCRIPTION + " text not null"
			+ ");";

	SportTable() {}

	@Override
    public void onCreate(final SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
    public void onUpgrade(final SQLiteDatabase database, final int oldVersion,
			final int newVersion) {
        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
            + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
	}
}
