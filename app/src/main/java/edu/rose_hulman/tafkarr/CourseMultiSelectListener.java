package edu.rose_hulman.tafkarr;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteCursor;
import android.view.ActionMode;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class CourseMultiSelectListener extends BaseMultiSelectListener {

    private CourseListFragment mClFrag;

    public CourseMultiSelectListener(Context context, CourseListFragment clFrag) {
        super(context);
        mClFrag = clFrag;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        if(item.getItemId()==R.id.multi_select_delete) {
            ArrayList<Long> idsToRemove = new ArrayList<>();
            SimpleCursorAdapter adapter = mClFrag.getCursorAdapter();
            int idCol = ((SQLiteCursor) adapter.getItem(0)).getColumnIndex(CourseDataAdapter.KEY_ID);
            for (int rowNum : mSelectedRows) {
                long courseId = ((SQLiteCursor) adapter.getItem(rowNum)).getLong(idCol);
                idsToRemove.add(courseId);
            }

            mClFrag.removeCoursesByIds(idsToRemove);
        }
        else if(item.getItemId() == R.id.multi_select_edit){
            ArrayList<Long> idsToEdit = new ArrayList<>();
            SimpleCursorAdapter adapter = mClFrag.getCursorAdapter();
            int idCol = ((SQLiteCursor) adapter.getItem(0)).getColumnIndex(CourseDataAdapter.KEY_ID);
            for (int rowNum : mSelectedRows) {
                long courseId = ((SQLiteCursor) adapter.getItem(rowNum)).getLong(idCol);
                idsToEdit.add(courseId);
            }
            if(idsToEdit.size()>1){
                Toast.makeText(mClFrag.getActivity(), "Cannot edit more than one Course", Toast.LENGTH_SHORT).show();
            }else {
                mClFrag.launchEditCourse((Activity) mContext, idsToEdit.get(0));
            }
        }

        return true;
    }
}
