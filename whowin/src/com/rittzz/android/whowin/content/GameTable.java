package com.rittzz.android.whowin.content;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.rittzz.android.whowin.util.Logging;

public final class GameTable implements DatabaseTable {

    private static final String LOG_TAG = Logging.makeLogTag(GameTable.class);

	// Database table
	public static final String TABLE_NAME = "games";

	public static final String COLUMN_ID = BaseColumns._ID;

	public static final String COLUMN_SPORT_ID = "sport_id";

	public static final String COLUMN_NAME = "name";

	public static final String COLUMN_PLAYER_1_ID = "player_1_id";
	public static final String COLUMN_PLAYER_2_ID = "player_2_id";

	public static final String COLUMN_PLAYER_1_GAMES = "player_1_games";
	public static final String COLUMN_PLAYER_2_GAMES = "player_2_games";

	public static final String COLUMN_TIMESTAMP = "timestamp";

	// Joined Columns
	public static final String COLUMN_PLAYER_1_NAME = "player_1_name";
	public static final String COLUMN_PLAYER_2_NAME = "player_2_name";

	// Database creation SQL statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_NAME
			+ "(" + COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_SPORT_ID + " integer not null, "
			+ COLUMN_NAME + " text, "
			+ COLUMN_PLAYER_1_ID + " integer not null, "
			+ COLUMN_PLAYER_2_ID + " integer not null, "
			+ COLUMN_PLAYER_1_GAMES + " integer not null, "
			+ COLUMN_PLAYER_2_GAMES + " integer not null, "
			+ COLUMN_TIMESTAMP + " integer not null"
			+ ");";

	GameTable() {}

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
