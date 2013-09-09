package com.rittzz.android.whowin.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.rittzz.android.whowin.R;
import com.rittzz.android.whowin.util.Extras;

public abstract class ScoreboardFragment extends SherlockFragment {

    private long sportId;

    private TextView titleTextView;
    private TableLayout tableLayout;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sportId = getArguments().getLong(Extras.SPORT_ID);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_scoreboard, container, false);

        titleTextView = (TextView) root.findViewById(R.id.board_title);
        tableLayout = (TableLayout) root.findViewById(R.id.table_layout);

        return root;
    }

    protected long getSportId() {
        return sportId;
    }

    protected void setBoardTitle(final String title) {
        titleTextView.setText(title);
    }

    protected TableLayout getTableLayout() {
        return tableLayout;
    }
}
