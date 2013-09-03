package com.rittzz.android.whowin.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class ProgressDialogFragment extends SherlockDialogFragment {

    public static ProgressDialogFragment newInstance(final String title, final String message) {
        final ProgressDialogFragment f = new ProgressDialogFragment();
        f.setArguments(new Bundle());
        f.getArguments().putString("title", title);
        f.getArguments().putString("message", message);
        return f;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle(getArguments().getString("title"));
        dialog.setMessage(getArguments().getString("message"));
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        return dialog;
    }
}
