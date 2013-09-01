package com.rittzz.android.whowin.content;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class TestData {
    public static void addGameTableEntries(final Context context) {
        // TODO Add test data to test out the content provider

        final Uri queryUri = WhowinData.CONTENT_URI.buildUpon().appendPath("sports").build();;

        final Cursor cursor = context.getContentResolver().query(queryUri, null, null, null, null);
        cursor.close();
    }
}
