package com.rittzz.android.whowin.fragment;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.rittzz.android.whowin.content.WhowinData;
import com.rittzz.android.whowin.util.Extras;
import com.rittzz.android.whowin.util.LoaderIdManager;

public class AllTimeWinnersFragment extends ScoreboardFragment {

    private final List<PlayerRow> playerRows = new ArrayList<PlayerRow>();

    public AllTimeWinnersFragment newInstance(final long sportId) {
        final AllTimeWinnersFragment frag = new AllTimeWinnersFragment();
        final Bundle args = new Bundle();

        args.putLong(Extras.SPORT_ID, sportId);

        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setBoardTitle("All Time");
    }

    private void load() {
        final LoaderManager.LoaderCallbacks<Cursor> callback = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(final int loaderId, final Bundle args) {
                final Uri uri = WhowinData.createSportIdGamesContentUri(getSportId());
                final String[] projection = new String[] {
                        WhowinData.Game.PLAYER_1_NAME,
                        WhowinData.Game.PLAYER_1_GAMES,
                        WhowinData.Game.PLAYER_2_NAME,
                        WhowinData.Game.PLAYER_2_GAMES };

                return new CursorLoader(getActivity(), uri, projection, null, null, null);
            }

            @Override
            public void onLoadFinished(final Loader<Cursor> loader, final Cursor cursor) {

            }

            @Override
            public void onLoaderReset(final Loader<Cursor> loader) {

            }
        };

        final int loaderId = LoaderIdManager.getInstance().getLoaderIdForTask(AllTimeWinnersFragment.class, 0);
        getLoaderManager().initLoader(loaderId, null, callback);
    }

    private static class PlayerRow {
        private final String name;
        private final int wins;

        public PlayerRow(final String name, final int wins) {
            this.name = name;
            this.wins = wins;
        }
    }
}
