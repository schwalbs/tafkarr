package edu.rose_hulman.tafkarr;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;


public class CourseListFragment extends Fragment {


    public static final String courseId = "45";
    public static final String courseName = "46";
    public static final String courseBundleId = "FF";
    private static ListView mListView;
    private static CourseDataAdapter mCourseDataAdapter;
    private static SimpleCursorAdapter mCursorAdapter;

    public static CourseDataAdapter getCourseDataAdapter() {
        return mCourseDataAdapter;
    }

    public static SimpleCursorAdapter getCursorAdapter() {
        return mCursorAdapter;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CourseListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCourseDataAdapter = new CourseDataAdapter(this.getActivity());
        mCourseDataAdapter.open();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_course_tab, container, false);
        mListView = (ListView) rootView.findViewById(R.id.course_list);

        Cursor cursor = mCourseDataAdapter.getCoursesCursor();
        String[] fromColumns = new String[]{CourseDataAdapter.KEY_NAME,
                CourseDataAdapter.KEY_SCORE};
        int[] toTextViews = new int[]{R.id.courseRowTitle, R.id.courseRowGrade};
        mCursorAdapter = new SimpleCursorAdapter(this.getActivity(),
                R.layout.course_row, cursor, fromColumns, toTextViews, 0);
        mListView.setAdapter(mCursorAdapter);
        registerForContextMenu(mListView);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        mListView.setMultiChoiceModeListener(new CourseMultiChoiceModeListener(getActivity(), this));
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.add_course_fab);
        fab.attachToListView(mListView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCourseDialogFragment newDialog = new AddCourseDialogFragment();
                newDialog.show(getFragmentManager(), "dialog");
            }
        });
//        mListView.setAdapter(new CourseAdapter(mCourses, getActivity()));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), CourseActivity.class);
                SQLiteCursor cursor = (SQLiteCursor) mCursorAdapter.getItem(position);
                i.putExtra(courseId, cursor.getInt(cursor.getColumnIndex(CourseDataAdapter.KEY_ID)));
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


    private static void updateCursor() {
        Cursor cursor = mCourseDataAdapter.getCoursesCursor();
        mCursorAdapter.changeCursor(cursor);
        mCourseDataAdapter.logAll();
    }

    static void addCourse(Course c) {
        mCourseDataAdapter.addCourse(c);
        updateCursor();
    }

    static int addCoursesCheckUniqueName(ArrayList<Course> courses) {
        int added = 0;
        for (Course c : courses) {
            if (!mCourseDataAdapter.existsCourseWithName(c.getTitle())) {
                mCourseDataAdapter.addCourse(c);
                added++;
            }
        }
        updateCursor();
        return added;
    }

    static void addCourses(ArrayList<Course> courses) {
        for (Course c : courses) {
            mCourseDataAdapter.addCourse(c);
        }
        updateCursor();
    }

    static void removeCourse(Course c) {
        mCourseDataAdapter.removeCourse(c);
        updateCursor();
    }

    static void removeCourses(ArrayList<Course> courses) {
        for (Course c : courses) {
            mCourseDataAdapter.removeCourse(c);
        }
        updateCursor();
    }

    static void removeCoursesByIds(ArrayList<Long> courseIds) {
        mCourseDataAdapter.removeCourses(courseIds);
        updateCursor();
    }

    /**
     * Read: Get a score for the data storage mechanism
     *
     * @param id Index of the score in the data storage mechanism
     */
    private Course getScore(long id) {
        // return mScores.get((int) id);
        return mCourseDataAdapter.getCourse(id);
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
//        mScoreDataAdapter.updateCourse(s);
//        Cursor cursor = mScoreDataAdapter.getCoursesCursor();
//        mCursorAdapter.changeCursor(cursor);
//
//        // Score selectedScore = getCourse(mSelectedId);
//        // selectedScore.setName(s.getName());
//        // selectedScore.setScore(s.getCourse());
//        // Collections.sort(mScores);
//        // mScoreAdapter.notifyDataSetChanged();
//    }

    /**
     * Delete: Remove a score from the data storage mechanism
     *
     * @param id Index of the score in the data storage mechanism
     */
    private void removeScore(long id) {
        // mScores.remove((int) id);
        // Collections.sort(mScores);
        // mScoreAdapter.notifyDataSetChanged();
        mCourseDataAdapter.removeCourse(id);
        Cursor cursor = mCourseDataAdapter.getCoursesCursor();
        mCursorAdapter.changeCursor(cursor);
    }


}
