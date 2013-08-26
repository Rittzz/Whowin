package com.rittzz.android.whowin.content;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MainDatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "whowin_data.db";
	private static final int DATABASE_VERSION = 1;

	private final List<DatabaseTable> tables = new ArrayList<DatabaseTable>();

	public MainDatabaseHelper(final Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

		tables.add(new GameTable());
	}

	// Method is called during creation of the database
	@Override
	public void onCreate(final SQLiteDatabase database) {
		for (final DatabaseTable table : tables) {
		    table.onCreate(database);
		}
	}

	// Method is called during an upgrade of the database,
	// e.g. if you increase the database version
	@Override
	public void onUpgrade(final SQLiteDatabase database, final int oldVersion,
			final int newVersion) {
	    for (final DatabaseTable table : tables) {
	        table.onUpgrade(database, oldVersion, newVersion);
        }
	}
}
