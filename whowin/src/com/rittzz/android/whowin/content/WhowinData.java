package com.rittzz.android.whowin.content;

import android.net.Uri;

public class WhowinData {

    private WhowinData() {};

    static final String AUTHORITY = MainContentProvider.AUTHORITY;

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
}
