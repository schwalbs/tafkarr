package edu.rose_hulman.tafkarr;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;

public class CourseActivity extends Activity implements AddCategoryDialogFragment.AddCategoryDialogListener, AddAssignmentDialogFragment.AddAssignmentDialogListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {

            getFragmentManager().beginTransaction()
                    .add(R.id.container, new CourseFragment())
                    .commit();
        }
    }

    @Override
    public void onDialogConfirmClick(DialogFragment dialog, String categoryName, double categoryWeight) {
        Category newCategory = new Category(categoryName, categoryWeight, getIntent().getLongExtra(CourseListFragment.courseId, -1));
        CourseFragment.addCategory(newCategory);
    }


    @Override
    public void onDialogDenyClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String assignmentName, String catName, Double assignmentGrade, int catPos, boolean isNew, long mId) {
        Assignment a = new Assignment(assignmentName, assignmentGrade);
        a.setCatId(catName);
        if(isNew){
            a.setId(mId);
            CourseFragment.editAssignment(a, catPos);
        }else {
            CourseFragment.addAssignment(a, catPos);
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

}
