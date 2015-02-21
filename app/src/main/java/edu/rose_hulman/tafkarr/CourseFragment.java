package edu.rose_hulman.tafkarr;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by andrewca on 2/18/2015.
 */
public class CourseFragment extends Fragment {
    public static ExpandableListAdapter listAdapter;
    public static ExpandableListView listView;
    public static List<String>listCategories;
    public static HashMap<String, List<Assignment>> mapAssignments;

    public CourseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().getIntent().getStringExtra(CourseListFragment.courseId);
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
        mapAssignments = new HashMap<String, List<Assignment>>();

        listCategories.add("Homework");
        listCategories.add("Quizes");
        listCategories.add("Exams");

        List<Assignment> homework = new ArrayList<Assignment>();
        homework.add(new Assignment("homework 1",69));
        homework.add(new Assignment("homework b",100));

        List<Assignment> quizes = new ArrayList<Assignment>();
        quizes.add(new Assignment("quiz 1", 79));
        quizes.add(new Assignment("The quiz I took drunk",100));
        quizes.add(new Assignment("pop quiz", 0));

        List<Assignment> exams = new ArrayList<Assignment>();
        exams.add(new Assignment("Exam 1",100));
        exams.add(new Assignment("Exam 2",98));

        mapAssignments.put(listCategories.get(0), homework);
        mapAssignments.put(listCategories.get(1), quizes);
        mapAssignments.put(listCategories.get(2), exams);
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
        listView.setAdapter(new CourseAdapter(context, listCategories, mapAssignments));
    }

    public static void addAssignment(Context context, String assignmentName, String catName, int assignmentGrade) {
        if(mapAssignments.containsKey(catName)) {
            mapAssignments.get(catName).add(new Assignment(assignmentName,assignmentGrade));
        }else{
            List<Assignment> newList = new ArrayList<Assignment>();
            newList.add(new Assignment(assignmentName,assignmentGrade));
            mapAssignments.put(catName,newList);
        }
        Log.d("HHH", assignmentName +"");
        listView.setAdapter(new CourseAdapter(context, listCategories, mapAssignments));
    }


    public static class CourseAdapter extends BaseExpandableListAdapter {

        private Context mContext;
        private List<String> mCatagories;
        private HashMap<String, List<Assignment>> mAssignments;
        public CourseAdapter(Context context, List<String> assignmentCatagories, HashMap<String, List<Assignment>> gradedAssignments){
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
        public Assignment getChild(int groupPosition, int childPosition) {
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
            TextView categoryGradeView = (TextView) convertView.findViewById(R.id.group_value);
            TextView categoryTitleView = (TextView) convertView.findViewById(R.id.group_title);
            double sum = 0;
            double avg = 0;
            for(int i=0;i<getChildrenCount(groupPosition);i++){
                sum = sum + getChild(groupPosition, i).getGrade();
            }
            avg=sum/getChildrenCount(groupPosition);
            categoryGradeView.setText(String.format("%1$,.2f",avg));
            categoryTitleView.setText(catagoryTitle);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            final Assignment childText = (Assignment) getChild(groupPosition,childPosition);
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.course_list_item,null);
            }
            TextView listedAssignmentGrade = (TextView) convertView.findViewById(R.id.grade_value);
            TextView listedAssignmentTitle = (TextView) convertView.findViewById(R.id.assignment_title);
            listedAssignmentGrade.setText(childText.getGrade() + " ");
            listedAssignmentTitle.setText(childText.getTitle());
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

}

