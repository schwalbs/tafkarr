package edu.rose_hulman.tafkarr;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;

public class AddClassDialogFragment extends DialogFragment {
    public interface addClassDialogListener {
        public void onClassConfirmClick(DialogFragment dialog, String className);
        public void onClassDenyClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    String mClassName;
    String mClassTarget;
    addClassDialogListener mListener;
    String mTag;

    public AddClassDialogFragment (){

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (addClassDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layoutView = inflater.inflate(R.layout.dialog_add_class, null);
        builder.setView(layoutView);
        final EditText mClassNameField = (EditText) layoutView.findViewById(R.id.class_name);
//        final EditText mClassTargetField = (EditText) layoutView.findViewById(R.id.class_target);

        builder.setTitle("Add a Class");
        builder
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        mClassName = mClassNameField.getText().toString();
//                        mClassTarget = mClassTargetField.getText().toString();
                        mListener.onClassConfirmClick(AddClassDialogFragment.this, mClassName);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        mListener.onClassDenyClick(AddClassDialogFragment.this);
                    }
                });
        return builder.create();
    }
}