package com.rittzz.android.whowin.content;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.rittzz.android.whowin.util.Logging;

public class MainContentProvider extends ContentProvider {

    private static final String LOG_TAG = Logging.makeLogTag(MainContentProvider.class);

	// database
	private MainDatabaseHelper database;

	// Used for the UriMacher
	private static final int WHOWINS = 10;
	private static final int WHOWIN_ID = 20;

    static final String AUTHORITY = "com.rittz.android.whowin.contentprovider";

	private static final String BASE_PATH = "whowin";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + BASE_PATH);

	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/whowins";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/whowin";

	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);

	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, WHOWINS);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", WHOWIN_ID);
	}

	@Override
	public boolean onCreate() {
		database = new MainDatabaseHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(final Uri uri, final String[] projection, final String selection,
			final String[] selectionArgs, final String sortOrder) {

	    Log.d(LOG_TAG, "Incoming URI: " + uri);

		// Using SQLiteQueryBuilder instead of query() method
		final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		// Check if the caller has requested a column which does not exists
		checkColumns(projection);

		// Set the table
		queryBuilder.setTables(GameTable.TABLE_NAME);

		final int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case WHOWINS:
			break;
		case WHOWIN_ID:
			// Adding the ID to the original query
			queryBuilder.appendWhere(GameTable.COLUMN_ID + "="
					+ uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		final SQLiteDatabase db = database.getWritableDatabase();
		final Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, null, null, sortOrder);
		// Make sure that potential listeners are getting notified
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public String getType(final Uri uri) {
		return null;
	}

	@Override
	public Uri insert(final Uri uri, final ContentValues values) {
	    Log.d(LOG_TAG, "Incoming URI: " + uri);

		final int uriType = sURIMatcher.match(uri);
		final SQLiteDatabase sqlDB = database.getWritableDatabase();
		final int rowsDeleted = 0;
		long id = 0;
		switch (uriType) {
		case WHOWINS:
			id = sqlDB.insert(GameTable.TABLE_NAME, null, values);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
	    Log.d(LOG_TAG, "Incoming URI: " + uri);

		final int uriType = sURIMatcher.match(uri);
		final SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsDeleted = 0;
		switch (uriType) {
		case WHOWINS:
			rowsDeleted = sqlDB.delete(GameTable.TABLE_NAME, selection,
					selectionArgs);
			break;
		case WHOWIN_ID:
			final String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = sqlDB.delete(GameTable.TABLE_NAME,
						GameTable.COLUMN_ID + "=" + id, null);
			} else {
				rowsDeleted = sqlDB.delete(GameTable.TABLE_NAME,
						GameTable.COLUMN_ID + "=" + id + " and " + selection,
						selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public int update(final Uri uri, final ContentValues values, final String selection,
			final String[] selectionArgs) {

		final int uriType = sURIMatcher.match(uri);
		final SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsUpdated = 0;
		switch (uriType) {
		case WHOWINS:
			rowsUpdated = sqlDB.update(GameTable.TABLE_NAME, values, selection,
					selectionArgs);
			break;
		case WHOWIN_ID:
			final String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(GameTable.TABLE_NAME, values,
						GameTable.COLUMN_ID + "=" + id, null);
			} else {
				rowsUpdated = sqlDB.update(GameTable.TABLE_NAME, values,
						GameTable.COLUMN_ID + "=" + id + " and " + selection,
						selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

	private void checkColumns(final String[] projection) {
		final String[] available = {
				GameTable.COLUMN_NAME, GameTable.COLUMN_DESCRIPTION,
				GameTable.COLUMN_ID };
		if (projection != null) {
			final HashSet<String> requestedColumns = new HashSet<String>(
					Arrays.asList(projection));
			final HashSet<String> availableColumns = new HashSet<String>(
					Arrays.asList(available));
			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException(
						"Unknown columns in projection");
			}
		}
	}

}
