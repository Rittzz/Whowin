package com.rittzz.android.whowin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.rittzz.android.whowin.fragment.SportListFragment;

public class HomeActivity extends BaseActivity implements SportListFragment.ItemClickListener {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getSupportMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        menu.findItem(R.id.action_settings).setIntent(new Intent(this, SettingsActivity.class));
        return true;
    }

    @Override
    public void onItemClick(final long sportId) {
        Toast.makeText(this, "Clicked Id " + sportId, Toast.LENGTH_SHORT).show();
    }
}
