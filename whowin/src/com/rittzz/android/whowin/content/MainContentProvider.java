package com.rittzz.android.whowin.content;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.rittzz.android.whowin.util.Logging;

public class MainContentProvider extends ContentProvider {

    private static final String LOG_TAG = Logging.makeLogTag(MainContentProvider.class);

    static final String AUTHORITY = "com.rittz.android.whowin.contentprovider";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // UriMacher Values
    private static final int SPORTS = 10;

    private static final int SPORTS_ID_PLAYERS = 20;
    private static final int SPORTS_ID_PLAYER_ID = 21;

    private static final int SPORTS_ID_GAMES = 30;
    private static final int SPORTS_ID_GAME_ID = 31;

    private static final int SPORTS_ID_STATS = 40;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // UriMatcher Setup
    static {
        sURIMatcher.addURI(AUTHORITY, "sports", SPORTS);

        sURIMatcher.addURI(AUTHORITY, "sports/#/players", SPORTS_ID_PLAYERS);
        sURIMatcher.addURI(AUTHORITY, "sports/#/player/#", SPORTS_ID_PLAYER_ID);

        sURIMatcher.addURI(AUTHORITY, "sports/#/games", SPORTS_ID_GAMES);
        sURIMatcher.addURI(AUTHORITY, "sports/#/game/#", SPORTS_ID_GAME_ID);

        sURIMatcher.addURI(AUTHORITY, "sports/#/stats", SPORTS_ID_STATS);
    }

    // Database
    private MainDatabaseHelper database;

    @Override
    public boolean onCreate() {
        database = MainDatabaseHelper.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(final Uri uri, final String[] projection, final String selection,
        final String[] selectionArgs, final String sortOrder) {

        Log.d(LOG_TAG, "Incoming Query URI: " + uri);

        // Using SQLiteQueryBuilder instead of query() method
        final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // Check if the caller has requested a column which does not exists
        checkColumns(projection);

        // Set the table
        queryBuilder.setTables(SportTable.TABLE_NAME);

        final int uriType = sURIMatcher.match(uri);
        switch (uriType) {
        case SPORTS:
            // Query the sports DP here
            break;
        default:
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        final SQLiteDatabase db = database.getWritableDatabase();
        final Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null,
            null, sortOrder);

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
        throw new UnsupportedOperationException("The " + CONTENT_URI + " is read only");
    }

    @Override
    public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
        throw new UnsupportedOperationException("The " + CONTENT_URI + " is read only");
    }

    @Override
    public int update(final Uri uri, final ContentValues values, final String selection,
        final String[] selectionArgs) {
        throw new UnsupportedOperationException("The " + CONTENT_URI + " is read only");
    }

    private void checkColumns(final String[] projection) {
        final String[] available = { SportTable.COLUMN_NAME, SportTable.COLUMN_DESCRIPTION,
            SportTable.COLUMN_ID };
        if (projection != null) {
            final HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            final HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
            // Check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }

}
