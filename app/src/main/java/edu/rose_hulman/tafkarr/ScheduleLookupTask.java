package edu.rose_hulman.tafkarr;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Iterator;

public class ScheduleLookupTask extends
        AsyncTask<String, String, ArrayList<String>> {

    private Context mContext;
    private ProgressBar mProgress;
    private RecyclerView mResultsList;
    private ScheduleLookupResultsListAdapter mResultsListAdapter;

    public ScheduleLookupTask(Context context, RecyclerView resultsList,
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
        String authorization = params[0];
        String term = params[1];
        String userSearch = params[2];
        String response = Util.sendHttpRequest(mContext, (Util.getScheduleLookupSearchRequest(mContext, term, userSearch, authorization)));
        if (response == null) {
            return null;
        }

        // check if multiple/partial users received
        Document doc = Jsoup.parse(response);
        ArrayList<String> data = new ArrayList<>();
        if (searchShowsOneUser(doc)) {
            // only 1 user
            data.add(parseUserSearchHTML(doc));
        } else {
            // go through and query all other users
            data.addAll(searchMultiple(doc, term, authorization));
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

    private String singleSearch(String term, String userSearch, String authorization) {
        String response = Util.sendHttpRequest(mContext, (Util.getScheduleLookupSearchRequest(mContext, term, userSearch, authorization)));
        if (response == null) {
            return "";
        }

        Document doc = Jsoup.parse(response);
        return parseUserSearchHTML(doc);
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
