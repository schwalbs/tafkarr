package edu.rose_hulman.tafkarr;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorTreeAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by andrewca on 2/18/2015.
 */
public class CourseFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = "TAG";
    public static ExpandableListAdapter listAdapter;
    public static ExpandableListView mListView;
    public static List<String>listCategories;
    public static HashMap<String, List<Assignment>> mapAssignments;
    private static AssignmentDataAdapter mAssignmentDataAdapter;
    private static CategoryDataAdapter mCategoryDataAdapter;
    private static AssignmentSimpleCursorTreeAdapter mCursorAdapter;
    private static long courseId;
    private static final String[] PHONE_PROJECTION = new String[] {
            ContactsContract.CommonDataKinds.Phone._ID,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.TYPE };

    private static final String[] CONTACT_PROJECTION = new String[] {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME };


    public CourseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        courseId = getActivity().getIntent().getLongExtra(CourseListFragment.courseId, -1);
//        getActivity().getIntent().getStringExtra(CourseListFragment.courseName);
//        prepareListData();
        mAssignmentDataAdapter = new AssignmentDataAdapter(this.getActivity());
        mAssignmentDataAdapter.open();
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_course, container, false);
        mListView = (ExpandableListView) rootView.findViewById(R.id.expandableListView);
        Cursor cursor = mAssignmentDataAdapter.getAssignmentsCursor();
        String[] fromColumns = new String[] { ClassDataAdapter.KEY_NAME,
                ClassDataAdapter.KEY_SCORE };
        int[] toTextViews = new int[] { R.id.courseRowTitle, R.id.courseRowGrade};
//        mCursorAdapter = new SimpleCursorAdapter(this.getActivity(),
//                R.layout.course_row, cursor, fromColumns, toTextViews, 0);
        mListView.setAdapter(mCursorAdapter);
        registerForContextMenu(mListView);
//        listView.setAdapter(new CourseAdapter(getActivity(), listCategories, mapAssignments));

        mCursorAdapter = new AssignmentSimpleCursorTreeAdapter(this.getActivity(),
                R.layout.course_list_group,
                R.layout.course_list_item,
                new String[] { ContactsContract.Contacts.DISPLAY_NAME },
                new int[] { R.id.group_title },
                new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER },
                new int[] { R.id.assignment_title });

        mListView.setAdapter(mCursorAdapter);

        Loader<Cursor> loader = getLoaderManager().getLoader(-1);
        if (loader != null && !loader.isReset()) {
            getLoaderManager().restartLoader(-1, null, this);
        } else {
            getLoaderManager().initLoader(-1, null, this);
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_edit_course, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_assignment) {
//            DialogFragment dialog = new AddAssignmentDialogFragment();
//            dialog.show(getFragmentManager(), "addAssignmentDialogFragment");
            AddAssignmentDialogFragment newDialog = new AddAssignmentDialogFragment();
            newDialog.show(getFragmentManager(), "dialogAss");

        }
        else if(id == R.id.add_category){
//            DialogFragment dialog = AddCategoryDialogFragment.newInstatnce();
//            dialog.show(getFragmentManager(), "addCategoryDialogFragment");
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
            AddCategoryDialogFragment newDialog = new AddCategoryDialogFragment();
            newDialog.show(getFragmentManager(), "dialog");
        }

        return super.onOptionsItemSelected(item);
    }

    public static void addCategory(Context context, String name, int weight){
        listCategories.add(name);
        List<Assignment> newList = new ArrayList<Assignment>();
        mapAssignments.put(name,newList);
//        mListView.setAdapter(new CourseAdapter(context, listCategories, mapAssignments));
    }

    static void addCategory(Category c) {
        mCategoryDataAdapter.addCategory(c);
        Cursor cursor = mAssignmentDataAdapter.getAssignmentsCursor();
        mCursorAdapter.changeCursor(cursor);
        mAssignmentDataAdapter.logAll();
    }

    /**
     * Read: Get a score for the data storage mechanism
     *
     * @param id
     *            Index of the score in the data storage mechanism
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
     * @param id
     *            Index of the score in the data storage mechanism
     */
    private void removeCategory(long id) {
        // mScores.remove((int) id);
        // Collections.sort(mScores);
        // mScoreAdapter.notifyDataSetChanged();
        mCategoryDataAdapter.removeCategory(id);
        Cursor cursor = mCategoryDataAdapter.getCategorysCursor();
        mCursorAdapter.changeCursor(cursor);
    }
    static void addAssignment(Assignment a) {
        mAssignmentDataAdapter.addAssignment(a);
        Cursor cursor = mAssignmentDataAdapter.getAssignmentsCursor();
        mCursorAdapter.changeCursor(cursor);
        mAssignmentDataAdapter.logAll();
    }

    /**
     * Read: Get a score for the data storage mechanism
     *
     * @param id
     *            Index of the score in the data storage mechanism
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
     * @param id
     *            Index of the score in the data storage mechanism
     */
    private void removeAssignment(long id) {
        mAssignmentDataAdapter.removeAssignment(id);
        Cursor cursor = mAssignmentDataAdapter.getAssignmentsCursor();
        mCursorAdapter.changeCursor(cursor);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader for loader_id " + id);
        CursorLoader cl;
        if (id != -1) {
            // child cursor
            Uri contactsUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            String selection = "("
                    + ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                    + " = ? )";
            String sortOrder = ContactsContract.CommonDataKinds.Phone.TYPE
                    + " COLLATE LOCALIZED ASC";
            String[] selectionArgs = new String[] { String.valueOf(id) };

            cl =  new CursorLoader(this.getActivity(), contactsUri, PHONE_PROJECTION,
                    selection, selectionArgs, sortOrder);
        } else {
            // group cursor
            Uri contactsUri = ContactsContract.Contacts.CONTENT_URI;
            String selection = "((" + ContactsContract.Contacts.DISPLAY_NAME
                    + " NOTNULL) AND ("
                    + ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1) AND ("
                    + ContactsContract.Contacts.DISPLAY_NAME + " != '' ))";
            String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
                    + " COLLATE LOCALIZED ASC";
            cl = new CursorLoader(this.getActivity(), contactsUri, CONTACT_PROJECTION,
                    selection, null, sortOrder);
        }

        return cl;
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Setting the new cursor onLoadFinished. (Old cursor would be closed
        // automatically)
        int id = loader.getId();
        Log.d(LOG_TAG, "onLoadFinished() for loader_id " + id);
        if (id != -1) {
            // child cursor
            if (!data.isClosed()) {
                Log.d(LOG_TAG, "data.getCount() " + data.getCount());

                HashMap<Integer, Integer> groupMap = mCursorAdapter.getGroupMap();
                try {
                    int groupPos = groupMap.get(id);
                    Log.d(LOG_TAG, "onLoadFinished() for groupPos " + groupPos);
                    mCursorAdapter.setChildrenCursor(groupPos, data);
                } catch (NullPointerException e) {
                    Log.w(LOG_TAG,
                            "Adapter expired, try again on the next query: "
                                    + e.getMessage());
                }
            }
        } else {
            mCursorAdapter.setGroupCursor(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Called just before the cursor is about to be closed.
        int id = loader.getId();
        Log.d(LOG_TAG, "onLoaderReset() for loader_id " + id);
        if (id != -1) {
            // child cursor
            try {
                mCursorAdapter.setChildrenCursor(id, null);
            } catch (NullPointerException e) {
                Log.w(LOG_TAG, "Adapter expired, try again on the next query: "
                        + e.getMessage());
            }
        } else {
            mCursorAdapter.setGroupCursor(null);
        }
    }


//    public static class CourseAdapter extends BaseExpandableListAdapter {
//
//        private Context mContext;
//        private List<String> mCatagories;
//        private HashMap<String, List<Assignment>> mAssignments;
//        public CourseAdapter(Context context, List<String> assignmentCatagories, HashMap<String, List<Assignment>> gradedAssignments){
//            this.mContext=context;
//            this.mCatagories = assignmentCatagories;
//            this.mAssignments = gradedAssignments;
//        }
//
//        @Override
//        public int getGroupCount() {
//            return this.mCatagories.size();
//        }
//
//        @Override
//        public int getChildrenCount(int groupPosition) {
//            return this.mAssignments.get(this.mCatagories.get(groupPosition)).size();
//        }
//
//        @Override
//        public Object getGroup(int groupPosition) {
//            return this.mCatagories.get(groupPosition);
//        }
//
//        @Override
//        public Assignment getChild(int groupPosition, int childPosition) {
//            return this.mAssignments.get(this.mCatagories.get(groupPosition)).get(childPosition);
//        }
//
//        @Override
//        public long getGroupId(int groupPosition) {
//            return groupPosition;
//        }
//
//        @Override
//        public long getChildId(int groupPosition, int childPosition) {
//            return childPosition;
//        }
//
//        @Override
//        public boolean hasStableIds() {
//            return false;
//        }
//
//        @Override
//        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
//            String catagoryTitle = (String) getGroup(groupPosition);
//            if(convertView == null){
//                LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                convertView = inflater.inflate(R.layout.course_list_group, null);
//            }
//            TextView categoryGradeView = (TextView) convertView.findViewById(R.id.group_value);
//            TextView categoryTitleView = (TextView) convertView.findViewById(R.id.group_title);
//            double sum = 0;
//            double avg = 0;
//            for(int i=0;i<getChildrenCount(groupPosition);i++){
//                sum = sum + getChild(groupPosition, i).getGrade();
//            }
//            avg=sum/getChildrenCount(groupPosition);
//            categoryGradeView.setText(String.format("%1$,.2f",avg));
//            categoryTitleView.setText(catagoryTitle);
//            return convertView;
//        }
//
//        @Override
//        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//            final Assignment childText = (Assignment) getChild(groupPosition,childPosition);
//            if(convertView == null){
//                LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                convertView = inflater.inflate(R.layout.course_list_item,null);
//            }
//            TextView listedAssignmentGrade = (TextView) convertView.findViewById(R.id.grade_value);
//            TextView listedAssignmentTitle = (TextView) convertView.findViewById(R.id.assignment_title);
//            listedAssignmentGrade.setText(childText.getGrade() + " ");
//            listedAssignmentTitle.setText(childText.getTitle());
//            return convertView;
//        }
//
//        @Override
//        public boolean isChildSelectable(int groupPosition, int childPosition) {
//            return true;
//        }
//    }

    public class AssignmentSimpleCursorTreeAdapter extends SimpleCursorTreeAdapter {

    private final String LOG_TAG = getClass().getSimpleName().toString();
    private CourseActivity mActivity;
    protected final HashMap<Integer, Integer> mGroupMap;

    // Please Note: Here cursor is not provided to avoid querying on main
    // thread.
    public AssignmentSimpleCursorTreeAdapter(Context context, int groupLayout,
                                          int childLayout, String[] groupFrom, int[] groupTo,
                                          String[] childrenFrom, int[] childrenTo) {

        super(context, null, groupLayout, groupFrom, groupTo, childLayout,
                childrenFrom, childrenTo);
        mActivity = (CourseActivity) context;
        mGroupMap = new HashMap<Integer, Integer>();
    }

    @Override
    protected Cursor getChildrenCursor(Cursor groupCursor) {
        // Logic to get the child cursor on the basis of selected group.
        int groupPos = groupCursor.getPosition();
        int groupId = groupCursor.getInt(groupCursor
                .getColumnIndex(ContactsContract.Contacts._ID));

        Log.d(LOG_TAG, "getChildrenCursor() for groupPos " + groupPos);
        Log.d(LOG_TAG, "getChildrenCursor() for groupId " + groupId);

        mGroupMap.put(groupId, groupPos);
        Loader<Cursor> loader = mActivity.getLoaderManager().getLoader(groupId);
        if (loader != null && !loader.isReset()) {
            mActivity.getLoaderManager()
                    .restartLoader(groupId, null, mActivity);
        } else {
            mActivity.getLoaderManager().initLoader(groupId, null, mActivity);
        }

        return null;
    }

    public HashMap<Integer, Integer> getGroupMap() {
        return mGroupMap;
    }

}

}

