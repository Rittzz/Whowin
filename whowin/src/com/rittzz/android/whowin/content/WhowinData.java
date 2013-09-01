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

    public final static class SportPlayerGames {
        private SportPlayerGames() {
        };

        public static final String _ID = GameTable.COLUMN_ID;

        public static final String PLAYER_1_ID = GameTable.COLUMN_PLAYER_1_ID;
        public static final String PLAYER_2_ID = GameTable.COLUMN_PLAYER_2_ID;

        public static final String PLAYER_1_NAME = "player_1_name";
        public static final String PLAYER_2_NAME = "player_2_name";

        public static final String PLAYER_1_GAMES = GameTable.COLUMN_PLAYER_1_GAMES;
        public static final String PLAYER_2_GAMES = GameTable.COLUMN_PLAYER_2_GAMES;

        public static final String TIMESTAMP = GameTable.COLUMN_TIMESTAMP;
    }

    public static final class PlayersJoinedGames {
        private PlayersJoinedGames() {
        };

        static HashMap<String, String> playersJoinedGamesProjectionMap = new HashMap<String, String>();

        static {
            playersJoinedGamesProjectionMap.put(PlayerTable.COLUMN_ID, "t1."
                + PlayerTable.COLUMN_ID);
            playersJoinedGamesProjectionMap.put(PlayerTable.COLUMN_NAME, "t."
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
