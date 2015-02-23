package edu.rose_hulman.tafkarr;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.SimpleCursorTreeAdapter;

public class CourseFragment extends Fragment {
    public static ExpandableListView mListView;
    public static CategoryDataAdapter mCategoryDataAdapter;
    private static AssignmentDataAdapter mAssignmentDataAdapter;
    private static SimpleCursorTreeAdapter mCursorAdapter;
    private static long courseId;
    private static String mCourseName;

    public CourseFragment() {
    }

    static void addCategory(Category c) {
        mCategoryDataAdapter.addCategory(c);
        Cursor cursor = mCategoryDataAdapter.getCategoriesCursor(courseId);
        mCursorAdapter.setGroupCursor(cursor);
        mCategoryDataAdapter.logAll();
    }

    static void addAssignment(Assignment a, int catPos) {
        mAssignmentDataAdapter.addAssignment(a);
        Cursor cursor = mAssignmentDataAdapter.getAssignmentsCursor(a.getCatId());
        mCursorAdapter.setChildrenCursor(catPos, cursor);
        mAssignmentDataAdapter.logAll();
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

        Cursor categoryCursor = mCategoryDataAdapter.getCategoriesCursor(this.courseId);
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
                new int[]{R.id.assignment_title, R.id.grade_value}) {
            @Override
            protected Cursor getChildrenCursor(Cursor groupCursor) {
                return mAssignmentDataAdapter.getAssignmentsCursor(groupCursor.getString(groupCursor.getColumnIndexOrThrow(CategoryDataAdapter.KEY_NAME)));
            }

//            @Override
//            public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
//                convertView.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View v) {
//                        Cursor newCurse = (Cursor) v.get
////                                getItemAtPosition(childPosition+1);
//                        AddAssignmentDialogFragment newDialog = AddAssignmentDialogFragment.newInstance(newCurse.getString(newCurse.getColumnIndexOrThrow(AssignmentDataAdapter.KEY_NAME)),newCurse.getDouble(newCurse.getColumnIndexOrThrow(AssignmentDataAdapter.KEY_SCORE)), newCurse.getLong(newCurse.getColumnIndexOrThrow(AssignmentDataAdapter.KEY_ID)));
//                        newDialog.show(getFragmentManager(), "dialogAss");
//                        return true;
//                    }
//                });
//                return super.getGroupView(groupPosition, isExpanded, convertView, parent);
//            }
        };
        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Cursor newCurse = (Cursor) mListView.getItemAtPosition(childPosition+1);
                AddAssignmentDialogFragment newDialog = AddAssignmentDialogFragment.newInstance(newCurse.getString(newCurse.getColumnIndexOrThrow(AssignmentDataAdapter.KEY_NAME)),newCurse.getDouble(newCurse.getColumnIndexOrThrow(AssignmentDataAdapter.KEY_SCORE)), newCurse.getLong(newCurse.getColumnIndexOrThrow(AssignmentDataAdapter.KEY_ID)));
                newDialog.show(getFragmentManager(), "dialogAss");
                return true;
            }
        });
        mListView.setAdapter(mCursorAdapter);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_course, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_assignment) {
            AddAssignmentDialogFragment newDialog = new AddAssignmentDialogFragment();
            newDialog.show(getFragmentManager(), "dialogAss");
            return true;
        } else if (id == R.id.add_category) {
            AddCategoryDialogFragment newDialog = new AddCategoryDialogFragment();
            newDialog.show(getFragmentManager(), "dialog");
            return true;
        } else if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(getActivity());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Category getCategory(long id) {
        return mCategoryDataAdapter.getCategory(id);
    }

    private void removeCategory(long id) {
        mCategoryDataAdapter.removeCategory(id);
        Cursor cursor = mCategoryDataAdapter.getCategoriesCursor(courseId);
        mCursorAdapter.changeCursor(cursor);
    }

    private Assignment getAssignemt(long id) {
        return mAssignmentDataAdapter.getAssignment(id);
    }

    public static void editAssignment(Assignment a, int catPos){
        mAssignmentDataAdapter.updateAssignment(a);
        Cursor cursor = mAssignmentDataAdapter.getAssignmentsCursor(a.getCatId());
        mCursorAdapter.setChildrenCursor(catPos, cursor);
    }

    private void removeAssignment(long id) {
        mAssignmentDataAdapter.removeAssignment(id);
        Cursor cursor = mAssignmentDataAdapter.getAssignmentsCursor();
        mCursorAdapter.changeCursor(cursor);
    }


}

