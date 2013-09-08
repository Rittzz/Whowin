package com.rittzz.android.whowin;

import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.rittzz.android.whowin.util.ViewServer;

public abstract class BaseActivity extends SherlockFragmentActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewServer.get(this).addWindow(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewServer.get(this).removeWindow(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ViewServer.get(this).setFocusedWindow(this);
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        final MenuItem settingsMenuItem = menu.findItem(R.id.action_settings);
        if (settingsMenuItem != null) {
            settingsMenuItem.setIntent(new Intent(this, SettingsActivity.class));
        }
        return true;
    }
}
