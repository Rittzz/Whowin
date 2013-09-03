package com.rittzz.android.whowin;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.support.v4.app.NavUtils;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.rittzz.android.whowin.content.MainDatabaseHelper;
import com.rittzz.android.whowin.content.TestData;
import com.rittzz.android.whowin.content.WhowinData;

public class SettingsActivity extends SherlockPreferenceActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    private void setupActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupSimplePreferencesScreen();
    }

    private void setupSimplePreferencesScreen() {
        addPreferencesFromResource(R.xml.pref_general);

        findPreference("erase_data").setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(final Preference preference) {
                MainDatabaseHelper.getInstance(getApplication()).reset();
                getContentResolver().notifyChange(WhowinData.CONTENT_URI, null);
                Toast.makeText(getApplication(), "Data Cleared", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        findPreference("test_data").setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(final Preference preference) {
                TestData.testDatabase(getApplication());
                getContentResolver().notifyChange(WhowinData.CONTENT_URI, null);
                Toast.makeText(getApplication(), "Test Complete", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}
