package uk.co.traintrackapp.traintrack.utils;

import android.graphics.Color;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

    public static final int ASSETS = 1;
    public static final int FILESYSTEM = 2;
    public static final int BLUE = Color.parseColor("#33b5e5");
    public static final String ARGS_PAGE_TITLE = "ARGS_PAGE_TITLE";
    public static final String API_BASE_URL = "http://192.168.1.69:3000";
    private static final String JSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

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
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("API-KEY", "TEST");
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            try(DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                wr.write(postData.getBytes(StandardCharsets.UTF_8));
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
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
            HttpURLConnection conn = (HttpURLConnection) new URL(url + "?" + getData).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("API-KEY", "TEST");
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
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
        if (date == null) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(JSON_DATE_FORMAT);
        return formatter.format(date);
    }

}
