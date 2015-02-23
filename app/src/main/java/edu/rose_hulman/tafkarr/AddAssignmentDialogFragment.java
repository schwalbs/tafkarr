package edu.rose_hulman.tafkarr;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

public class AddAssignmentDialogFragment extends DialogFragment {
    private static final String KEY_ASS_NAME = "name key";
    private static final String KEY_ASS_GRADE = "grade key";
    private static final String KEY_ASS_ID = "ID key";

    // Use this instance of the interface to deliver action events
    private AddAssignmentDialogListener mListener;
    private String mAssignmentName;
    private String mGrade;
    private Spinner mCatSpin;
    private EditText assignmentNameEditText;
    private EditText mAssignmentGradeField;
    private Cursor categories;
    private long mID;
    private String category;

    public AddAssignmentDialogFragment() {

    }

    public static AddAssignmentDialogFragment newInstance(String name, double grade, long iD) {
        Bundle args = new Bundle();
        args.putString(KEY_ASS_NAME, name);
        args.putDouble(KEY_ASS_GRADE, grade);
        args.putLong(KEY_ASS_ID, iD);
        AddAssignmentDialogFragment dF = new AddAssignmentDialogFragment();
        dF.setArguments(args);
        return (dF);
    }

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
        Bundle bundle = getArguments();
        if (bundle != null) {
            mAssignmentName = bundle.getString(KEY_ASS_NAME);
            mGrade = bundle.getDouble(KEY_ASS_GRADE) + "";
            mID = bundle.getLong(KEY_ASS_ID);
        } else {
            mID = -1;
        }
        String category = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(R.layout.dialog_add_assignment);
        View layoutView = inflater.inflate(R.layout.dialog_add_assignment, null);
        builder.setView(layoutView);

        assignmentNameEditText = (EditText) layoutView.findViewById(R.id.assignment_name);
        assignmentNameEditText.setText(mAssignmentName);
        mCatSpin = (Spinner) layoutView.findViewById(R.id.category_picker);
        CategoryDataAdapter mCategoryDataAdapter = CourseFragment.mCategoryDataAdapter;
        categories = mCategoryDataAdapter.getCategoriesCursor(getActivity().getIntent().getLongExtra(CourseListFragment.courseId, -1));
        mCatSpin.setAdapter(new SimpleCursorAdapter(getActivity(), android.R.layout.simple_spinner_item, categories, new String[]{CategoryDataAdapter.KEY_NAME}, new int[]{android.R.id.text1}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER));
        mAssignmentGradeField = (EditText) layoutView.findViewById(R.id.assignment_grade);
        mAssignmentGradeField.setText(mGrade);
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
                        double grade = Double.parseDouble(mGrade);
                        Cursor curse = (Cursor) mCatSpin.getSelectedItem();
                        String categoryName = curse.getString(curse.getColumnIndexOrThrow(CategoryDataAdapter.KEY_NAME));
                        Log.d("HHH", categoryName);
                        mListener.onDialogPositiveClick(AddAssignmentDialogFragment.this, mAssignmentName, categoryName, grade, mCatSpin.getSelectedItemPosition(), getArguments() != null, mID);
                        dismiss();
                    }
                }
            });
        }
    }

    public interface AddAssignmentDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String assignmentName, String catName, Double assignmentGrade, int categoryPos, boolean isNew, long iD);

        public void onDialogNegativeClick(DialogFragment dialog);
    }

}