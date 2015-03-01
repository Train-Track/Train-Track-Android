package uk.co.traintrackapp.traintrack.api;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.traintrackapp.traintrack.R;
import uk.co.traintrackapp.traintrack.model.Station;
import uk.co.traintrackapp.traintrack.utils.Utils;

public class CallingPoint {

    private static final String ON_TIME = "On time";
    public static final int START = R.drawable.start;
    public static final int STOP = R.drawable.stop;
    public static final int END = R.drawable.end;

    private Station station;
    private String scheduledTime;
    private String estimatedTime;
    private String actualTime;
    private int icon;

    public CallingPoint() {

    }

    public CallingPoint(JSONObject json) {
        try {
            this.station = new Station(json.getJSONObject("station"));
            this.scheduledTime = json.getString("st");
            this.estimatedTime = json.getString("et");
            this.actualTime = json.getString("at");
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public void setActualTime(String actualTime) {
        this.actualTime = actualTime;
    }

    public Station getStation() {
        return station;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public String getActualTime() {
        return actualTime;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getIcon() {
        return icon;
    }

    public String toString() {
        return getScheduledTime() + " " + getStation() + " " + getTime() + "\n";
    }

    public boolean hasArrived() {
        return !getActualTime().isEmpty();
    }

    public boolean isOnTime() {
        return getEstimatedTime().equalsIgnoreCase(ON_TIME);
    }

    public String getTime() {
        if (hasArrived()) {
            return getActualTime();
        } else if (isOnTime()) {
            return getScheduledTime();
        } else {
            return getEstimatedTime();
        }
    }

}