package edu.rose_hulman.tafkarr;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.app.ListFragment;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


import java.util.ArrayList;

import edu.rose_hulman.tafkarr.dummy.DummyContent;


public class CourseListFragment extends Fragment {

    public static final String courseId="45";
    public static final String courseName="46";
    public static final String courseBundleId="FF";
    private static ListView mListView;
    private static ClassDataAdapter mClassDataAdapter;
    private static SimpleCursorAdapter mCursorAdapter;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CourseListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClassDataAdapter = new ClassDataAdapter(this.getActivity());
        mClassDataAdapter.open();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_course_tab, container, false);
        mListView = (ListView)rootView.findViewById(android.R.id.list);

        Cursor cursor = mClassDataAdapter.getScoresCursor();
        String[] fromColumns = new String[] { ClassDataAdapter.KEY_NAME,
                ClassDataAdapter.KEY_SCORE };
        int[] toTextViews = new int[] { R.id.courseRowTitle, R.id.courseRowGrade};
        mCursorAdapter = new SimpleCursorAdapter(this.getActivity(),
                R.layout.course_row, cursor, fromColumns, toTextViews, 0);
        mListView.setAdapter(mCursorAdapter);
        registerForContextMenu(mListView);

//        mListView.setAdapter(new CourseAdapter(mCourses, getActivity()));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), CourseActivity.class);
                SQLiteCursor cursor = (SQLiteCursor) mCursorAdapter.getItem(position);
                i.putExtra(courseId, cursor.getInt(cursor.getColumnIndex(ClassDataAdapter.KEY_ID)));
//                i.putExtra(courseName, cursor.getString(cursor.getColumnIndex(ClassDataAdapter.KEY_NAME)));
                startActivityForResult(i, position);
            }
        });
        return rootView;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//            mCourses.get(requestCode).setCourseGrade(data.getIntExtra("TAG",-1));
//    }

     static void addClass(Course c) {
        mClassDataAdapter.addScore(c);
        Cursor cursor = mClassDataAdapter.getScoresCursor();
        mCursorAdapter.changeCursor(cursor);
        mClassDataAdapter.logAll();
    }

    /**
     * Read: Get a score for the data storage mechanism
     *
     * @param id
     *            Index of the score in the data storage mechanism
     */
    private Course getScore(long id) {
        // return mScores.get((int) id);
        return mClassDataAdapter.getScore(id);
    }

    /**
     * Update: Edit a score in the data storage mechanism Uses the values in the
     * pass Score to updates the score at the mSelectedId location
     *
     * @param c
     *            Container for the new values to use in the update
     */
//    private void editScore(Course c) {
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
    private void removeScore(long id) {
        // mScores.remove((int) id);
        // Collections.sort(mScores);
        // mScoreAdapter.notifyDataSetChanged();
        mClassDataAdapter.removeScore(id);
        Cursor cursor = mClassDataAdapter.getScoresCursor();
        mCursorAdapter.changeCursor(cursor);
    }




}
