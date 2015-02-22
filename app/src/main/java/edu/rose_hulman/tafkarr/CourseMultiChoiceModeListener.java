package edu.rose_hulman.tafkarr;

import android.content.Context;
import android.database.sqlite.SQLiteCursor;
import android.view.ActionMode;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

/**
 * Created by gartzkds on 2/21/2015.
 */
public class CourseMultiChoiceModeListener extends BaseMultiChoiceModeListener {

    private CourseListFragment mClFrag;

    public CourseMultiChoiceModeListener(Context context, CourseListFragment clFrag) {
        super(context);
        mClFrag = clFrag;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        ArrayList<Long> idsToRemove = new ArrayList<>();
        SimpleCursorAdapter adapter = mClFrag.getCursorAdapter();
        int idCol = ((SQLiteCursor) adapter.getItem(0)).getColumnIndex(CourseDataAdapter.KEY_ID);
        for (int rowNum : mSelectedRows) {
            long courseId = ((SQLiteCursor) adapter.getItem(rowNum)).getLong(idCol);
            idsToRemove.add(courseId);
        }

        mClFrag.removeCoursesByIds(idsToRemove);


        return true;
    }
}
