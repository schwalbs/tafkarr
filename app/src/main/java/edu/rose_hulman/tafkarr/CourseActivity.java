package edu.rose_hulman.tafkarr;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import static android.app.PendingIntent.getActivity;


public class CourseActivity extends Activity implements AddCategoryDialogFragment.addCategoryDialogListener,AddAssignmentDialogFragment.addAssignmentDialogListener, LoaderManager.LoaderCallbacks<Object> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        if (savedInstanceState == null) {

            getFragmentManager().beginTransaction()
                    .add(R.id.container, new CourseFragment())
                    .commit();
        }
    }

    @Override
    public void onDialogConfirmClick(DialogFragment dialog, String categoryName, int categoryWeight) {
        CourseFragment.addCategory(this, categoryName,categoryWeight);
    }

    @Override
    public void onDialogDenyClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String assignmentName, String catName, int assignmentGrade) {
        Assignment a = new Assignment(assignmentName, assignmentGrade);
        CourseFragment.addAssignment(a);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {

    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }
}
