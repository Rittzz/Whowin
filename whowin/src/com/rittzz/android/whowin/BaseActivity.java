package com.rittzz.android.whowin;

import android.content.Intent;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public abstract class BaseActivity extends SherlockFragmentActivity {

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        final MenuItem settingsMenuItem = menu.findItem(R.id.action_settings);
        if (settingsMenuItem != null) {
            settingsMenuItem.setIntent(new Intent(this, SettingsActivity.class));
        }
        return true;
    }
}
