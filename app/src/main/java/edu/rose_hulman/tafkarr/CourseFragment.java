package edu.rose_hulman.tafkarr;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleCursorTreeAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by andrewca on 2/18/2015.
 */
public class CourseFragment extends Fragment {
    private static final String LOG_TAG = "TAG";
    public static ExpandableListAdapter listAdapter;
    public static ExpandableListView mListView;
    public static List<String> listCategories;
    public static HashMap<String, List<Assignment>> mapAssignments;
    private static AssignmentDataAdapter mAssignmentDataAdapter;
    public static CategoryDataAdapter mCategoryDataAdapter;
    private static SimpleCursorTreeAdapter mCursorAdapter;
    private static long courseId;
    private static String mCourseName;

    public CourseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        courseId = getActivity().getIntent().getLongExtra(CourseListFragment.courseId, -1);
        mCourseName = getActivity().getIntent().getStringExtra(CourseListFragment.courseName);
        mAssignmentDataAdapter = new AssignmentDataAdapter(this.getActivity());
        mAssignmentDataAdapter.open();
        mCategoryDataAdapter = new CategoryDataAdapter(this.getActivity());
        mCategoryDataAdapter.open();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_course, container, false);
        mListView = (ExpandableListView) rootView.findViewById(R.id.expandableListView);

        Cursor categoryCursor = mCategoryDataAdapter.getCategorysCursor(this.courseId);
        String[] fromColumns = new String[]{CourseDataAdapter.KEY_NAME,
                CourseDataAdapter.KEY_SCORE};
        int[] toTextViews = new int[]{R.id.courseRowTitle, R.id.courseRowGrade};
        mListView.setAdapter(mCursorAdapter);
        registerForContextMenu(mListView);

        mCursorAdapter = new SimpleCursorTreeAdapter(this.getActivity(), categoryCursor,
                R.layout.course_list_group,
                R.layout.course_list_group,
                new String[]{CategoryDataAdapter.KEY_NAME},
                new int[]{R.id.group_title},
                R.layout.course_list_item,
                R.layout.course_list_item,
                new String[]{AssignmentDataAdapter.KEY_NAME, AssignmentDataAdapter.KEY_SCORE},
                new int[]{R.id.assignment_title, R.id.assignment_grade}) {
            @Override
            protected Cursor getChildrenCursor(Cursor groupCursor) {
                return mAssignmentDataAdapter.getAssignmentsCursor(groupCursor.getString(groupCursor.getColumnIndexOrThrow(CategoryDataAdapter.KEY_NAME)));
            }
        };

        mListView.setAdapter(mCursorAdapter);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_edit_course, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_assignment) {
            AddAssignmentDialogFragment newDialog = new AddAssignmentDialogFragment();
            newDialog.show(getFragmentManager(), "dialogAss");

        } else if (id == R.id.add_category) {
            AddCategoryDialogFragment newDialog = new AddCategoryDialogFragment();
            newDialog.show(getFragmentManager(), "dialog");
        }

        return super.onOptionsItemSelected(item);
    }

    static void addCategory(Category c) {
        mCategoryDataAdapter.addCategory(c);
        Cursor cursor = mCategoryDataAdapter.getCategorysCursor(courseId);
        mCursorAdapter.setGroupCursor(cursor);
        mCategoryDataAdapter.logAll();
    }

    /**
     * Read: Get a score for the data storage mechanism
     *
     * @param id Index of the score in the data storage mechanism
     */
    private Category getCategory(long id) {
        // return mScores.get((int) id);
        return mCategoryDataAdapter.getCategory(id);
    }

    /**
     * Update: Edit a score in the data storage mechanism Uses the values in the
     * pass Score to updates the score at the mSelectedId location
     *
     * @param c
     *            Container for the new values to use in the update
     */
//    private void editAssignment(Course c) {
//        if (mSelectedId == -1) {
//            Log.e("SLS", "Attempt to update with no score selected.");
//        }
//        c.setId((int) mSelectedId);
//        mScoreDataAdapter.updateScore(s);
//        Cursor cursor = mScoreDataAdapter.getScoresCursor();
//        mCursorAdapter.changeCursor(cursor);
//
//        // Score selectedScore = getScore(mSelectedId);
//        // selectedScore.setName(s.getName());
//        // selectedScore.setScore(s.getScore());
//        // Collections.sort(mScores);
//        // mScoreAdapter.notifyDataSetChanged();
//    }

    /**
     * Delete: Remove a score from the data storage mechanism
     *
     * @param id Index of the score in the data storage mechanism
     */
    private void removeCategory(long id) {
        mCategoryDataAdapter.removeCategory(id);
        Cursor cursor = mCategoryDataAdapter.getCategorysCursor(courseId);
        mCursorAdapter.changeCursor(cursor);
    }

    static void addAssignment(Assignment a) {
        mAssignmentDataAdapter.addAssignment(a);
        Cursor cursor = mAssignmentDataAdapter.getAssignmentsCursor(a.getCatId());
        for(int i =0; i<mCategoryDataAdapter.getCategorysCursor(courseId).getCount();i++) {
            mCursorAdapter.setChildrenCursor(i, cursor);
        }
        mAssignmentDataAdapter.logAll();
    }

    /**
     * Read: Get a score for the data storage mechanism
     *
     * @param id Index of the score in the data storage mechanism
     */
    private Assignment getAssignemt(long id) {
        // return mScores.get((int) id);
        return mAssignmentDataAdapter.getAssignment(id);
    }

    /**
     * Update: Edit a score in the data storage mechanism Uses the values in the
     * pass Score to updates the score at the mSelectedId location
     *
     * @param c
     *            Container for the new values to use in the update
     */
//    private void editAssignment(Course c) {
//        if (mSelectedId == -1) {
//            Log.e("SLS", "Attempt to update with no score selected.");
//        }
//        c.setId((int) mSelectedId);
//        mScoreDataAdapter.updateScore(s);
//        Cursor cursor = mScoreDataAdapter.getScoresCursor();
//        mCursorAdapter.changeCursor(cursor);
//
//        // Score selectedScore = getScore(mSelectedId);
//        // selectedScore.setName(s.getName());
//        // selectedScore.setScore(s.getScore());
//        // Collections.sort(mScores);
//        // mScoreAdapter.notifyDataSetChanged();
//    }

    /**
     * Delete: Remove a score from the data storage mechanism
     *
     * @param id Index of the score in the data storage mechanism
     */
    private void removeAssignment(long id) {
        mAssignmentDataAdapter.removeAssignment(id);
        Cursor cursor = mAssignmentDataAdapter.getAssignmentsCursor();
        mCursorAdapter.changeCursor(cursor);
    }


}

