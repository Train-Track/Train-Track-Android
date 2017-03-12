package uk.co.traintrackapp.traintrack.utils;

import android.graphics.Color;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Utils {

    public static final int ASSETS = 1;
    public static final int FILESYSTEM = 2;
    public static final int BLUE = Color.parseColor("#33b5e5");
    public static final String ARGS_PAGE_TITLE = "ARGS_PAGE_TITLE";
    public static final String ARGS_STATION_UUID = "ARGS_STATION_UUID";
    public static final String ARGS_JOURNEY_LEG = "ARGS_JOURNEY_LEG";
    public static final String ARGS_JOURNEY_LEG_UUID = "ARGS_JOURNEY_LEG_UUID";
    public static final String ARGS_JOURNEY_UUID = "ARGS_JOURNEY_UUID";
    public static final String ARGS_SERVICE_ID = "ARGS_SERVICE_ID";
    public static final String ARGS_SERVICE = "ARGS_SERVICE";
    public static final String API_BASE_URL = "http://192.168.1.69:3000";
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
     * Convert an ISO8601 formatted date-time string into an object
     * @param input the date-time string
     * @return the date-time object
     */
    public static DateTime getDateTimeFromString(String input) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(JSON_DATE_FORMAT);
        if (input == null) {
            return null;
        }
        return formatter.parseDateTime(input);
    }

    /**
     * Convert a date-time into an ISO8601 formatted string
     * @param input the date-time object
     * @return the formatted string
     */
    public static String getStringFromDateTime(DateTime input) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(JSON_DATE_FORMAT);
        if (input == null) {
            return null;
        }
        return formatter.print(input);
    }

}
