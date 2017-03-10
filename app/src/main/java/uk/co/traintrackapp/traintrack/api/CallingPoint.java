package uk.co.traintrackapp.traintrack.api;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.traintrackapp.traintrack.R;
import uk.co.traintrackapp.traintrack.model.Station;
import uk.co.traintrackapp.traintrack.utils.Utils;

public class CallingPoint {

    public static final int START = R.drawable.start;
    public static final int STOP = R.drawable.stop;
    public static final int END = R.drawable.end;

    private Station station;
    private String scheduledTimeArrival;
    private String estimatedTimeArrival;
    private String actualTimeArrival;
    private String scheduledTimeDeparture;
    private String estimatedTimeDeparture;
    private String actualTimeDeparture;
    private int icon;

    public CallingPoint() {
        this.station = new Station();
        this.scheduledTimeArrival = "";
        this.estimatedTimeArrival = "";
        this.actualTimeArrival = "";
        this.scheduledTimeDeparture = "";
        this.estimatedTimeDeparture = "";
        this.actualTimeDeparture = "";
    }

    public CallingPoint(JSONObject json) {
        this();
        try {

            if (!json.isNull("station")) {
                this.station = new Station(json.getJSONObject("station"));
            }

            if (!json.isNull("sta")) {
                this.scheduledTimeArrival = json.getString("sta");
            }

            if (!json.isNull("eta")) {
                this.estimatedTimeArrival = json.getString("eta");
            }

            if (!json.isNull("ata")) {
                this.actualTimeArrival = json.getString("ata");
            }

            if (!json.isNull("std")) {
                this.scheduledTimeDeparture = json.getString("std");
            }

            if (!json.isNull("etd")) {
                this.estimatedTimeDeparture = json.getString("etd");
            }

            if (!json.isNull("atd")) {
                this.actualTimeDeparture = json.getString("atd");
            }

        } catch (JSONException e) {
            Utils.log("Calling Point: " + e.getMessage());
        }
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public Station getStation() {
        return station;
    }

    public String getScheduledTimeArrival() {
        return scheduledTimeArrival;
    }

    public void setScheduledTimeArrival(String scheduledTimeArrival) {
        this.scheduledTimeArrival = scheduledTimeArrival;
    }

    public String getEstimatedTimeArrival() {
        return estimatedTimeArrival;
    }

    public void setEstimatedTimeArrival(String estimatedTimeArrival) {
        this.estimatedTimeArrival = estimatedTimeArrival;
    }

    public String getActualTimeArrival() {
        return actualTimeArrival;
    }

    public void setActualTimeArrival(String actualTimeArrival) {
        this.actualTimeArrival = actualTimeArrival;
    }

    public String getScheduledTimeDeparture() {
        return scheduledTimeDeparture;
    }

    public void setScheduledTimeDeparture(String scheduledTimeDeparture) {
        this.scheduledTimeDeparture = scheduledTimeDeparture;
    }

    public String getEstimatedTimeDeparture() {
        return estimatedTimeDeparture;
    }

    public void setEstimatedTimeDeparture(String estimatedTimeDeparture) {
        this.estimatedTimeDeparture = estimatedTimeDeparture;
    }

    public String getActualTimeDeparture() {
        return actualTimeDeparture;
    }

    public void setActualTimeDeparture(String actualTimeDeparture) {
        this.actualTimeDeparture = actualTimeDeparture;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getIcon() {
        return icon;
    }

    public String toString() {
        return getScheduledTimeDeparture() + " " + getStation() + " " + getTime() + "\n";
    }

    public boolean hasArrived() {
        return !getActualTimeArrival().isEmpty();
    }

    public boolean isOnTime() {
        return getEstimatedTimeArrival().equalsIgnoreCase(getScheduledTimeArrival());
    }

    public String getScheduledTime() {
        String std = getScheduledTimeDeparture();
        if ((std != null) && (!std.isEmpty())) {
            return std;
        } else {
            return getScheduledTimeArrival();
        }
    }

    public String getTime() {
        if (hasArrived()) {
            return getActualTimeArrival();
        } else if (isOnTime()) {
            return getEstimatedTimeArrival();
        } else {
            return getScheduledTimeArrival();
        }
    }

}