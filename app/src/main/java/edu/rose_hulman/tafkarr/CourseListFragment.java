package edu.rose_hulman.tafkarr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.ListFragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;

import edu.rose_hulman.tafkarr.dummy.DummyContent;


public class CourseListFragment extends ListFragment {
    private ArrayList<Course> mCourses;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CourseListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCourses = new ArrayList<Course>();
        Course androidDev = new Course();
        androidDev.setTitle("Android Application Development");
        androidDev.setCourseGrade(96);
        mCourses.add(androidDev);
        Course compArc = new Course();
        compArc.setTitle("AdvTopics in Comp Architecture");
        compArc.setCourseGrade(86.0);
        mCourses.add(compArc);
        Course german = new Course();
        german.setTitle("German Language & Culture II");
        german.setCourseGrade(79.0);
        mCourses.add(german);
        Course srDesign = new Course();
        srDesign.setTitle("Engineering Design II");
        srDesign.setCourseGrade(100.0);
        mCourses.add(srDesign);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_course_tab, container, false);
        setListAdapter(new CourseAdapter(mCourses));
        return rootView;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent i = new Intent(getActivity(), CourseActivity.class);
        startActivity(i);


    }

    public class CourseAdapter extends ArrayAdapter<Course>{
        public CourseAdapter(ArrayList<Course> courses) {
            super(getActivity(), 0, courses);
        }
        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(
                        R.layout.course_row, null);
            }
            Course course = getItem(position);

            TextView titleTextView = (TextView) convertView
                    .findViewById(R.id.courseRowTitle);
            TextView gradeTextView = (TextView) convertView
                    .findViewById(R.id.courseRowGrade);
//            ImageButton addAssignment = (ImageButton) convertView.findViewById(R.id.courseRowEdit);
//            addAssignment.setFocusable(true);
            titleTextView.setText(course.getTitle());
            gradeTextView.setText(course.getCourseGrade() + "");
//            addAssignment.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(getActivity(), AddAssignmentActivity.class);
//                    startActivity(intent);
//                }
//            });
            return convertView;
        }
    }

}
