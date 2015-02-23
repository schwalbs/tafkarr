package edu.rose_hulman.tafkarr;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Iterator;

public class LoadCurrentTermCoursesTask extends
        AsyncTask<Void, Void, ArrayList<Course>> {

    private Context mContext;
    private String mUsername;
    private String mTerm = Util.getCurrentTerm();
    private String mAuthorization;
    private DialogFragment mProgress;

    public LoadCurrentTermCoursesTask(Context context, String username, String authorization) {
        mContext = context;
        mUsername = username;
        mAuthorization = authorization;
    }

    @Override
    protected ArrayList<Course> doInBackground(Void... params) {
        String response = Util.sendHttpRequest(mContext, Util.getScheduleLookupSearchRequest(mContext, mTerm, mUsername, mAuthorization));
        if (response == null) {
            return null;
        }

        return parseResponse(Jsoup.parse(response));

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        mProgress = new ProgressDialog();
        mProgress.show(((Activity) mContext).getFragmentManager(), "progress");
    }

    @Override
    protected void onPostExecute(ArrayList<Course> result) {
        super.onPostExecute(result);
        if (result == null) {
            Toast.makeText(mContext, mContext.getString(R.string.error_loading_courses), Toast.LENGTH_SHORT).show();
        } else {
            int numAdded = CourseListFragment.addCoursesCheckUniqueName(result);
            Toast.makeText(mContext, mContext.getString(R.string.courses_imported, numAdded), Toast.LENGTH_SHORT).show();
        }
        mProgress.dismiss();
    }

    private ArrayList<Course> parseResponse(Document doc) {
        ArrayList<Course> courses = new ArrayList<>();
        //get the schedule
        Element schedule = doc.select("table").get(1);
        Iterator<Element> rows = schedule.select("tr").iterator();
        // ignore row headers
        rows.next();
        while (rows.hasNext()) {
            Elements rowData = rows.next().select("td");
            Course course = new Course();
            String courseId = rowData.get(0).text();
            String courseName = rowData.get(2).text();
            course.setTitle(courseId + " - " + courseName);
            courses.add(course);
        }
        return courses;
    }
}
