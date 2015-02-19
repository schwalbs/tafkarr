package edu.rose_hulman.tafkarr;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by andrewca on 2/18/2015.
 */
public class CourseFragment extends Fragment {
    ExpandableListAdapter listAdapter;
    ExpandableListView listView;
    List<String> listCategories;
    HashMap<String, List<String>> mapAssignments;

    public CourseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareListData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_course, container, false);
        listView = (ExpandableListView) rootView.findViewById(R.id.expandableListView);
        listView.setAdapter(new CourseAdapter(getActivity(), listCategories, mapAssignments));

        return rootView;
    }

    private void prepareListData() {
        listCategories = new ArrayList<String>();
        mapAssignments = new HashMap<String, List<String>>();

        listCategories.add("Homework");
        listCategories.add("Quizes");
        listCategories.add("Exams");

        List<String> homework = new ArrayList<String>();
        homework.add("homework 1");
        homework.add("homework b");

        List<String> quizes = new ArrayList<String>();
        quizes.add("quiz 1");
        quizes.add("The quiz I took drunk");
        quizes.add("pop quiz");

        List<String> exams = new ArrayList<String>();
        exams.add("Exam 1");
        exams.add("Exam 2");

        mapAssignments.put(listCategories.get(0), homework);
        mapAssignments.put(listCategories.get(1), quizes);
        mapAssignments.put(listCategories.get(2), exams);
    }


    public class CourseAdapter extends BaseExpandableListAdapter {

        private Context mContext;
        private List<String> mCatagories;
        private HashMap<String, List<String>> mAssignments;
        public CourseAdapter(Context context, List<String> assignmentCatagories, HashMap<String, List<String>> gradedAssignments){
            this.mContext=context;
            this.mCatagories = assignmentCatagories;
            this.mAssignments = gradedAssignments;
        }

        @Override
        public int getGroupCount() {
            return this.mCatagories.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this.mAssignments.get(this.mCatagories.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this.mCatagories.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return this.mAssignments.get(this.mCatagories.get(groupPosition)).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            String catagoryTitle = (String) getGroup(groupPosition);
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.course_list_group, null);
            }
            TextView catagoryTitleView = (TextView) convertView.findViewById(R.id.group_title);
            catagoryTitleView.setText(catagoryTitle);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            final String childText = (String) getChild(groupPosition,childPosition);
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.course_list_item,null);
            }
            TextView listedAssignmentTitle = (TextView) convertView.findViewById(R.id.assignment_title);
            listedAssignmentTitle.setText(childText);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

}

