package com.rittzz.android.whowin;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.rittzz.android.whowin.content.TestData;

public class HomeActivity extends SherlockFragmentActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TestData.testDatabase(this);
    }

}
