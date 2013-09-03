package com.rittzz.android.whowin.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.rittzz.android.whowin.AddSportActivity;
import com.rittzz.android.whowin.R;
import com.rittzz.android.whowin.content.WhowinData;

public class SportListFragment extends SherlockFragment implements AdapterView.OnItemClickListener {

    private ItemClickListener clickListener;

    private ListView listView;
    private View emptyView;
    private View loadingView;

    private Cursor cursor;

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof ItemClickListener)) {
            throw new ClassCastException("Attach activity must be of type " + ItemClickListener.class);
        }

        clickListener = (ItemClickListener) activity;
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_sport_list, menu);
    }

    @Override
    public void onPrepareOptionsMenu(final Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_new_sport).setIntent(new Intent(getActivity(), AddSportActivity.class));
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_sport_list, container, false);

        listView = (ListView) root.findViewById(R.id.listView);
        emptyView = root.findViewById(R.id.emptyView);
        loadingView = root.findViewById(R.id.progressBar);

        return root;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView.setOnItemClickListener(this);

        listView.setVisibility(View.INVISIBLE);
        loadingView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.INVISIBLE);

        final LoaderManager.LoaderCallbacks<Cursor> loaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(final int loaderId, final Bundle bundle) {
                final Uri uri = WhowinData.SPORTS_CONTENT_URI;
                final String[] projection = new String[] { WhowinData.Sport._ID, WhowinData.Sport.NAME };

                return new CursorLoader(getActivity(), uri, projection, null, null, WhowinData.Sport.NAME + " ASC");
            }

            @Override
            public void onLoadFinished(final Loader<Cursor> loader, final Cursor cursor) {
                loadingView.setVisibility(View.INVISIBLE);

                if (cursor.getCount() > 0) {
                    listView.setVisibility(View.VISIBLE);

                    SportListFragment.this.cursor = cursor;
                    final SportCursorAdapter cursorAdapter = new SportCursorAdapter(getActivity(), cursor);
                    listView.setAdapter(cursorAdapter);
                }
                else {
                    emptyView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onLoaderReset(final Loader<Cursor> loader) {
                // Clear the data.
                SportListFragment.this.cursor = null;
                listView.setAdapter(null);
            }
        };
        getLoaderManager().initLoader(0, null, loaderCallback);
    }


    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        if (cursor != null) {
            cursor.moveToPosition(position);
            final long sportId = cursor.getLong(cursor.getColumnIndexOrThrow(WhowinData.Sport._ID));
            clickListener.onItemClick(sportId);
        }
    }

    private class SportCursorAdapter extends CursorAdapter {

        public SportCursorAdapter(final Context context, final Cursor c) {
            super(context, c, FLAG_REGISTER_CONTENT_OBSERVER);

        }

        @Override
        public void bindView(final View item, final Context ctx, final Cursor cursor) {
            final TextView tv = (TextView)item.findViewById(android.R.id.text1);
            tv.setText(cursor.getString(cursor.getColumnIndexOrThrow(WhowinData.Sport.NAME)));
        }

        @Override
        public View newView(final Context ctx, final Cursor cursor, final ViewGroup parent) {
            final LayoutInflater inflater = getActivity().getLayoutInflater();
            return inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }

    }

    public interface ItemClickListener {
        public void onItemClick(final long sportId);
    }
}
