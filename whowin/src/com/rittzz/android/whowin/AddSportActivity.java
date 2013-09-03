package com.rittzz.android.whowin;

import java.lang.ref.WeakReference;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.rittzz.android.whowin.content.SportTable;
import com.rittzz.android.whowin.content.WhowinData;
import com.rittzz.android.whowin.fragment.ProgressDialogFragment;

public class AddSportActivity extends BaseActivity {

    private static final String PROGRESS_DIALOG_TAG = "add_progress_dialog";

    private EditText nameEditText;
    private EditText descriptionEditText;

    private AddSportTask addSportTask;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sport);

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);

        findViewById(R.id.addSportButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                addSport();
            }
        });

        setupActionBar();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getSupportMenuInflater().inflate(R.menu.add_sport, menu);
        return true;
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
    protected void onDestroy() {
        super.onDestroy();
        if (addSportTask != null) {
            addSportTask.cancel(true);
            addSportTask = null;
        }
    }

    private void addSport() {
        final String name = nameEditText.getText().toString();
        final String description = descriptionEditText.getText().toString();

        // Error Checking
        if (name == null || name.length() == 0) {
            Toast.makeText(this, "The name is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        final ContentValues values = new ContentValues();
        values.put(SportTable.COLUMN_NAME, name);
        values.put(SportTable.COLUMN_DESCRIPTION, description);

        addSportTask = new AddSportTask(this, values);
        addSportTask.execute();
    }

    private static class AddSportTask extends AsyncTask<Void, Void, Boolean> {

        private final WeakReference<AddSportActivity> wref;
        private final ContentValues values;

        public AddSportTask(final AddSportActivity ctx, final ContentValues values) {
            wref = new WeakReference<AddSportActivity>(ctx);
            this.values = values;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            final AddSportActivity baseContext = wref.get();
            if (baseContext != null) {
                final ProgressDialogFragment frag = ProgressDialogFragment.newInstance(null, baseContext.getResources().getString(R.string.adding_dot_dot_dot));
                frag.show(baseContext.getSupportFragmentManager(), PROGRESS_DIALOG_TAG);
            }
        }

        @Override
        protected Boolean doInBackground(final Void... params) {
            AddSportActivity baseContext = wref.get();
            if (baseContext != null) {
                final Context context = baseContext.getApplication();
                baseContext = null;

                final Uri insertUri = context.getContentResolver().insert(WhowinData.SPORTS_CONTENT_URI, values);
                if (insertUri != null) {
                    return true;
                }
                else {
                    return false;
                }
            }
            return false;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            final AddSportActivity baseContext = wref.get();
            if (baseContext != null) {
                final ProgressDialogFragment frag = (ProgressDialogFragment) baseContext.getSupportFragmentManager().findFragmentByTag(PROGRESS_DIALOG_TAG);
                if (frag != null) {
                    frag.dismiss();
                }
            }
        }

        @Override
        protected void onPostExecute(final Boolean result) {
            super.onPostExecute(result);
            final AddSportActivity baseContext = wref.get();
            if (baseContext != null) {
                baseContext.addSportTask = null;

                if (result) {
                    Toast.makeText(baseContext, "Added", Toast.LENGTH_SHORT).show();
                    baseContext.finish();
                }
                else {
                    Toast.makeText(baseContext, "Error adding sport, name in use", Toast.LENGTH_SHORT).show();
                }

                final ProgressDialogFragment frag = (ProgressDialogFragment) baseContext.getSupportFragmentManager().findFragmentByTag(PROGRESS_DIALOG_TAG);
                if (frag != null) {
                    frag.dismiss();
                }
            }
        }
    }
}
