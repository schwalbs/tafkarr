package edu.rose_hulman.tafkarr;

import android.content.Context;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;


/**
 * Created by gartzkds on 2/20/2015.
 */
public class Util {
    public static final String TERM_ONE_SUFFIX = "10";
    public static final String TERM_TWO_SUFFIX = "20";
    public static final String TERM_THREE_SUFFIX = "30";
    public static final String TERM_FOUR_SUFFIX = "40";

    public static String getCurrentTerm() {
        String term = "";
        GregorianCalendar now = new GregorianCalendar();
        int month = now.get(GregorianCalendar.MONTH);
        int year = now.get(GregorianCalendar.YEAR);
        if (month >= GregorianCalendar.SEPTEMBER && month <= GregorianCalendar.NOVEMBER) {
            //sept-november -> 201510
            term = (year + 1) + TERM_ONE_SUFFIX;
        } else if (month == GregorianCalendar.DECEMBER || month <= GregorianCalendar.FEBRUARY) {
            //december-february -> 201520
            if (month == GregorianCalendar.DECEMBER) {
                year = year + 1;
            }
            term = year + TERM_TWO_SUFFIX;
        } else if (month >= GregorianCalendar.MARCH && month <= GregorianCalendar.MAY) {
            //march-May -> 201530
            term = year + TERM_THREE_SUFFIX;
        } else {
            //june-august ->201540
            term = year + TERM_FOUR_SUFFIX;
        }
        return term;
    }

    public static String sendHttpRequest(Context context, HttpRequestBase request) {
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            ResponseHandler<String> handler = new BasicResponseHandler();
            return httpclient.execute(request, handler);
        } catch (Exception e) {
            Log.e(context.getString(R.string.log_error), e.getMessage());
            return null;
        }
    }

    public static HttpPost getScheduleLookupSearchRequest(Context context, String term, String username, String authorization) {
        try {
            HttpPost req = new HttpPost(context.getString(R.string.schedule_lookup_base));
            List<NameValuePair> reqParams = new ArrayList<>(2);
            // term
            reqParams.add(new BasicNameValuePair("termcode", term));
            reqParams.add(new BasicNameValuePair("view", "grid"));
            // username
            reqParams.add(new BasicNameValuePair("id1", username));
            reqParams.add(new BasicNameValuePair("bt1", "ID%2FUsername"));
            req.setEntity(new UrlEncodedFormEntity(reqParams));
            // authentication
            req.addHeader("Authorization", "Basic " + authorization);
            return req;
        } catch (Exception e) {
            Log.e(context.getString(R.string.log_error), e.getMessage());
            return null;
        }
    }
}
