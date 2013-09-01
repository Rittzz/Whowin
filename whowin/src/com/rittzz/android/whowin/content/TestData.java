package com.rittzz.android.whowin.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.util.Pair;

public class TestData {
    public static void testDatabase(final Context context) {
        // Reset Database
        context.deleteDatabase(MainDatabaseHelper.DATABASE_NAME);

        // Add Test Data
        addTestData(context);

        // Query Test Data

        // Get the SportId
        long sportId = -1;
        {
            final Uri uri = WhowinData.CONTENT_URI.buildUpon().appendPath("sports").build();
            final Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            Log.d("TestData", "Sports");
            while (cursor.moveToNext()) {
                sportId = cursor.getLong(cursor.getColumnIndexOrThrow(SportTable.COLUMN_ID));
                Log.d("TestData", "DATA - " + cursorToString(cursor));
            }
            cursor.close();
        }

        // Query the Players
        {
            final Uri uri = WhowinData.CONTENT_URI.buildUpon().appendPath("players").build();
            final Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            Log.d("TestData", "Players");
            while (cursor.moveToNext()) {
                Log.d("TestData", "DATA - " + cursorToString(cursor));
            }
            cursor.close();
        }

        // Query the Games
        {
            final Uri uri = WhowinData.CONTENT_URI.buildUpon().appendPath("games").build();
            final Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            Log.d("TestData", "Games");
            while (cursor.moveToNext()) {
                Log.d("TestData", "DATA - " + cursorToString(cursor));
            }
            cursor.close();
        }

        // Query the Players
        {
            final Uri uri = WhowinData.CONTENT_URI.buildUpon().appendPath("sports")
                .appendPath(Long.toString(sportId)).appendPath("games").build();
            final Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            Log.d("TestData", "Games for Sport " + sportId);
            while (cursor.moveToNext()) {
                sportId = cursor.getLong(cursor.getColumnIndexOrThrow(SportTable.COLUMN_ID));
                Log.d("TestData", "DATA - " + cursorToString(cursor));
            }
            cursor.close();
        }
    }

    public static void addTestData(final Context context) {
        final SQLiteDatabase db = MainDatabaseHelper.getInstance(context).getWritableDatabase();
        db.beginTransaction();
        try {
            // A Sport first
            final ContentValues sportValues = new ContentValues();
            sportValues.put(SportTable.COLUMN_NAME, "Pool");
            sportValues.put(SportTable.COLUMN_DESCRIPTION, "The awesome soundhound pool stats");
            final long sportId = db.insertOrThrow(SportTable.TABLE_NAME, null, sportValues);

            // Then some players
            final int totalPlayers = 10;
            final ArrayList<Long> playerIds = new ArrayList<Long>(totalPlayers);
            for (int i = 0; i < totalPlayers; i++) {
                final ContentValues values = new ContentValues();
                values.put(PlayerTable.COLUMN_NAME, "Name " + i);
                playerIds.add(db.insertOrThrow(PlayerTable.TABLE_NAME, null, values));
            }

            // Add Games
            final int totalGameCount = 10;
            for (int i = 0; i < totalGameCount; i++) {
                final ContentValues values = new ContentValues();
                values.put(GameTable.COLUMN_SPORT_ID, sportId);

                final Pair<Long, Long> playerPair = pickTwoPlayers(playerIds);
                values.put(GameTable.COLUMN_PLAYER_1_ID, playerPair.first);
                values.put(GameTable.COLUMN_PLAYER_2_ID, playerPair.second);

                final Pair<Integer, Integer> wins = randomWinCount();
                values.put(GameTable.COLUMN_PLAYER_1_GAMES, wins.first);
                values.put(GameTable.COLUMN_PLAYER_2_GAMES, wins.second);

                final long timestamp = System.currentTimeMillis();
                values.put(GameTable.COLUMN_TIMESTAMP, timestamp);

                db.insertOrThrow(GameTable.TABLE_NAME, null, values);
            }

            // Commit the Base
            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }
    }

    /**
     * Assumes the list parameters is at least of size 2.
     *
     * @param list
     * @return
     */
    private static Pair<Long, Long> pickTwoPlayers(final Collection<Long> list) {
        final ArrayList<Long> copyList = new ArrayList<Long>(list);
        Collections.shuffle(copyList);
        return Pair.create(copyList.get(0), copyList.get(1));
    }

    private static final int[] bestOfCount = new int[] { 1, 3, 5, 7, 9 };

    private static Pair<Integer, Integer> randomWinCount() {
        final Random random = new Random();

        final int bestOf = bestOfCount[Math.abs(random.nextInt() % bestOfCount.length)];
        final int toWinCount = bestOf / 2 + 1;

        final int winnerWins = toWinCount;
        final int loserWins = toWinCount > 1 ? Math.abs(random.nextInt() % (toWinCount - 1)) : 0;

        if (random.nextDouble() < 0.5) {
            return Pair.create(winnerWins, loserWins);
        }
        else {
            return Pair.create(loserWins, winnerWins);
        }
    }

    private static String cursorToString(final Cursor cursor) {
        final StringBuilder str = new StringBuilder();
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            final String columnName = cursor.getColumnName(i);
            final String value = cursor.getString(i);

            str.append(String.format("'%s'='%s'", columnName, value));
        }
        return str.toString();
    }
}
