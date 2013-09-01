package com.rittzz.android.whowin.content;

import java.util.HashMap;
import java.util.Locale;

import android.net.Uri;

public class WhowinData {

    private WhowinData() {
    };

    static final String AUTHORITY = MainContentProvider.AUTHORITY;

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Special Column names used for tables that are joined

    public final static class SportGames {
        private SportGames() {
        };

        static HashMap<String, String> projectionMap = new HashMap<String, String>();

        static final String WHERE_SPORT_ID = "t." + GameTable.COLUMN_SPORT_ID;

        static {
            projectionMap.put(GameTable.COLUMN_ID, "t." + GameTable.COLUMN_ID);
            projectionMap.put(GameTable.COLUMN_SPORT_ID, "t." + GameTable.COLUMN_SPORT_ID);

            projectionMap.put(GameTable.COLUMN_PLAYER_1_ID, "t." + GameTable.COLUMN_PLAYER_1_ID);
            projectionMap.put(GameTable.COLUMN_PLAYER_2_ID, "t." + GameTable.COLUMN_PLAYER_2_ID);

            projectionMap.put(GameTable.COLUMN_PLAYER_1_GAMES, "t." + GameTable.COLUMN_PLAYER_1_GAMES);
            projectionMap.put(GameTable.COLUMN_PLAYER_2_GAMES, "t." + GameTable.COLUMN_PLAYER_2_GAMES);

            projectionMap.put(GameTable.COLUMN_PLAYER_1_NAME, "t1." + PlayerTable.COLUMN_NAME + " AS " + GameTable.COLUMN_PLAYER_1_NAME);
            projectionMap.put(GameTable.COLUMN_PLAYER_2_NAME, "t2." + PlayerTable.COLUMN_NAME + " AS " + GameTable.COLUMN_PLAYER_2_NAME);

            projectionMap.put(GameTable.COLUMN_TIMESTAMP, "t." + GameTable.COLUMN_TIMESTAMP);
        }

        // Helpers to reduce massive confusion
        static final String TABLE_NAME = GameTable.TABLE_NAME + " t" + " JOIN "
            + PlayerTable.TABLE_NAME + " t1 ON t1." + PlayerTable.COLUMN_ID + " = t."
            + GameTable.COLUMN_PLAYER_1_ID + " JOIN " + PlayerTable.TABLE_NAME + " t2 ON t2."
            + PlayerTable.COLUMN_ID + " = t." + GameTable.COLUMN_PLAYER_2_ID;
    }

    public static final class PlayersWithSport {
        private PlayersWithSport() {
        };

        static HashMap<String, String> projectionMap = new HashMap<String, String>();

        static {
            projectionMap.put(PlayerTable.COLUMN_ID, "t1."
                + PlayerTable.COLUMN_ID);
            projectionMap.put(PlayerTable.COLUMN_NAME, "t."
                + PlayerTable.COLUMN_NAME);
        }

        // Helpers to reduce massive confusion
        private static final String TABLE_NAME = PlayerTable.TABLE_NAME + " t JOIN " + "(%1$s) t1 ON t1."
            + PlayerTable.COLUMN_ID + " = t." + PlayerTable.COLUMN_ID + ";";

        private static final String UNION_QUERY = "SELECT "
            + GameTable.COLUMN_PLAYER_1_ID + " AS " + PlayerTable.COLUMN_ID + " FROM "
            + GameTable.TABLE_NAME + " WHERE " + GameTable.COLUMN_SPORT_ID + "=%1$d"
            + " UNION " +
            "SELECT " + GameTable.COLUMN_PLAYER_2_ID + " AS "
            + PlayerTable.COLUMN_ID + " FROM " + GameTable.TABLE_NAME + " WHERE " + GameTable.COLUMN_SPORT_ID + "=%1$d";

        static String getTableNameForSportId(final long sportId) {
            final String unionQueryWithSportId = String.format(Locale.US, UNION_QUERY, sportId);
            return String.format(Locale.US, TABLE_NAME, unionQueryWithSportId);
        }
    }
}
