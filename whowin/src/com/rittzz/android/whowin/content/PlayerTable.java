package com.rittzz.android.whowin.content;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.rittzz.android.whowin.util.Logging;

public final class PlayerTable implements DatabaseTable {

    private static final String LOG_TAG = Logging.makeLogTag(PlayerTable.class);

	// Database table
	public static final String TABLE_NAME = "players";

	public static final String COLUMN_ID = BaseColumns._ID;
	public static final String COLUMN_NAME = "name";

	// Database creation SQL statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_NAME
			+ "(" + COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_NAME + " unique text not null"
			+ ");";

	PlayerTable() {}

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
