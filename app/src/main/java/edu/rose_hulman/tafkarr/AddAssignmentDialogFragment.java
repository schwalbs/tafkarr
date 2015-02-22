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
import android.widget.Spinner;

public class AddAssignmentDialogFragment extends DialogFragment {
    public interface AddAssignmentDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String assignmentName, String catName, int assignmentGrade);

        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    private AddAssignmentDialogListener mListener;
    private String mAssignmentName;
    private String mGrade;
    private Spinner mCatSpin;
    private EditText assignmentNameEditText;
    private EditText mAssignmentGradeField;
    private String category;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (AddAssignmentDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString() + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(R.layout.dialog_add_assignment);
        View layoutView = inflater.inflate(R.layout.dialog_add_assignment, null);
        builder.setView(layoutView);

        assignmentNameEditText = (EditText) layoutView.findViewById(R.id.assignment_name);

        mCatSpin = (Spinner) layoutView.findViewById(R.id.category_picker);

//        mCatSpin.setAdapter(new SimpleCursorAdapter(getActivity(), android.R.layout.simple_spinner_item, );
        //new ArrayAdapter<String>(getActivity(),
        //android.R.layout.simple_spinner_item, CourseFragment.listCategories));

        mAssignmentGradeField = (EditText) layoutView.findViewById(R.id.assignment_grade);

        builder.setTitle(getActivity().getString(R.string.add_assignment_dialog_title));
        builder
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // do nothing so it can be overriden later

                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        mListener.onDialogNegativeClick(AddAssignmentDialogFragment.this);
                    }
                });
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
                    mAssignmentName = assignmentNameEditText.getText().toString().trim();
                    mGrade = mAssignmentGradeField.getText().toString().trim();
                    if (mAssignmentName.isEmpty()) {
                        assignmentNameEditText.setError(getActivity().getString(R.string.required));
                    } else if (mGrade.isEmpty()) {
                        mAssignmentGradeField.setError(getActivity().getString(R.string.required));
                    } else {
                        int grade = Integer.parseInt(mGrade);
                        mListener.onDialogPositiveClick(AddAssignmentDialogFragment.this, mAssignmentName, mCatSpin.getSelectedItem().toString(), grade);
                        dismiss();
                    }
                }
            });
        }
    }

}