package edu.rose_hulman.tafkarr;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class CourseListFragment extends Fragment {


    public static final String courseId = "45";
    public static final String courseName = "46";
    public static final String courseBundleId = "FF";
    private static ListView mListView;
    private static CourseDataAdapter mCourseDataAdapter;
    private static SimpleCursorAdapter mCursorAdapter;

    public SimpleCursorAdapter getCursorAdapter(){
        return mCursorAdapter;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCourseDataAdapter = new CourseDataAdapter(this.getActivity());
        mCourseDataAdapter.open();
    }

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
    static void launchEditCourse(Activity activity,long id){
        Course newCourse = mCourseDataAdapter.getCourse(id);
        AddCourseDialogFragment newDialog = AddCourseDialogFragment.newInstance(newCourse.getTitle(), newCourse.getId());
        newDialog.show(activity.getFragmentManager(), "dialogAss");
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_course_tab, container, false);

        Cursor cursor = mCourseDataAdapter.getCoursesCursor();
        String[] fromColumns = new String[]{CourseDataAdapter.KEY_NAME,
                CourseDataAdapter.KEY_SCORE};
        int[] toTextViews = new int[]{R.id.courseRowTitle, R.id.courseRowGrade};
        mCursorAdapter = new SimpleCursorAdapter(this.getActivity(),
                R.layout.course_row, cursor, fromColumns, toTextViews, 0) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View rowView = super.getView(position, convertView, parent);

                Cursor cursor = (Cursor) getItem(position);
                Course course = mCourseDataAdapter.getCourse(cursor.getLong(cursor.getColumnIndexOrThrow(CourseDataAdapter.KEY_ID)));
                CategoryDataAdapter cda = new CategoryDataAdapter(getActivity());
                cda.open();
                Cursor categoriesCursor = cda.getCategoriesCursor(course.getId());

                AssignmentDataAdapter ada = new AssignmentDataAdapter(getActivity());
                ada.open();


                double total = 0;
                categoriesCursor.moveToFirst();
                for (int i = 0; i < categoriesCursor.getCount(); i++) {
                    double weight = categoriesCursor.getDouble(categoriesCursor.getColumnIndexOrThrow(CategoryDataAdapter.KEY_WEIGHT)) / 100;
                    String catName = categoriesCursor.getString(categoriesCursor.getColumnIndexOrThrow(CategoryDataAdapter.KEY_NAME));
                    double avg = ada.getCategoryAverage(catName);
                    total += (avg * weight);
                    categoriesCursor.moveToNext();
                }

                String totalS = getActivity().getString(R.string.double_format, total);
                ((TextView) rowView.findViewById(R.id.courseRowGrade)).setText(totalS);
                ada.close();
                cda.close();
                return rowView;
            }
        };


        mListView = (ListView) rootView.findViewById(R.id.course_list);
        mListView.setAdapter(mCursorAdapter);
        registerForContextMenu(mListView);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        mListView.setMultiChoiceModeListener(new CourseMultiSelectListener(getActivity(), this));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), CourseActivity.class);
                SQLiteCursor cursor = (SQLiteCursor) mCursorAdapter.getItem(position);
                i.putExtra(courseId, cursor.getLong(cursor.getColumnIndex(CourseDataAdapter.KEY_ID)));
                i.putExtra(courseName, cursor.getString(cursor.getColumnIndex(ClassDataAdapter.KEY_NAME)));
                startActivityForResult(i, position);
            }
        });

        rootView.findViewById(R.id.add_course_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCourseDialogFragment newDialog = new AddCourseDialogFragment();
                newDialog.show(getFragmentManager(), "");
            }
        });
        return rootView;
    }

    public static void editCourse(Course newCourse) {
        mCourseDataAdapter.updateCourse(newCourse);
        updateCursor();
    }
}
