package edu.rose_hulman.tafkarr;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity implements ActionBar.TabListener, AddCourseDialogFragment.AddCourseDialogListener {

    SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private SharedPreferences mSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedPrefs = getSharedPreferences(getString(R.string.shared_prefs_file), MODE_PRIVATE);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // make the tabs look like they're part of the actionbar
        if (getActionBar() != null) {
            findViewById(R.id.pager_title_strip).setElevation(getActionBar().getElevation());
            getActionBar().setElevation(0);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sign_out) {
            //remove the saved authorization
            SharedPreferences.Editor editor = mSharedPrefs.edit();
            editor.remove(getString(R.string.prefs_key_auth_shared));
            editor.remove(getString(R.string.prefs_key_username_shared));
            editor.apply();

            //go back to login
            Intent i = new Intent(this, LoginActivity.class);
            this.startActivity(i);
            finish();
            return true;
        } else if (id == R.id.load_courses) {
            String username = mSharedPrefs.getString(getString(R.string.prefs_key_username_shared), "");
            String authorization = mSharedPrefs.getString(getString(R.string.prefs_key_auth_shared), "");
            new LoadCurrentTermCoursesTask(this, username, authorization).execute();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // nothing special to do
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // nothing special to do
    }

    @Override
    public void onCourseConfirmClick(DialogFragment dialog, String courseName, boolean isEdited, long id) {

        Course newCourse = new Course();
        newCourse.setTitle(courseName);
        if(isEdited){
            newCourse.setId(id);
            CourseListFragment.editCourse(newCourse);
        }else {
            CourseListFragment.addCourse(newCourse);
        }

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private Fragment[] fragments = new Fragment[]{new CourseListFragment(), new ScheduleLookupFragment()};
        private String[] mFragmentTitles = new String[]{"Courses", "Schedule Lookup"};

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position >= 0 && position <= getCount()) {
                return fragments[position];
            }
            return null;
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position >= 0 && position <= getCount()) {
                return mFragmentTitles[position];
            }
            return null;
        }
    }
}
