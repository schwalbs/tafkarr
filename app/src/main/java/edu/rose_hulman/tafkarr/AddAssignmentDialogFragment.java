package edu.rose_hulman.tafkarr;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;

public class AddAssignmentDialogFragment extends DialogFragment {
    public interface addAssignmentDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String assignmentName, String catName, int assignmentGrade);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    addAssignmentDialogListener mListener;
    String assignmentName;
    EditText assignmentNameEditText;
    int grade;
    String category;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (addAssignmentDialogListener) activity;
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
        builder.setView(R.layout.dialog_add_assignment);
        View layoutView = inflater.inflate(R.layout.dialog_add_assignment, null);
        builder.setView(layoutView);
        assignmentNameEditText = (EditText) layoutView.findViewById(R.id.assignment_name);
        final Spinner mCatSpin = (Spinner) layoutView.findViewById(R.id.spinner2);
        mCatSpin.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, CourseFragment.listCategories));
        final EditText mAssignmentGradeField = (EditText) layoutView.findViewById(R.id.assignment_grade);
        mAssignmentGradeField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                grade = Integer.valueOf(s.toString());
            }
        });
        builder.setTitle("Add an assignment");
        builder
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        Log.d("HHH", "ONDIALOGPOSITIVECLICK:"+ assignmentNameEditText.getText().toString());
                        mListener.onDialogPositiveClick(AddAssignmentDialogFragment.this, assignmentNameEditText.getText().toString(), mCatSpin.getSelectedItem().toString(), grade);
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
}