package edu.rose_hulman.tafkarr;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by gartzkds on 2/21/2015.
 */
public class ProgressDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View layoutView = inflater.inflate(R.layout.dialog_progress, null);
        builder.setView(layoutView);

        return builder.create();
    }
}
