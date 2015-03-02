package uk.co.traintrackapp.traintrack.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import uk.co.traintrackapp.traintrack.model.Journey;

public class Utils {

    public static final int ASSETS = 1;
    public static final int FILESYSTEM = 2;
    public static final int BLUE = Color.parseColor("#33b5e5");
    public static final String API_BASE_URL = "http://192.168.1.73:3000";
    private static final String JSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    /**
     * @param message
     *            the log message to write
     */
    public static void log(String message) {
        Log.i("TrainTrack", message);
    }

    /**
     * @param hour
     *            the hour
     * @param minute
     *            the minute
     */
    public static String zeroPadTime(int hour, int minute) {
        String time = "";
        if (hour < 10) {
            time = "0";
        }
        time += hour + ":";
        if (minute < 10) {
            time += "0";
        }
        time += minute;
        return time;
    }

    /**
     * @param url
     *            the URL to send to
     * @param postData
     *            the raw string to send as the payload
     */
    public static String httpPost(String url, String postData) {
        StringBuilder builder = new StringBuilder();
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(postData));
            httpPost.setHeader("Content-Type", "text/xml");
            HttpResponse response = client.execute(httpPost);
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    content));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            Utils.log(e.getMessage());
        }
        return builder.toString();
    }


    /**
     * @param url
     *            the URL to get from
     */
    public static String httpGet(String url) {
        return httpGet(url, "");
    }

    /**
     * @param url
     *            the URL to get from
     * @param getData
     *            the raw string to send as the payload
     */
    public static String httpGet(String url, String getData) {
        StringBuilder builder = new StringBuilder();
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url + "?" + getData);
            httpGet.setHeader("Accept", "application/json");
            HttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    content));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            Utils.log(e.getMessage());
        }
        return builder.toString();
    }

    /**
     * @param hourOfDay
     *            0-23 hours
     * @param minute
     *            0 - 59 minutes
     *
     * @return date object
     */
    public static Date getDateWithTime(int hourOfDay, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, hourOfDay);
        cal.set(Calendar.MINUTE, minute);
        return cal.getTime();
    }

    /**
     * @param time
     *            in the format hh:mm
     *
     * @return date object
     */
    public static Date getDateWithTime(String time) {
        if (time.split(":").length != 2) {
            return null;
        }
        int hourOfDay = Integer.valueOf(time.split(":")[0]);
        int minute = Integer.valueOf(time.split(":")[1]);
        return getDateWithTime(hourOfDay, minute);
    }

    /**
     * Convert date to a date object
     * @param date as a string 2015-03-02T09:07:00.000Z
     * @return date
     */
    public static Date getDateFromString(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat(JSON_DATE_FORMAT);
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            Utils.log(e.getMessage());
            return null;
        }
    }

    /**
     * Convert date to a string format
     * @param date to a string 2015-03-02T09:07:00.000Z
     * @return date
     */
    public static String getStringFromDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(JSON_DATE_FORMAT);
        return formatter.format(date);
    }

}
