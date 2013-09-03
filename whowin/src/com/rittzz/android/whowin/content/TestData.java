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
        MainDatabaseHelper.getInstance(context).reset();

        // Add Test Data
        addTestData(context, "Pool");
        addTestData(context, "Fooseball");

        // Query Test Data

        // Get the SportId
        long sportId = -1;
        {
            final Uri uri = WhowinData.SPORTS_CONTENT_URI;
            final Cursor cursor = context.getContentResolver().query(uri, null, null, null, WhowinData.Sport.NAME + " ASC");
            Log.d("TestData", "Sports");
            while (cursor.moveToNext()) {
                sportId = cursor.getLong(cursor.getColumnIndexOrThrow(SportTable.COLUMN_ID));
                Log.d("TestData", "DATA - " + cursorToString(cursor));
            }
            cursor.close();
        }

        // Query the Players
        {
            final Uri uri = WhowinData.PLAYERS_CONTENT_URI;
            final Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            Log.d("TestData", "Players");
            while (cursor.moveToNext()) {
                Log.d("TestData", "DATA - " + cursorToString(cursor));
            }
            cursor.close();
        }

        // Query the Games
        {
            final Uri uri = WhowinData.GAMES_CONTENT_URI;
            final Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            Log.d("TestData", "Games");
            while (cursor.moveToNext()) {
                Log.d("TestData", "DATA - " + cursorToString(cursor));
            }
            cursor.close();
        }

        // Query the Players
        {
            final Uri uri = WhowinData.createSportIdGamesContentUri(sportId);
            final Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            Log.d("TestData", "Games for Sport " + sportId);
            while (cursor.moveToNext()) {
                Log.d("TestData", "DATA - " + cursorToString(cursor));
            }
            cursor.close();
        }

        // Query the Players for a Sport
        {
            final Uri uri = WhowinData.createSportIdPlayersContentUri(sportId);
            final Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            Log.d("TestData", "Players for Sport " + sportId);
            while (cursor.moveToNext()) {
                Log.d("TestData", "DATA - " + cursorToString(cursor));
            }
            cursor.close();
        }
    }

    private static int name_counter = 0;
    private static boolean names_add = false;

    public static void addTestData(final Context context, final String sportName) {
        final SQLiteDatabase db = MainDatabaseHelper.getInstance(context).getWritableDatabase();
        db.beginTransaction();
        try {
            // A Sport first
            final ContentValues sportValues = new ContentValues();
            sportValues.put(SportTable.COLUMN_NAME, sportName);
            sportValues.put(SportTable.COLUMN_DESCRIPTION, "The awesome soundhound pool stats");
            final long sportId = db.insertOrThrow(SportTable.TABLE_NAME, null, sportValues);

            // Then some players
            final ArrayList<Long> playerIds = new ArrayList<Long>();
            if (!names_add) {
                final int totalPlayers = 10;
                for (int i = 0; i < totalPlayers; i++) {
                    final ContentValues values = new ContentValues();
                    values.put(PlayerTable.COLUMN_NAME, "Name " + name_counter++);
                    playerIds.add(db.insertOrThrow(PlayerTable.TABLE_NAME, null, values));
                }
                names_add = true;
            }
            else {
                // Query the existing players
                final String limit = "5";
                final Cursor cursor = db.query(PlayerTable.TABLE_NAME, new String[] {PlayerTable.COLUMN_ID}, null, null, null, null, null, limit);
                while (cursor.moveToNext()) {
                    playerIds.add(cursor.getLong(cursor.getColumnIndexOrThrow(PlayerTable.COLUMN_ID)));
                }
                cursor.close();
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
        str.append('[');
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            final String columnName = cursor.getColumnName(i);
            final String value = cursor.getString(i);

            str.append(String.format("'%s'='%s'", columnName, value));
            if (i < cursor.getColumnCount() - 1) {
                str.append(", ");
            }
        }
        str.append(']');
        return str.toString();
    }
}
