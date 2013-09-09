package com.rittzz.android.whowin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.rittzz.android.whowin.util.Extras;

public class SportActivity extends BaseActivity {

    private long sportId;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);

        if (getIntent().hasExtra(Extras.SPORT_ID)) {
            sportId = getIntent().getLongExtra(Extras.SPORT_ID, -1);
        }
        else {
            throw new IllegalArgumentException("You must pass a sport id to this activity");
        }

        populateViews();
    }

    private void populateViews() {
        final String title = getIntent().getStringExtra(Extras.SPORT_NAME);
        getSupportActionBar().setTitle(title);

        final String description = getIntent().getStringExtra(Extras.SPORT_DESCRIPTION);
        final TextView descriptionTv = (TextView) findViewById(R.id.tv_description);
        descriptionTv.setText(description);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getSupportMenuInflater().inflate(R.menu.sport, menu);
        return true;
    }

    public static Intent makeIntent(final Context ctx, final long sportId, final String name, final String description) {
        final Intent intent = new Intent(ctx, SportActivity.class);

        intent.putExtra(Extras.SPORT_ID, sportId);
        intent.putExtra(Extras.SPORT_NAME, name);
        intent.putExtra(Extras.SPORT_DESCRIPTION, description);

        return intent;
    }
}
