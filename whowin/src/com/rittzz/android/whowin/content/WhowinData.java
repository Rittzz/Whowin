package com.rittzz.android.whowin.content;

import java.util.HashMap;
import java.util.Locale;

import android.net.Uri;

public class WhowinData {

    private WhowinData() {
    };

    private static final String AUTHORITY = MainContentProvider.AUTHORITY;

    private static final String RAW_CONTENT_URI = "content://" + AUTHORITY;

    public static final Uri CONTENT_URI = Uri.parse(RAW_CONTENT_URI);

    public final static class Sport {
        private Sport() {};

        public static final String _ID = "_id";
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";

        static final String TABLE_NAME = SportTable.TABLE_NAME;
        static final HashMap<String, String> projectionMap = new HashMap<String, String>();
        static {
            projectionMap.put(_ID, SportTable.COLUMN_ID);
            projectionMap.put(NAME, SportTable.COLUMN_NAME);
            projectionMap.put(DESCRIPTION, SportTable.COLUMN_DESCRIPTION);
        }
    }

    public final static class Player {
        private Player() {};

        public static final String _ID = "_id";
        public static final String NAME = "name";

        static final String TABLE_NAME = SportTable.TABLE_NAME;
        static final HashMap<String, String> projectionMap = new HashMap<String, String>();
        static {
            projectionMap.put(_ID, PlayerTable.COLUMN_ID);
            projectionMap.put(NAME, PlayerTable.COLUMN_NAME);
        }
    }

    public final static class Game {
        private Game() {};

        public static final String _ID = "_id";
        public static final String SPORT_ID = "sport_id";

        public static final String NAME = "name";

        public static final String PLAYER_1_ID = "player_1_id";
        public static final String PLAYER_2_ID = "player_2_id";

        public static final String PLAYER_1_GAMES = "player_1_games";
        public static final String PLAYER_2_GAMES = "player_2_games";

        public static final String TIMESTAMP = "timestamp";

        // Joind Columns
        public static final String PLAYER_1_NAME = "player_1_name";
        public static final String PLAYER_2_NAME = "player_2_name";

        // Always use the joined table
        static final String TABLE_NAME = GameTable.TABLE_NAME + " t" + " JOIN "
            + PlayerTable.TABLE_NAME + " t1 ON t1." + PlayerTable.COLUMN_ID + " = t."
            + GameTable.COLUMN_PLAYER_1_ID + " JOIN " + PlayerTable.TABLE_NAME + " t2 ON t2."
            + PlayerTable.COLUMN_ID + " = t." + GameTable.COLUMN_PLAYER_2_ID;

        static final HashMap<String, String> projectionMap = new HashMap<String, String>();
        static {
            projectionMap.put(_ID, "t." + GameTable.COLUMN_ID);
            projectionMap.put(SPORT_ID, "t." + GameTable.COLUMN_SPORT_ID);

            projectionMap.put(NAME, "t." + GameTable.COLUMN_NAME);

            projectionMap.put(PLAYER_1_ID, "t." + GameTable.COLUMN_PLAYER_1_ID);
            projectionMap.put(PLAYER_2_ID, "t." + GameTable.COLUMN_PLAYER_2_ID);

            projectionMap.put(PLAYER_1_GAMES, "t." + GameTable.COLUMN_PLAYER_1_GAMES);
            projectionMap.put(PLAYER_2_GAMES, "t." + GameTable.COLUMN_PLAYER_2_GAMES);

            projectionMap.put(PLAYER_1_NAME, "t1." + PlayerTable.COLUMN_NAME + " AS " + GameTable.COLUMN_PLAYER_1_NAME);
            projectionMap.put(PLAYER_2_NAME, "t2." + PlayerTable.COLUMN_NAME + " AS " + GameTable.COLUMN_PLAYER_2_NAME);

            projectionMap.put(TIMESTAMP, "t." + GameTable.COLUMN_TIMESTAMP);
        }
    }

    // Special Column names used for tables that are joined

//    public final static class SportGames {
//        private SportGames() {};
//
//        static HashMap<String, String> projectionMap = new HashMap<String, String>();
//
//        static final String WHERE_SPORT_ID = "t." + GameTable.COLUMN_SPORT_ID;
//        static {
//            projectionMap.put(GameTable.COLUMN_ID, "t." + GameTable.COLUMN_ID);
//            projectionMap.put(GameTable.COLUMN_SPORT_ID, "t." + GameTable.COLUMN_SPORT_ID);
//
//            projectionMap.put(GameTable.COLUMN_PLAYER_1_ID, "t." + GameTable.COLUMN_PLAYER_1_ID);
//            projectionMap.put(GameTable.COLUMN_PLAYER_2_ID, "t." + GameTable.COLUMN_PLAYER_2_ID);
//
//            projectionMap.put(GameTable.COLUMN_PLAYER_1_GAMES, "t." + GameTable.COLUMN_PLAYER_1_GAMES);
//            projectionMap.put(GameTable.COLUMN_PLAYER_2_GAMES, "t." + GameTable.COLUMN_PLAYER_2_GAMES);
//
//            projectionMap.put(GameTable.COLUMN_PLAYER_1_NAME, "t1." + PlayerTable.COLUMN_NAME + " AS " + GameTable.COLUMN_PLAYER_1_NAME);
//            projectionMap.put(GameTable.COLUMN_PLAYER_2_NAME, "t2." + PlayerTable.COLUMN_NAME + " AS " + GameTable.COLUMN_PLAYER_2_NAME);
//
//            projectionMap.put(GameTable.COLUMN_TIMESTAMP, "t." + GameTable.COLUMN_TIMESTAMP);
//        }
//
//        // Helpers to reduce massive confusion
//        static final String TABLE_NAME = GameTable.TABLE_NAME;
//
//        // Helpers to reduce massive confusion
//        static final String JOINED_TABLE_NAME = GameTable.TABLE_NAME + " t" + " JOIN "
//            + PlayerTable.TABLE_NAME + " t1 ON t1." + PlayerTable.COLUMN_ID + " = t."
//            + GameTable.COLUMN_PLAYER_1_ID + " JOIN " + PlayerTable.TABLE_NAME + " t2 ON t2."
//            + PlayerTable.COLUMN_ID + " = t." + GameTable.COLUMN_PLAYER_2_ID;
//    }

    public static final class PlayersWithSport {
        private PlayersWithSport() {
        };

        static HashMap<String, String> projectionMap = new HashMap<String, String>();

        static {
            projectionMap.put(Player._ID, "t1."
                + PlayerTable.COLUMN_ID);
            projectionMap.put(Player.NAME, "t."
                + PlayerTable.COLUMN_NAME);
        }

        // Helpers to reduce confusion
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

    public static Uri SPORTS_CONTENT_URI = Uri.parse(RAW_CONTENT_URI + "/sports" );
    public static Uri PLAYERS_CONTENT_URI = Uri.parse(RAW_CONTENT_URI + "/players" );
    public static Uri GAMES_CONTENT_URI = Uri.parse(RAW_CONTENT_URI + "/games" );

    public static Uri createPlayerIdContentUri(final long playerId) {
        return PLAYERS_CONTENT_URI.buildUpon().appendPath(Long.toString(playerId)).build();
    }

    public static Uri createSportIdPlayersContentUri(final long sportId) {
        return SPORTS_CONTENT_URI.buildUpon().appendPath(Long.toString(sportId))
            .appendPath("players").build();
    }

    public static Uri createSportIdPlayerIdContentUri(final long sportId, final long playerId) {
        return SPORTS_CONTENT_URI.buildUpon().appendPath(Long.toString(sportId))
            .appendPath("player").appendPath(Long.toString(playerId)).build();
    }

    public static Uri createSportIdGamesContentUri(final long sportId) {
        return SPORTS_CONTENT_URI.buildUpon().appendPath(Long.toString(sportId))
            .appendPath("games").build();
    }

    public static Uri createSportIdGameIdContentUri(final long sportId, final long gameId) {
        return SPORTS_CONTENT_URI.buildUpon().appendPath(Long.toString(sportId))
            .appendPath("game").appendPath(Long.toString(gameId)).build();
    }
}
