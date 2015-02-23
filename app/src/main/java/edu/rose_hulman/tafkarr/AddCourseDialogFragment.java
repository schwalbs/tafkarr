package edu.rose_hulman.tafkarr;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class AddCourseDialogFragment extends DialogFragment {
    private static final String KEY_COURSE_NAME = "key course";
    private static final String KEY_COURSE_ID = "key iden";
    // Use this instance of the interface to deliver action events
    private String mClassName;
    private EditText mClassNameField;
    private AddCourseDialogListener mListener;
    public AddCourseDialogFragment() {

    }
    public static AddCourseDialogFragment newInstance(String name, long iD) {
        Bundle args = new Bundle();
        args.putString(KEY_COURSE_NAME, name);
        args.putLong(KEY_COURSE_ID, iD);
        AddCourseDialogFragment dF = new AddCourseDialogFragment();
        dF.setArguments(args);
        return (dF);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (AddCourseDialogListener) activity;
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
        mClassNameField = (EditText) layoutView.findViewById(R.id.class_name);
        if(getArguments()!=null) {
            mClassNameField.setText(getArguments().getString(KEY_COURSE_NAME));
        }
        builder.setTitle(getActivity().getString(R.string.add_class));
        builder
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do nothing so it can be overriden later
                    }
                })
                .setNegativeButton(android.R.string.cancel, null);
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            //override positive button so it won't automatically close
            dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Send the positive button event back to the host activity

                    mClassName = mClassNameField.getText().toString().trim();
                    if (mClassName.isEmpty()) {
                        mClassNameField.setError(getActivity().getString(R.string.required));
                    } else {
                        mListener.onCourseConfirmClick(AddCourseDialogFragment.this, mClassName);
                        dismiss();
                    }
                }
            });
        }
    }


    public interface AddCourseDialogListener {
        public void onCourseConfirmClick(DialogFragment dialog, String className);
    }
}