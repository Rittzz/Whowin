package com.rittzz.android.whowin.util;

import java.util.HashMap;

/**
 * Creates unique IDs intended to identify a particular Loader task within
 * a particular class.  This eases the management of unique IDs when
 * activities or fragments are subclassed and both superclas and subclass
 * need to perform loader activities without their loader IDs colliding.
 */

public class LoaderIdManager {

    private static final LoaderIdManager instance = new LoaderIdManager();

    private final HashMap<String, Integer> loaderIdMap = new HashMap<String, Integer>();
    private int nextId;

    private LoaderIdManager() {
    }

    public static LoaderIdManager getInstance() {
        return instance;
    }

    public synchronized int getLoaderIdForTask(final Class<?> clazz, final int task_id) {
        final String key = clazz.getCanonicalName() + '.' + task_id;
        Integer id = loaderIdMap.get(key);
        if (id == null) {
            id = nextId;
            loaderIdMap.put(key, id);
            nextId++;
        }
        return id;
    }

    /**
     * Returns a generated unique id.  This will not conflict with the id generated from {@link #getLoaderIdForTask(Class, int)}.
     * @return
     */
    public synchronized int getLoaderIdForTask() {
        return nextId++;
    }
}
