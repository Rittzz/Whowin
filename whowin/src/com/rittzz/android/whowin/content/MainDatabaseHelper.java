package com.rittzz.android.whowin.content;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Do not call any of these methods off the UI thread!  EVER!
 */
public class MainDatabaseHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "whowin_data.db";
	private static final int DATABASE_VERSION = 1;

	private static MainDatabaseHelper instance = null;

	private final List<DatabaseTable> tables = new ArrayList<DatabaseTable>();

	public static synchronized MainDatabaseHelper getInstance(final Context context) {
	    if (instance == null) {
	        instance = new MainDatabaseHelper(context.getApplicationContext());
	    }
	    return instance;
	}

	private MainDatabaseHelper(final Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

		tables.add(new SportTable());
		tables.add(new PlayerTable());
		tables.add(new GameTable());
	}

	/**
	 * Resets the entire database and wipes all the data.
	 */
	public void reset() {
	    for (final DatabaseTable table : tables) {
            table.onReset(getWritableDatabase());
        }
	}

	@Override
	public void onCreate(final SQLiteDatabase database) {
		for (final DatabaseTable table : tables) {
		    table.onCreate(database);
		}
	}

	@Override
	public void onUpgrade(final SQLiteDatabase database, final int oldVersion,
			final int newVersion) {
	    for (final DatabaseTable table : tables) {
	        table.onUpgrade(database, oldVersion, newVersion);
        }
	}
}
