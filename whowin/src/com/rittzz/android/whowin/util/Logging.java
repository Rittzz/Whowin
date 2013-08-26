package com.rittzz.android.whowin.util;

public final class Logging {

    private static final String LOG_PREFIX = "ww:";

    private Logging() {}

    public static String makeLogTag(final Class<?> c) {
        final String full = LOG_PREFIX + c.getSimpleName();
        return full.length() > 23 ? full.substring(0, 23) : full;
    }
}
