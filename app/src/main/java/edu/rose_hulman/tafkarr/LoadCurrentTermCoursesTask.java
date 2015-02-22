package edu.rose_hulman.tafkarr;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by gartzkds on 2/21/2015.
 */
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
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            ResponseHandler<String> handler = new BasicResponseHandler();
            String response = httpclient.execute(getRequest(), handler);

            ArrayList<Course> courses = parseResponse(Jsoup.parse(response));
            return courses;
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
            return null;
        }
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
            Toast.makeText(mContext, "Errors while fetching schedule", Toast.LENGTH_SHORT).show();
        } else {
            int numAdded = CourseListFragment.addCoursesCheckUniqueName(result);
            mProgress.dismiss();
            Toast.makeText(mContext, mContext.getString(R.string.courses_imported, numAdded), Toast.LENGTH_SHORT).show();
        }

    }


    private HttpPost getRequest() {
        try {
            HttpPost req = new HttpPost(mContext.getString(R.string.schedule_lookup_base));
            List<NameValuePair> reqParams = new ArrayList<>(2);
            // term
            reqParams.add(new BasicNameValuePair("termcode", mTerm));
            reqParams.add(new BasicNameValuePair("view", "grid"));
            // username
            reqParams.add(new BasicNameValuePair("id1", mUsername));
            reqParams.add(new BasicNameValuePair("bt1", "ID%2FUsername"));
            req.setEntity(new UrlEncodedFormEntity(reqParams));
            // authentication
            req.addHeader("Authorization", "Basic " + mAuthorization);
            return req;
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
            return null;
        }
    }

    private ArrayList<Course> parseResponse(Document doc) {
        ArrayList<Course> courses = new ArrayList<Course>();
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
