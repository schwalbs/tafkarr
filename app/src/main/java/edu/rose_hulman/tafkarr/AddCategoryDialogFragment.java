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
import android.widget.SeekBar;
import android.widget.TextView;

public class AddCategoryDialogFragment extends DialogFragment {

    public interface AddCategoryDialogListener {
        public void onDialogConfirmClick(DialogFragment dialog, String categoryName, double categoryWeight);

        public void onDialogDenyClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    private int mCategoryWeight;
    private String mCategoryName;
    private EditText mCategoryNameField;
    private AddCategoryDialogListener mListener;

    public AddCategoryDialogFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (AddCategoryDialogListener) activity;
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
        View layoutView = inflater.inflate(R.layout.dialog_add_category, null);
        builder.setView(layoutView);

        mCategoryNameField = (EditText) layoutView.findViewById(R.id.category_name);

        final TextView weightDisplay = (TextView) layoutView.findViewById(R.id.weight_display);
        weightDisplay.setText(mCategoryWeight + "%");

        SeekBar mSeekBar = (SeekBar) layoutView.findViewById(R.id.weight_seek_bar);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCategoryWeight = progress;
                weightDisplay.setText(progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        builder.setTitle(getActivity().getString(R.string.add_category_title));
        builder
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do nothing so it can be overriden later
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        mListener.onDialogDenyClick(AddCategoryDialogFragment.this);
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
                    mCategoryName = mCategoryNameField.getText().toString().trim();
                    if (mCategoryName.isEmpty()) {
                        mCategoryNameField.setError(getActivity().getString(R.string.required));
                    } else {
                        // Send the positive button event back to the host activity
                        mListener.onDialogConfirmClick(AddCategoryDialogFragment.this, mCategoryName, mCategoryWeight);
                    }
                }
            });
        }
    }

}