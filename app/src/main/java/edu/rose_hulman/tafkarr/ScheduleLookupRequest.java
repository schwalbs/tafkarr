package edu.rose_hulman.tafkarr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ScheduleLookupRequest extends
		AsyncTask<String, String, ArrayList<String>> {

	private Context mContext;
	private ProgressBar mProgress;
	private RecyclerView mResultsList;
	private ScheduleLookupResultsListAdapter mResultsListAdapter;

	public ScheduleLookupRequest(Context context, RecyclerView resultsList,
			ScheduleLookupResultsListAdapter resultsListAdapter,
			ProgressBar progress) {
		mContext = context;
		mResultsList = resultsList;
		mResultsListAdapter = resultsListAdapter;
		mProgress = progress;
	}

	@Override
	protected ArrayList<String> doInBackground(String... params) {
		if (params.length < 3) {
			return null;
		}

		ArrayList<String> data = new ArrayList<>();
		String authorization = params[0];
		String term = params[1];
		String userSearch = params[2];
		try {
			HttpPost req = new HttpPost(
					"https://prodweb.rose-hulman.edu/regweb-cgi/reg-sched.pl");
			List<NameValuePair> reqParams = new ArrayList<>(2);
			reqParams.add(new BasicNameValuePair("termcode", term));
			reqParams.add(new BasicNameValuePair("view", "grid"));
			// username
			reqParams.add(new BasicNameValuePair("id1", userSearch));
			reqParams.add(new BasicNameValuePair("bt1", "ID%2FUsername"));
			// room number
			reqParams.add(new BasicNameValuePair("id4", ""));
			// params.add(new BasicNameValuePair("bt4", "Room"));
			// course id
			reqParams.add(new BasicNameValuePair("id5", ""));
			// params.add(new BasicNameValuePair("bt5", "Course"));
			req.setEntity(new UrlEncodedFormEntity(reqParams));
			// authentication
			req.addHeader("Authorization", "Basic " + authorization);

			DefaultHttpClient httpclient = new DefaultHttpClient();

			ResponseHandler<String> handler = new BasicResponseHandler();
			String response = httpclient.execute(req, handler);

			// check if multiple/partial users received
			Document doc = Jsoup.parse(response);
			if (searchShowsOneUser(doc)) {
				// only 1 user
				data.add(parseUserSearchHTML(doc));
			} else {
				// go through and query all other users
				data.addAll(searchMultiple(doc, term,
						"Z2FydHprZHM6U3RFd0FyZEVzcyEx"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return data;
	}

	@Override
	protected void onPostExecute(ArrayList<String> result) {
		super.onPostExecute(result);
		if (result == null) {
			Toast.makeText(mContext, "Errors while fetching schedules",
					Toast.LENGTH_SHORT).show();
		} else {
			mResultsListAdapter.setDataset(result);
		}
		mProgress.setVisibility(View.GONE);
		mResultsList.setVisibility(View.VISIBLE);

	}

	private ArrayList<String> searchMultiple(Document doc, String term,
			String authorization) {
		ArrayList<String> compiled = new ArrayList<>();

		// get table showing users
		Element usernamesTable = doc.select("table").get(1);
		Elements usernamesEl = usernamesTable.select("td:nth-child(1) a");

		for (Element usernameEl : usernamesEl) {
			compiled.add(singleSearch(term, usernameEl.text(), authorization));
		}
		return compiled;
	}

	private String singleSearch(String term, String userSearch,
			String authorization) {
		String data = "";
		try {
			HttpPost req = new HttpPost(
					"https://prodweb.rose-hulman.edu/regweb-cgi/reg-sched.pl");
			List<NameValuePair> params = new ArrayList<>(2);
			params.add(new BasicNameValuePair("termcode", term));
			params.add(new BasicNameValuePair("view", "grid"));
			// username
			params.add(new BasicNameValuePair("id1", userSearch));
			params.add(new BasicNameValuePair("bt1", "ID%2FUsername"));
			// room number
			params.add(new BasicNameValuePair("id4", ""));
			// params.add(new BasicNameValuePair("bt4", "Room"));
			// course id
			params.add(new BasicNameValuePair("id5", ""));
			// params.add(new BasicNameValuePair("bt5", "Course"));
			req.setEntity(new UrlEncodedFormEntity(params));
			// authentication
			req.addHeader("Authorization", "Basic " + authorization);

			DefaultHttpClient httpclient = new DefaultHttpClient();

			ResponseHandler<String> handler = new BasicResponseHandler();
			String response = httpclient.execute(req, handler);
			Document doc = Jsoup.parse(response);

			// Get name
			// Name: [name] Major: [major] Year: [year] Advisor: [advisor]

			data += parseUserSearchHTML(doc);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	private boolean searchShowsOneUser(Document doc) {
		return doc.select("table td[class=bw80").first() != null;
	}

	private String parseUserSearchHTML(Document doc) {
		String userInfo = doc.select("table td[class=bw80]").first().text();
		int namePos = userInfo.indexOf("Name: ");
		int majorPos = userInfo.indexOf("Major: ");
		int yearPos = userInfo.indexOf("Year: ");
		int advPos = userInfo.indexOf("Advisor: ");
		// for faculty
		int usernamePos = userInfo.indexOf("Username: ");

		String name = "";
		// students have name, major, year, advisor
		// faculty have name, username, dept, room, phone, campus mail
		if (namePos != -1 && majorPos != -1) {
			name = "Name: "
					+ userInfo.substring(namePos + "Name: ".length(), majorPos)
					+ "\n";
		} else if (namePos != -1 && usernamePos != -1) {
			name = "Name: "
					+ userInfo.substring(namePos + "Name: ".length(),
							usernamePos) + "\n";
		}
		String major = "";
		if (majorPos != -1 && yearPos != -1) {
			major = "Major: "
					+ userInfo
							.substring(majorPos + "Major: ".length(), yearPos)
					+ "\n";
		}

		String year = "";
		if (yearPos != -1 && advPos != -1) {
			year = "Year: "
					+ userInfo.substring(yearPos + "Year: ".length(), advPos)
					+ "\n";
		}
		String ret = "";
		ret += name;
		ret += major;
		ret += year;

		// remove unwanted HTML content
		Element schedule = doc.select("table").get(1);
		Iterator<Element> rows = schedule.select("tr").iterator();
		// ignore headers
		rows.next();
		while (rows.hasNext()) {
			Element x = rows.next();
			Elements rowData = x.select("td");
			String d = "";
			// course ID
			d += rowData.get(0).text();
			d += "\t\t";
			// prof
			d += rowData.get(3).text();
			d += "\t\t";
			// schedule
			d += rowData.get(7).text();
			ret += d + "\n";
		}
		return ret;
	}
}
