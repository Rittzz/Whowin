package com.rittzz.android.whowin.content;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Build;
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

    private static final int GAMES = 30;
    private static final int SPORTS_ID_GAMES = 31;
    private static final int SPORTS_ID_GAME_ID = 32;

    private static final int SPORTS_ID_STATS = 40;

    private static final int PLAYERS = 50;
    private static final int PLAYER_ID = 51;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // UriMatcher Setup
    static {
        sURIMatcher.addURI(AUTHORITY, "sports", SPORTS);
        sURIMatcher.addURI(AUTHORITY, "players", PLAYERS);
        sURIMatcher.addURI(AUTHORITY, "games", GAMES);

        sURIMatcher.addURI(AUTHORITY, "player/#", PLAYERS);

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

    @TargetApi(14)
    @Override
    public Cursor query(final Uri uri, final String[] projection, final String selection,
        final String[] selectionArgs, final String sortOrder) {

        Log.d(LOG_TAG, "Incoming Query URI: " + uri);

        // Using SQLiteQueryBuilder instead of query() method
        final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        final SQLiteDatabase db = database.getWritableDatabase();

        if (Build.VERSION.SDK_INT >= 14) {
            queryBuilder.setStrict(true);
        }

        final int uriType = sURIMatcher.match(uri);
        switch (uriType) {

        case SPORTS:
            queryBuilder.setTables(SportTable.TABLE_NAME);
            break;
        case PLAYERS:
            queryBuilder.setTables(PlayerTable.TABLE_NAME);
            break;
        case GAMES:
            queryBuilder.setTables(GameTable.TABLE_NAME);
            break;

        case SPORTS_ID_GAMES: {
            final int sportId = Integer.parseInt(uri.getPathSegments().get(1));

            queryBuilder.setTables(GameTable.TABLE_NAME);
            queryBuilder.appendWhere(GameTable.COLUMN_SPORT_ID + " = " + sportId);
        }
            break;
        case SPORTS_ID_GAME_ID: {
            final int sportId = Integer.parseInt(uri.getPathSegments().get(2));
            final int gameId = Integer.parseInt(uri.getLastPathSegment());

            queryBuilder.setTables(GameTable.TABLE_NAME);
            queryBuilder.appendWhere(GameTable.COLUMN_SPORT_ID + " = " + sportId);
            queryBuilder.appendWhere(GameTable.COLUMN_ID + " = " + gameId);
        }
        case PLAYER_ID: {
            final int playerId = Integer.parseInt(uri.getLastPathSegment());

            queryBuilder.setTables(PlayerTable.TABLE_NAME);
            queryBuilder.appendWhere(PlayerTable.COLUMN_ID + " = " + playerId);
        }
            break;
        case SPORTS_ID_PLAYERS: {
            final int sportId = Integer.parseInt(uri.getPathSegments().get(2));

            queryBuilder.setTables(GameTable.TABLE_NAME + ", " + PlayerTable.TABLE_NAME);
            queryBuilder.appendWhere(GameTable.COLUMN_SPORT_ID + " = " + sportId);
        }
            break;
        default:
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        final String query = queryBuilder.buildQuery(projection, selection, null, null, sortOrder, null);
        Log.d("TEST", "Query - " + query);

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
}
